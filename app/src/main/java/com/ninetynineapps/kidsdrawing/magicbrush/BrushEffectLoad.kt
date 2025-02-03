package com.ninetynineapps.kidsdrawing.magicbrush

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.PorterDuff.Mode
import android.util.Log
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import java.io.IOException

class BrushEffectLoad {

    private var alpha = 120
    private var bitmap: Bitmap? = null
    private var bitmaps: Array<Bitmap?>? = null
    private var brushEffectFromResource: BrushEffectFromResource? = null
    private var column = 1
    protected lateinit var contextVal: Context
    private var deviceWidth: Int = 0
    private var deviceHeight: Int = 0
    private var f1440b: Float = 0.toFloat()
    private var f1449k: Paint? = null
    private var f1452n: Float = 0.toFloat()
    private var path: String? = null
    private var rotate = false
    private var row = 1
    private var unknown = 0.15f
    internal var aBitmap: Bitmap? = null

    enum class BrushEffectFromResource {
        RES,
        ASSERT,
        FILTERED,
        ONLINE,
        CACHE
    }

    fun m2413a() {
        if (this.bitmaps != null) {
            for (bitmap in this.bitmaps!!) {
                if (!(bitmap == null || bitmap.isRecycled)) {
                    bitmap.recycle()
                }
            }
        }
        this.bitmaps = null
        if (this.bitmap != null && !this.bitmap!!.isRecycled) {
            this.bitmap!!.recycle()
            this.bitmap = null
        }
    }

    fun m2416a(context: Context, pattern: String) {
        m2413a()
        this.f1449k = Paint()
        this.f1449k!!.xfermode = PorterDuffXfermode(Mode.SCREEN)
        if (this.alpha != 0) {
            this.f1449k!!.alpha = this.alpha
        }
        this.bitmaps = arrayOfNulls(this.row * this.column)
        try {
            val inputStream = context.assets.open(CommonConstants.DirMagicBrushContent + "/" + pattern)
            aBitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (aBitmap == null || aBitmap!!.isRecycled) {
            Log.e("bitmap", "null")
            return
        }
        val width = aBitmap!!.width / this.column
        val height = aBitmap!!.height / this.row
        this.bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888)
        var i = 0
        for (i2 in 0 until this.row) {
            var i3 = 0
            while (i3 < this.column) {
                this.bitmaps!![i] = Bitmap.createBitmap(aBitmap!!, i3 * width, i2 * height, width, height)
                i3++
                i++
            }
        }
        this.deviceWidth = 1000
        this.deviceHeight = 1000
        this.f1452n = this.deviceWidth.toFloat() * this.unknown
        this.f1440b = 350f
    }

    fun onCanvas(canvas: Canvas, f: Float, f2: Float, i: Int, i2: Int, i3: Int, i4: Int) {
        var i3 = i3
        if (this.bitmap != null && !this.bitmap!!.isRecycled) {
            val canvas2 = Canvas(this.bitmap!!)
            canvas2.drawColor(0, Mode.CLEAR)
            if (this.bitmaps != null && this.bitmaps!!.size > i2 && this.bitmaps!![i2] != null && !this.bitmaps!![i2]!!.isRecycled) {
                canvas2.drawBitmap(this.bitmaps!![i2]!!, 0.0f, 0.0f, null)
                if (this.alpha != 0) {
                    canvas2.drawColor(i, Mode.MULTIPLY)
                }
                val matrix = Matrix()
                if (this.rotate) {
                    if (i4 == 0) {
                        i3 += 315
                    }
                    matrix.postRotate(i3.toFloat())
                }
                val width = this.f1452n * (canvas.width.toFloat() / this.deviceWidth.toFloat()) / this.f1440b
                matrix.postScale(width, width)
                matrix.postTranslate(
                    f - this.aBitmap!!.width.toFloat() * width / 2f,
                    f2 - this.aBitmap!!.height.toFloat() * width / 2f
                )
                canvas.drawBitmap(aBitmap!!, matrix, this.f1449k)
                canvas.drawBitmap(aBitmap!!, matrix, null)
            }
        }
    }



    fun setContext(context1: Context) {
        this.contextVal = context1
    }

    internal fun setFromResource(brushEffectFromResource: BrushEffectFromResource) {
        this.brushEffectFromResource = brushEffectFromResource
    }

    internal fun GetRow(): Int {
        return this.row
    }

    internal fun setRow(row: Int) {
        this.row = row
    }

    internal fun GetColumn(): Int {
        return this.column
    }

    internal fun setColumn(column: Int) {
        this.column = column
    }

    internal fun setRotate(rotate: Boolean) {
        this.rotate = rotate
    }

    internal fun setPath(path: String) {
        this.path = path
    }

    internal fun setUnknown(unknown: Float) {
        this.unknown = unknown
    }

    internal fun setAlpha(i: Int) {
        this.alpha = i
    }



}