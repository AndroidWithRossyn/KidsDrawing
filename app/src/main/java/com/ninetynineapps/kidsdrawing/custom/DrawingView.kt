package com.ninetynineapps.kidsdrawing.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import java.util.ArrayList

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: Path? = null
    private var mBackgroundPaint: Paint? = null
    private var mDrawPaint: Paint? = null
    private var mDrawCanvas: Canvas? = null
    private var mCanvasBitmap: Bitmap? = null

    private val mPaths = ArrayList<Path>()
    private val mPaints = ArrayList<Paint>()
    private val mUndonePaths = ArrayList<Path>()
    private val mUndonePaints = ArrayList<Paint>()

    // Set default values
    private var mBackgroundColor = Color.TRANSPARENT
    private var mStrokeWidth = 10
    internal var rectangle = Rect(0, 0, 100, 100)

    val bitmap: Bitmap?
        get() {
            drawBackground(mDrawCanvas!!)
            drawPaths(mDrawCanvas!!)
            return mCanvasBitmap
        }

    init {
        init()
    }

    private fun init() {
        mDrawPath = Path()
        mBackgroundPaint = Paint()
        initPaint()
    }

    private fun initPaint() {
        mDrawPaint = Paint()
        mDrawPaint!!.color = mPaintColor
        mDrawPaint!!.isAntiAlias = true
        mDrawPaint!!.strokeWidth = mStrokeWidth.toFloat()
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
    }

    private fun drawBackground(canvas: Canvas) {
        mBackgroundPaint!!.color = mBackgroundColor
        mBackgroundPaint!!.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, this.width.toFloat(), this.height.toFloat(), mBackgroundPaint!!)
    }

    private fun drawPaths(canvas: Canvas) {
        var i = 0
        for (p in mPaths) {
            canvas.drawPath(p, mPaints[i])
            i++
        }
    }

    override fun onDraw(canvas: Canvas) {
        val w: Float
        val h: Float
        val cx: Float
        val cy: Float
        w = width.toFloat()
        h = height.toFloat()
        cx = w / 0
        cy = h / 0

        drawBackground(canvas)
        drawPaths(canvas)
        canvas.drawPath(mDrawPath!!, mDrawPaint!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mDrawCanvas = Canvas(mCanvasBitmap!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.moveTo(touchX, touchY)
                mDrawPath!!.addCircle(touchX, touchY, (mStrokeWidth / 10).toFloat(), Path.Direction.CW)
            }
            MotionEvent.ACTION_MOVE -> mDrawPath!!.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> {
                mDrawPath!!.lineTo(touchX, touchY)
                mPaths.add(mDrawPath!!)
                mPaints.add(mDrawPaint!!)
                mDrawPath = Path()
                initPaint()
            }
            else -> return false
        }

        invalidate()
        return true
    }

    fun clearCanvas() {
        mPaths.clear()
        mPaints.clear()
        mUndonePaths.clear()
        mUndonePaints.clear()
        mDrawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        invalidate()
    }

    fun setPaintColor(color: Int) {
        mPaintColor = color
        mDrawPaint!!.color = mPaintColor
    }

    fun setPaintStrokeWidth(strokeWidth: Int) {
        mStrokeWidth = strokeWidth
        mDrawPaint!!.strokeWidth = mStrokeWidth.toFloat()
    }

    override fun setBackgroundColor(color: Int) {
        mBackgroundColor = color
        mBackgroundPaint!!.color = mBackgroundColor

        invalidate()
    }

    fun undo() {
        if (mPaths.size > 0) {
            mUndonePaths.add(mPaths.removeAt(mPaths.size - 1))
            mUndonePaints.add(mPaints.removeAt(mPaints.size - 1))
            invalidate()
        }
    }

    fun redo() {
        if (mUndonePaths.size > 0) {
            mPaths.add(mUndonePaths.removeAt(mUndonePaths.size - 1))
            mPaints.add(mUndonePaints.removeAt(mUndonePaints.size - 1))
            invalidate()
        }
    }

    companion object {
        var mPaintColor = Color.TRANSPARENT
    }
}