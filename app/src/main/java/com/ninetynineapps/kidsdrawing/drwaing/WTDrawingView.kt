package com.ninetynineapps.kidsdrawing.drwaing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import java.util.*

class WTDrawingView(internal var context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var initialized: Boolean = false
    private val eraserPaint = Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
    private var drawCanvas: Canvas? = null
    private var drawBitmap: Bitmap? = null
    private val undoBitmap: Bitmap? = null

    private var dirtyRect: RectF? = null

    private var drawPath: WTBezierPath? = null
    private var pathArray: LinkedList<WTBezierPath>? = null
    private var redoPathArray: LinkedList<WTBezierPath>? = null

    private var drawingMode: Int = 0
    private val points = arrayOfNulls<PointF>(5)
    private var pointIndex: Int = 0
    private var movedPointCount: Int = 0

    var strokeColorVal = 0



    var strokeWidth: Float = 0.toFloat()
        set(strokeWidth) {
            field = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, strokeWidth, resources.displayMetrics)
            drawPaint.strokeWidth = this.strokeWidth
        }


    var eraserWidth: Float = 0.toFloat()
        set(eraserWidth) {
            field = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, eraserWidth, resources.displayMetrics)
            eraserPaint.strokeWidth = this.eraserWidth
        }


    fun setBlurSize(blueValue: Int) {
        drawPaint.maskFilter = BlurMaskFilter(blueValue.toFloat(), BlurMaskFilter.Blur.NORMAL)
    }

    fun clearMaskFilter() {
        drawPaint.maskFilter = null
    }

    fun getStrokeColor(): Int {
        return strokeColorVal
    }

    fun setStrokeColor(strokeColorInner: Int) {
        strokeColorVal = strokeColorInner
        drawPaint.color = strokeColorInner
    }

    fun setPencilView(isPencil: Boolean) {
        strokeWidth = if (isPencil) {
            1F
        } else {
            10F
        }
    }


    fun setEraserMode(drawEraser: Boolean) {
        drawingMode = if (drawEraser) ERASER else DRAW
    }

    fun isDrawingMode(): Boolean {
        return drawingMode == ERASER
    }


    fun undo() {
        val emptyPaint = Paint()
        emptyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        drawCanvas!!.drawPaint(emptyPaint)

        if (!pathArray!!.isEmpty()) {
            redoPathArray!!.add(pathArray!![pathArray!!.size - 1])
            pathArray!!.removeLast()

//            CommonConstants.IS_CLEAR = pathArray!!.size == 0
            Log.e("TAG", "undo:::::Lats If::: " +pathArray!!.size)
        }else{
            if (CommonConstants.IS_CLEAR_MAGIC && CommonConstants.IS_CLEAR_TEXT && CommonConstants.IS_CLEAR_STICKER){
                CommonConstants.IS_CLEAR = true
                CommonConstants.IS_CLEAR_BRUSH = true
                CommonConstants.IS_CLEAR_PENCIL = true
                Log.e("TAG", "undo::::::IS_CLEAR ${CommonConstants.IS_CLEAR_MAGIC}   ${CommonConstants.IS_CLEAR_TEXT}   ${CommonConstants.IS_CLEAR_STICKER}" )
            }
            Log.e("TAG", "undo:::::Lats Else::: " +pathArray!!.size)
        }

        if (CommonConstants.IS_CLEAR_TEXT ){
            /*if (pathArray!!.size == 0){
                CommonConstants.IS_CLEAR = true;
            }else{
//                if (CommonConstants.IS_CLEAR_MAGIC)
                CommonConstants.IS_CLEAR = false
            }*/
            if (CommonConstants.IS_CLEAR_MAGIC && CommonConstants.IS_CLEAR_STICKER){
                CommonConstants.IS_CLEAR = pathArray!!.size == 0
            }else{
                CommonConstants.IS_CLEAR = false
            }

        }


        for (path in pathArray!!) {
            drawPaint.style = if (path.isCircle) Paint.Style.FILL else Paint.Style.STROKE
            if (path.isEraser) {
                drawCanvas!!.drawPath(path, eraserPaint)
            } else {
                drawPaint.color = path.strokeColor
                drawPaint.strokeWidth = path.strokeWidth
                drawCanvas!!.drawPath(path, drawPaint)
            }
        }

        drawPaint.strokeWidth = this.strokeWidth
        drawPaint.color = this.strokeColorVal
        eraserPaint.strokeWidth = this.eraserWidth
        invalidate()
    }


    fun redo() {
        if (redoPathArray!!.isNotEmpty()) {
            pathArray!!.add(redoPathArray!![redoPathArray!!.size - 1])
            redoPathArray!!.removeAt(redoPathArray!!.size - 1)
            for (path in pathArray!!) {
                drawPaint.style = if (path.isCircle) Paint.Style.FILL else Paint.Style.STROKE
                drawPaint.color = path.strokeColor
                drawPaint.strokeWidth = path.strokeWidth
                drawCanvas!!.drawPath(path, drawPaint)
            }
            Log.e("TAG", "redo:::::pathArray If "+pathArray!!.size+"  "+redoPathArray!!.size )
            CommonConstants.IS_CLEAR = false
            drawPaint.strokeWidth = this.strokeWidth
            drawPaint.color = this.strokeColorVal
            eraserPaint.strokeWidth = this.eraserWidth
            invalidate()
        }else{
            Log.e("TAG", "redo:::::pathArray Else "+pathArray!!.size+"  "+redoPathArray!!.size )
        }
    }


    fun clear() {
        val emptyPaint = Paint()
        emptyPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        drawCanvas!!.drawPaint(emptyPaint)

        pathArray!!.clear()
        if (drawPath != null) {
            drawPath!!.reset()
        }

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("TAG", "onTouchEvent:::::Edit image  ")

        CommonConstants.IS_CLEAR =false
        CommonConstants.IS_CLEAR_BRUSH = false
        CommonConstants.IS_CLEAR_PENCIL = false
        if (strokeColorVal != 0) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                this.touchesBegan(PointF(event.x, event.y))
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                this.touchesMoved(PointF(event.x, event.y))
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                this.touchesEnded(PointF(event.x, event.y))
            }
        }else{
            Log.e("TAG", "onTouchEvent:Else:::: " )
        }
        return true
    }

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (initialized) {
            canvas.drawBitmap(drawBitmap!!, 0f, 0f, null)
        } else {
            init(this.width, this.height)
        }
    }

    private fun init(width: Int, height: Int) {
        if (!initialized) {
            pathArray = LinkedList()
            redoPathArray = LinkedList()

            drawingMode = DRAW

            drawBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
            drawCanvas = Canvas(drawBitmap!!)

            strokeWidth = DEFAULT_STROKE_WIDTH
            eraserWidth = DEFAULT_ERASER_WIDTH

            drawPaint.isAntiAlias = true
            drawPaint.style = Paint.Style.STROKE
            drawPaint.strokeCap = Paint.Cap.ROUND
            drawPaint.color = Color.WHITE
            drawPaint.strokeJoin = Paint.Join.ROUND


            eraserPaint.isAntiAlias = true
            eraserPaint.style = Paint.Style.STROKE
            eraserPaint.strokeCap = Paint.Cap.ROUND
            eraserPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

            dirtyRect = RectF()

            initialized = true
        }
    }


    private fun currentPaint(): Paint {
        return if (drawingMode == ERASER) eraserPaint else drawPaint
    }

    private fun touchesBegan(p: PointF) {

        if (drawingMode != DRAW && drawingMode != ERASER) {
            drawingMode = DRAW
        }

        movedPointCount = 0
        pointIndex = 0
        points[0] = p

        drawPaint.style = Paint.Style.STROKE
        eraserPaint.style = Paint.Style.STROKE

        drawPath = WTBezierPath()
        drawPath!!.strokeColor = this.strokeColorVal
        drawPath!!.strokeWidth = this.strokeWidth
        drawPath!!.isEraser = drawingMode == ERASER
    }

    private fun touchesMoved(p: PointF) {

        movedPointCount++
        pointIndex++
        points[pointIndex] = p

        if (pointIndex == 4) {
            points[3] = PointF((points[2]!!.x + points[4]!!.x) / 2, (points[2]!!.y + points[4]!!.y) / 2)

            moveToPoint(points[0]!!)
            addCurveToPoint(points[3]!!, points[1]!!, points[2]!!)

            drawCanvas!!.drawPath(drawPath!!, currentPaint())

            // Calc dirty rect
            val pathWidth = currentPaint().strokeWidth
            drawPath!!.computeBounds(dirtyRect!!, true)
            dirtyRect!!.left = dirtyRect!!.left - pathWidth
            dirtyRect!!.top = dirtyRect!!.top - pathWidth
            dirtyRect!!.right = dirtyRect!!.right + pathWidth
            dirtyRect!!.bottom = dirtyRect!!.bottom + pathWidth

            val invalidRect = Rect()
            dirtyRect!!.round(invalidRect)
            invalidate(invalidRect)

            points[0] = points[3]
            points[1] = points[3]
            points[2] = points[4]
            pointIndex = 2
        }
    }

    private fun touchesEnded(p: PointF) {

        if (movedPointCount < 3) {
            val paint = currentPaint()
            drawPath!!.reset()
            drawPath!!.isCircle = true
            drawPath!!.addCircle(points[0]!!.x, points[0]!!.y, paint.strokeWidth, Path.Direction.CW)
            paint.style = Paint.Style.FILL
            drawCanvas!!.drawPath(drawPath!!, paint)
        }

        movedPointCount = 0
        pointIndex = 0
        pathArray!!.add(drawPath!!)
        invalidate()
    }

    private fun moveToPoint(p: PointF) {
        drawPath!!.moveTo(p.x, p.y)
    }

    private fun addCurveToPoint(p: PointF, controlPoint1: PointF, controlPoint2: PointF) {
        drawPath!!.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p.x, p.y)
    }

    companion object {

        private val DRAW = 1
        private val ERASER = 2

        //    private static final int DEFAULT_STROKE_COLOR = Color.BLACK;
        private val DEFAULT_STROKE_WIDTH = 5.0f
        private val DEFAULT_ERASER_WIDTH = 20.0f

        private val drawPaint = Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)

    }
}
