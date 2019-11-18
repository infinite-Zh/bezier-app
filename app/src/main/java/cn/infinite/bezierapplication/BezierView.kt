package cn.infinite.bezierapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @author bug小能手
 * Created on 2019/11/18.
 */
class BezierView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val mPaint: Paint by lazy {
        Paint().apply {
            color = Color.RED
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }
    }
    private val mPath: Path by lazy {
        Path().apply {
        }
    }

    private val mPointPaint: Paint by lazy {
        Paint().apply {
            color = Color.BLACK
            strokeWidth = 20f
            style = Paint.Style.FILL_AND_STROKE
        }
    }
    private val mPoints = mutableListOf<PointF>()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            mPoints.add(PointF(event.x, event.y))
            return true
        }
        return super.onTouchEvent(event)
    }

    fun drawBezier(canvas: Canvas) {
        if (mPoints.size <= 1) {
            return
        }
        if (mPoints.size == 2) {
            mPath.moveTo(mPoints[0].x, mPoints[0].y)
            mPath.lineTo(mPoints[1].x, mPoints[1].y)
        } else {

        }


        canvas.drawPath(mPath, mPaint)

    }



    fun reset() {
        mPath.reset()
        mPoints.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPath.moveTo(100f, 100f)
//        mPath.quadTo(300f,100f,400f,400f)
        mPath.cubicTo(300f, 100f, 400f, 400f, 10f, 800f)
        canvas?.apply {
            drawPath(mPath, mPaint)
            drawPoint(100f, 100f, mPointPaint)
            drawPoint(300f, 100f, mPointPaint)
            drawPoint(400f, 400f, mPointPaint)
            drawPoint(10f, 800f, mPointPaint)

        }
    }
}