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
    private val mPointPaint: Paint by lazy {
        Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
        }
    }
    private val mLinePaint: Paint by lazy {
        Paint().apply {
            color = Color.GRAY
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }
    }
    private val mBezierPath: Path by lazy {
        Path().apply {
        }
    }
    private val mLinePath: Path by lazy {
        Path().apply {
        }
    }

    private val mControlPoints = mutableListOf<PointF>()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            mControlPoints.add(PointF(event.x, event.y))
            mBezierPath.reset()
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }


    fun reset() {
        mBezierPath.reset()
        mControlPoints.clear()
        drawPath = false
        invalidate()
    }

    fun compute() {

        var i = 1 / 1000f
        while (i < 1) {
            val p = PointF(
                calculateBezier(mControlPoints.size - 1, 0, i, true),
                calculateBezier(mControlPoints.size - 1, 0, i, false)
            )
            if (i == 1 / 1000f) {
                mBezierPath.moveTo(p.x, p.y)
            } else {
                mBezierPath.lineTo(p.x, p.y)
            }

            i += 1 / 1000f
        }
        drawPath = true
        invalidate()
    }

    /**
     * p(i,j)=
     *
     * */
    private fun calculateBezier(order: Int, j: Int, t: Float, calculateX: Boolean): Float {
        return if (order == 1) {
            if (calculateX) {
                (1 - t).times(mControlPoints[j].x) + t.times(mControlPoints[j + 1].x)
            } else {
                (1 - t).times(mControlPoints[j].y) + t.times(mControlPoints[j + 1].y)
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

    private var drawPath = false
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            if (drawPath) {
                drawPath(mBezierPath, mPaint)
            }
            mLinePath
                .reset()
            mControlPoints.forEachIndexed { index, p ->
                drawCircle(p.x, p.y, 8f, mPointPaint)
                if (index==0){
                    mLinePath.moveTo(p.x,p.y)
                }else{
                    mLinePath.lineTo(p.x,p.y)
                }
            }
            drawPath(mLinePath,mLinePaint)
        }

    }
}