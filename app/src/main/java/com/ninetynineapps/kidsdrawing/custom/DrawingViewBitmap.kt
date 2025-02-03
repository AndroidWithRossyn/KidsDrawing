package com.ninetynineapps.kidsdrawing.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingViewBitmap : View {

    lateinit var drawPath: Path
    lateinit var drawPaint: Paint
    lateinit var canvasPaint: Paint
    var paintColor = Color.TRANSPARENT
    var strokeWidth = 0
    lateinit var drawCanvas: Canvas
    private var canvasBitmap: Bitmap? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupDrawing()
    }

    fun setupDrawing() {
        drawPath = Path()
        drawPaint = Paint()
        drawPaint.color = paintColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = strokeWidth.toFloat()
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(canvasBitmap!!, 0f, 0f, canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        //respond to down, move and up events
        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> {
                drawPath.lineTo(touchX, touchY)
                drawCanvas.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> return false
        }//setupDrawing();
        invalidate()
        return true
    }

    fun setPattern(context: Context, string: String) {
        //        int patternID = magicBrushView.getResources().getIdentifier(string, "drawable", context.getPackageName());
        //        Bitmap patternBMP = BitmapFactory.decodeResource(magicBrushView.getResources(), patternID);
        //        BitmapShader patternBMPshader = new BitmapShader(patternBMP, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        //        drawPaint.setShader(patternBMPshader);
    }
}