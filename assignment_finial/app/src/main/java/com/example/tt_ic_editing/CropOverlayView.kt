package com.example.tt_ic_editing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min

class CropOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val borderPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val maskPaint = Paint().apply {
        color = Color.parseColor("#AA000000")
    }

    // 初始裁剪框
    private var cropRect = RectF()

    private var touchArea: TouchArea? = null
    private var lastX = 0f
    private var lastY = 0f

    enum class TouchArea { MOVE, LEFT, TOP, RIGHT, BOTTOM, LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM }

    private val handleSize = 40f  // 四角大小检测范围

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // 初始化为居中的正方形
        val size = min(w, h) * 0.6f
        val left = (w - size) / 2
        val top = (h - size) / 2
        cropRect.set(left, top, left + size, top + size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        // 让 cropRect 不被绘制（形成透明区域）
        canvas.clipOutRect(cropRect)

        // 现在画遮罩（裁剪外的区域）
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), maskPaint)

        canvas.restore()

        // 绘制裁剪边框
        canvas.drawRect(cropRect, borderPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchArea = getTouchArea(x, y)
                lastX = x
                lastY = y
                return touchArea != null
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = x - lastX
                val dy = y - lastY
                lastX = x
                lastY = y
                updateCropRect(dx, dy)
                invalidate()
            }

            MotionEvent.ACTION_UP -> touchArea = null
        }
        return true
    }

    private fun updateCropRect(dx: Float, dy: Float) {
        when (touchArea) {
            TouchArea.MOVE -> cropRect.offset(dx, dy)
            TouchArea.LEFT -> cropRect.left += dx
            TouchArea.RIGHT -> cropRect.right += dx
            TouchArea.TOP -> cropRect.top += dy
            TouchArea.BOTTOM -> cropRect.bottom += dy
            TouchArea.LEFT_TOP -> {
                cropRect.left += dx; cropRect.top += dy
            }

            TouchArea.RIGHT_TOP -> {
                cropRect.right += dx; cropRect.top += dy
            }

            TouchArea.LEFT_BOTTOM -> {
                cropRect.left += dx; cropRect.bottom += dy
            }

            TouchArea.RIGHT_BOTTOM -> {
                cropRect.right += dx; cropRect.bottom += dy
            }

            else -> {}
        }

        // 限制区域
        cropRect.left = cropRect.left.coerceAtLeast(0f)
        cropRect.top = cropRect.top.coerceAtLeast(0f)
        cropRect.right = cropRect.right.coerceAtMost(width.toFloat())
        cropRect.bottom = cropRect.bottom.coerceAtMost(height.toFloat())
    }

    private fun getTouchArea(x: Float, y: Float): TouchArea? {
        val r = cropRect

        return when {
            // 四角
            near(x, y, r.left, r.top) -> TouchArea.LEFT_TOP
            near(x, y, r.right, r.top) -> TouchArea.RIGHT_TOP
            near(x, y, r.left, r.bottom) -> TouchArea.LEFT_BOTTOM
            near(x, y, r.right, r.bottom) -> TouchArea.RIGHT_BOTTOM

            // 四边
            abs(x - r.left) < handleSize -> TouchArea.LEFT
            abs(x - r.right) < handleSize -> TouchArea.RIGHT
            abs(y - r.top) < handleSize -> TouchArea.TOP
            abs(y - r.bottom) < handleSize -> TouchArea.BOTTOM

            // 内部拖动
            r.contains(x, y) -> TouchArea.MOVE
            else -> null
        }
    }

    private fun near(x: Float, y: Float, px: Float, py: Float): Boolean {
        return abs(x - px) < handleSize && abs(y - py) < handleSize
    }

    fun getCropRect(): RectF = cropRect
}
