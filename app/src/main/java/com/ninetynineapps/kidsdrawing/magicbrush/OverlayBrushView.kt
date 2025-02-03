package com.ninetynineapps.kidsdrawing.magicbrush

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.PorterDuff.Mode
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.ninetynineapps.kidsdrawing.R
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Color
import com.ninetynineapps.kidsdrawing.common.CommonConstants


class OverlayBrushView(internal var context: Context, attributeSet: AttributeSet) :
    AppCompatImageView(context, attributeSet) {
    private var screenBitmap: Bitmap? = null
    private var brushEffectLoad: BrushEffectLoad? = null
    private val brushEffectLoadList = ArrayList<BrushEffectLoad>()
    private val getRandomEffects = ArrayList<GetRandomEffect>()
    private val arrOfBitmap = ArrayList<Bitmap>()
    private val lists = Stack<List<GetRandomEffect>>()
    lateinit var pattern: String

    private var xlast: Float = 0.toFloat()
    private var ylast: Float = 0.toFloat()
    private var touchCount = 0

    init {
        onTouchOverlayWhenBrushView = true
    }

    private fun setBrushResFoundMap(brushEffectLoad: BrushEffectLoad?) {
        if (brushEffectLoad != null) {
            this.brushEffectLoad = brushEffectLoad
            this.brushEffectLoad!!.m2416a(getContext(), pattern)
        } else {
            this.brushEffectLoad = null
        }

        if (this.screenBitmap == null || this.screenBitmap!!.isRecycled) {
            this.screenBitmap = Bitmap.createBitmap(1000, 1000, Config.ARGB_8888)
            setImageBitmap(this.screenBitmap)
        }
    }

    private fun setBrushRes(brushEffectLoad: BrushEffectLoad?) {
        if (brushEffectLoad != null) {
            this.brushEffectLoad = brushEffectLoad
            this.brushEffectLoad!!.m2416a(getContext(), pattern)
            return
        }
        this.brushEffectLoad = null
    }

    companion object {
        var onTouchOverlayWhenBrushView: Boolean = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        CommonConstants.IS_CLEAR = false
        CommonConstants.IS_CLEAR_MAGIC = false
        setBrushRes(brushEffectLoad)
        if (onTouchOverlayWhenBrushView || this.brushEffectLoad == null) {
            return false
        }
        val x = motionEvent.x
        val y = motionEvent.y
        when (motionEvent.action) {
            0 -> {
                actionDownTouch(x, y)
                xlast = x
                ylast = y
                touchCount = 0
            }

            2 -> {
                val finalX = motionEvent.x
                val finalY = motionEvent.y
                if (xlast < finalX) {
                    val diff = finalX - xlast
                    if (diff > context.resources.getInteger(R.integer.diff_brush_img)) {
                        actionMoveTouch(x, y)
                        xlast = x
                        ylast = y
                    }
                }

                if (xlast > finalX) {
                    val diff = xlast - finalX
                    if (diff > context.resources.getInteger(R.integer.diff_brush_img)) {
                        actionMoveTouch(x, y)
                        xlast = x
                        ylast = y
                    }
                }

                if (ylast < finalY) {
                    val diff = finalY - ylast
                    if (diff > context.resources.getInteger(R.integer.diff_brush_img)) {
                        actionMoveTouch(x, y)
                        xlast = x
                        ylast = y
                    }
                }

                if (ylast > finalY) {
                    val diff = ylast - finalY
                    if (diff > context.resources.getInteger(R.integer.diff_brush_img)) {
                        actionMoveTouch(x, y)
                        xlast = x
                        ylast = y
                    }
                }
            }
            1 -> {
            }
        }
        return true
    }


    private fun actionDownTouch(x: Float, y: Float) {
        val arrayList = ArrayList<GetRandomEffect>()
        val getRandomEffect = GetRandomEffect(x, y, this.brushEffectLoad!!)
        arrayList.add(getRandomEffect)
        this.getRandomEffects.add(getRandomEffect)
        this.lists.push(arrayList)
        this.brushEffectLoadList.add(this.brushEffectLoad!!)
        addBrushBitmap()
        // MainActivity.saveState = false;
    }

    private fun actionMoveTouch(x: Float, y: Float) {
        val getRandomEffect = GetRandomEffect(x, y, this.brushEffectLoad!!)
        this.getRandomEffects.add(getRandomEffect)
        if (!this.lists.isEmpty()) {
            (this.lists.peek() as ArrayList<GetRandomEffect>).add(getRandomEffect)
        }
        addBrushBitmap()
    }


    private fun addBrushBitmap() {
        if (this.screenBitmap != null && !this.screenBitmap!!.isRecycled) {
            val canvas = Canvas(this.screenBitmap!!)
            arrOfBitmap.add(screenBitmap!!)
            val it = this.getRandomEffects.iterator()
            val width = canvas.width.toFloat() / width.toFloat()
            val height = canvas.height.toFloat() / height.toFloat()
            if (it.hasNext()) {
                onCanvas(canvas, it.next(), width, height)
                it.remove()
            }
            setImageBitmap(this.screenBitmap)
        }
    }

    fun undoMagicBrush() {
        if (arrOfBitmap.isNotEmpty()) {
            arrOfBitmap.removeAt(arrOfBitmap.size - 1)
            val mutableBitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_transapent)
            val icon = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(icon)
            val it = this.getRandomEffects.iterator()
            val width = canvas.width.toFloat() / width.toFloat()
            val height = canvas.height.toFloat() / height.toFloat()
            if (it.hasNext()) {
                onCanvas(canvas, it.next(), width, height)
                it.remove()
            }
            setImageBitmap(icon)
        }
    }

    fun makeTransparent(bit: Bitmap, transparentColor: Color): Bitmap {
        val width = bit.width
        val height = bit.height
        val myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val allpixels = IntArray(myBitmap.height * myBitmap.width)
        bit.getPixels(allpixels, 0, myBitmap.width, 0, 0, myBitmap.width, myBitmap.height)
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height)

        for (i in 0 until myBitmap.height * myBitmap.width) {
            if (allpixels[i] as Color == transparentColor)
                allpixels[i] = Color.alpha(Color.TRANSPARENT)
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.width, 0, 0, myBitmap.width, myBitmap.height)
        return myBitmap
    }

    private fun onCanvas(
        canvas: Canvas,
        getRandomEffect: GetRandomEffect,
        width: Float,
        height: Float
    ) {
        this.brushEffectLoad!!.onCanvas(
            canvas,
            getRandomEffect.f11x * width,
            getRandomEffect.f12y * height,
            getRandomEffect.color,
            getRandomEffect.f1429c,
            getRandomEffect.f1430d,
            getRandomEffect.f1427a
        )
    }

    fun closeTouch() {
        if (this.brushEffectLoad != null) {
            this.brushEffectLoad!!.m2413a()
        }
        if (this.lists.isEmpty()) {
            if (!(this.screenBitmap == null || this.screenBitmap!!.isRecycled)) {
                this.screenBitmap!!.recycle()
                this.screenBitmap = null
            }
            setImageBitmap(this.screenBitmap)
        }
        onTouchOverlayWhenBrushView = true
    }

    fun setBrushStyle(pattern: String, context: Context) {
        this.context = context
        this.pattern = pattern
        if (this.brushEffectLoad == null) {
            this.brushEffectLoad =
                LoadBrush(getContext()).loadBrushInstance(getContext()).loadBrushInstance(0)
        }
        setBrushResFoundMap(this.brushEffectLoad)
        onTouchOverlayWhenBrushView = false
    }

    fun clearMagicBrush() {
        if (!(this.screenBitmap == null || this.screenBitmap!!.isRecycled)) {
            Canvas(this.screenBitmap!!).drawColor(-16711936, Mode.CLEAR)
            setImageBitmap(this.screenBitmap)
        }
        this.brushEffectLoadList.clear()
        this.lists.clear()

        if (CommonConstants.IS_CLEAR_STICKER && CommonConstants.IS_CLEAR_PENCIL && CommonConstants.IS_CLEAR_BRUSH && CommonConstants.IS_CLEAR_TEXT) {
            CommonConstants.IS_CLEAR = true
        }
        CommonConstants.IS_CLEAR_MAGIC = true
    }

    fun undoBrush() {

        if (!this.lists.isEmpty()) {
            brushEffectLoad = this.brushEffectLoad
            this.lists.pop()
            this.brushEffectLoadList.removeAt(this.brushEffectLoadList.size - 1)
            val canvas = Canvas(this.screenBitmap!!)
            canvas.drawColor(0, Mode.CLEAR)

            val width = canvas.width.toFloat() / width.toFloat()
            val height = canvas.height.toFloat() / height.toFloat()
            val it = this.lists.iterator()
            var i = 0
            while (it.hasNext()) {
                val list = it.next() as List<GetRandomEffect>
                val brushEffectLoad2 = this.brushEffectLoadList[i]
                if (brushEffectLoad2 != null) {
                    setBrushResFoundMap(brushEffectLoad2)
                }
                for (getRandomEffect in list) {
                    onCanvas(canvas, getRandomEffect, width, height)
                }
                i++
            }
            setBrushResFoundMap(brushEffectLoad)
            setImageBitmap(this.screenBitmap)
        }
    }
}
