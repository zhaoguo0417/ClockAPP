package com.example.clock.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.*

class AnalogClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 24f
        color = Color.parseColor("#4A8F7B")
    }
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.DKGRAY
    }
    private val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        textSize = 48f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }
    private val handPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
    }

    private val calendar = Calendar.getInstance()
    private val rect = Rect()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f
        val radius = min(cx, cy) * 0.9f

        // 1. 背景
        canvas.drawCircle(cx, cy, radius, bgPaint)

        // 2. 外圈
        canvas.drawCircle(cx, cy, radius, rimPaint)

        // 3. 刻度与数字
        for (i in 0 until 60) {
            val angle = Math.toRadians((i * 6).toDouble() - 90)
            val length = if (i % 5 == 0) radius * 0.12f else radius * 0.06f
            tickPaint.strokeWidth = if (i % 5 == 0) 8f else 4f
            val startR = radius - length
            val x1 = cx + cos(angle).toFloat() * startR
            val y1 = cy + sin(angle).toFloat() * startR
            val x2 = cx + cos(angle).toFloat() * radius
            val y2 = cy + sin(angle).toFloat() * radius
            canvas.drawLine(x1, y1, x2, y2, tickPaint)

            // 数字
            if (i % 5 == 0) {
                val num = if (i == 0) 12 else i / 5
                val tx = cx + cos(angle).toFloat() * (radius - length*2 - 20f)
                val ty = cy + sin(angle).toFloat() * (radius - length*2 - 20f) + numberPaint.textSize/3
                canvas.drawText(num.toString(), tx, ty, numberPaint)
            }
        }

        // 更新时间（带毫秒，平滑秒针）
        calendar.timeInMillis = System.currentTimeMillis()
        val hr = calendar.get(Calendar.HOUR).toFloat()
        val min = calendar.get(Calendar.MINUTE).toFloat()
        val sec = calendar.get(Calendar.SECOND).toFloat()
        val ms = calendar.get(Calendar.MILLISECOND).toFloat()

        // 4. 时针
        val hourAngle = Math.toRadians(((hr + min/60f) * 30f - 90f).toDouble())
        handPaint.strokeWidth = 16f
        handPaint.color = Color.DKGRAY
        drawHand(canvas, cx, cy, hourAngle, radius * 0.5f)

        // 5. 分针
        val minAngle = Math.toRadians((min * 6f - 90f).toDouble())
        handPaint.strokeWidth = 12f
        handPaint.color = Color.DKGRAY
        drawHand(canvas, cx, cy, minAngle, radius * 0.7f)

        // 6. 秒针（连续扫）
        val totalSeconds = sec + ms / 1000f
        val secAngle = Math.toRadians((totalSeconds * 6f - 90f).toDouble())
        handPaint.strokeWidth = 6f
        handPaint.color = Color.parseColor("#E91E63")
        drawHand(canvas, cx, cy, secAngle, radius * 0.8f)

        // 7. 中心点
        canvas.drawCircle(cx, cy, 16f, centerPaint)

        // 8. 平滑刷新
        postInvalidateOnAnimation()
    }

    private fun drawHand(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        angle: Double,
        length: Float
    ) {
        val x = cx + cos(angle).toFloat() * length
        val y = cy + sin(angle).toFloat() * length
        canvas.drawLine(cx, cy, x, y, handPaint)
    }
}
