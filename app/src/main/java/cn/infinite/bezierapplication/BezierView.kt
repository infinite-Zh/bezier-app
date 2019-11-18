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

    private fun drawBezier(canvas: Canvas) {
        if (mPoints.size <= 1) {
            return
        }

        canvas.drawPath(mPath, mPaint)

    }

    /**
     * p(i,j)=
     *
     * */
    private fun calculateBezier(order: Int, j: Int, t: Float, calculateX: Boolean): Float {
        return if (order == 1) {
            if (calculateX) {
                (1 - t).times(mPoints[j].x) + t.times(mPoints[j - 1].x)
            } else {
                (1 - t).times(mPoints[j].y) + t.times(mPoints[j - 1].y)
            }
        } else {
            (1 - t).times(calculateBezier(order - 1, j, t, calculateX)) + t.times(
                calculateBezier(
                    order - 1,
                    j + 1,
                    t,
                    calculateX
                )
            )
        }
    }


    fun reset() {
        mPath.reset()
        mPoints.clear()
        invalidate()
    }

    fun compute() {

        for (i in 0..999) {
        }
        var i = 1 / 1000f
        while (i < 1) {
            val p = PointF(
                calculateBezier(mPoints.size - 1, 0, i, true),
                calculateBezier(mPoints.size - 1, 0, i, false)
            )
            if (i == 1 / 1000f) {
                mPath.moveTo(p.x, p.y)
            } else {
                mPath.lineTo(p.x, p.y)
            }

            i += 1 / 1000
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        mPath.moveTo(100f, 100f)
////        mPath.quadTo(300f,100f,400f,400f)
//        mPath.cubicTo(300f, 100f, 400f, 400f, 10f, 800f)
//        canvas?.apply {
//            drawPath(mPath, mPaint)
//            drawPoint(100f, 100f, mPointPaint)
//            drawPoint(300f, 100f, mPointPaint)
//            drawPoint(400f, 400f, mPointPaint)
//            drawPoint(10f, 800f, mPointPaint)
//
//        }
        drawBezier(canvas!!)
    }
}