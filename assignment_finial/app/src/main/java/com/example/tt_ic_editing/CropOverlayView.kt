package com.example.tt_ic_editing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Region
import android.util.Log
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withSave

class CropOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val borderPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val maskPaint = Paint().apply {
        color = "#AA000000".toColorInt()
    }

    // 初始裁剪框
    private var cropRect = RectF()

    private var touchArea: TouchArea? = null
    private var lastX = 0f
    private var lastY = 0f

    var ratio: Float? = null
        set(value) {
            field = value

            value?.run {
                val width = cropRect.right - cropRect.left
                val height = cropRect.bottom - cropRect.top
                if (width < height * this) {
                    val newHeight = width / this
                    val half = (newHeight - height) / 2f
                    cropRect.top -= half
                    cropRect.bottom += half
                } else {
                    val newWidth = height * this
                    val half = (newWidth - width) / 2f
                    cropRect.left -= half
                    cropRect.right += half
                }
                invalidate()
            }
        }

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

        canvas.withSave {

            // 让 cropRect 不被绘制（形成透明区域）
            clipOutRect(cropRect)

            // 现在画遮罩（裁剪外的区域）
            drawRect(0f, 0f, width.toFloat(), height.toFloat(), maskPaint)

        }

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
//                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = x - lastX
                val dy = y - lastY
                lastX = x
                lastY = y
                updateCropRect(dx, dy)
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> touchArea = null
        }
        return true
    }

    private fun updateCropRect(dx: Float, dy: Float) {
        val newRect = RectF(cropRect)

        when (touchArea) {
            TouchArea.MOVE -> newRect.offset(dx, dy)
            TouchArea.LEFT -> {
                newRect.left += dx
                ratio?.run {
                    val width = newRect.right - newRect.left
                    val height = width / this
                    val half = (height - newRect.bottom + newRect.top) / 2f
                    newRect.top -= half
                    newRect.bottom += half
                }
            }

            TouchArea.RIGHT -> {
                newRect.right += dx
                ratio?.run {
                    val width = newRect.right - newRect.left
                    val height = width / this
                    val half = (height - newRect.bottom + newRect.top) / 2f
                    newRect.top -= half
                    newRect.bottom += half
                }
            }

            TouchArea.TOP -> {
                newRect.top += dy
                ratio?.run {
                    val height = newRect.bottom - newRect.top
                    val width = height * this
                    val half = (width - newRect.right + newRect.left) / 2f
                    newRect.left -= half
                    newRect.right += half
                }
            }

            TouchArea.BOTTOM -> {
                newRect.bottom += dy
                ratio?.run {
                    val height = newRect.bottom - newRect.top
                    val width = height * this
                    val half = (width - newRect.right + newRect.left) / 2f
                    newRect.left -= half
                    newRect.right += half
                }
            }

            TouchArea.LEFT_TOP -> {
                newRect.left += dx
                newRect.top += dy
                ratio?.run {
                    var width = newRect.right - newRect.left
                    var height = newRect.bottom - newRect.top
                    if (width < height) {
                        height = width / this
                        newRect.top -= height - newRect.bottom + newRect.top
                    } else {
                        width = height * this
                        newRect.left -= width - newRect.right + newRect.left
                    }
                }
            }

            TouchArea.RIGHT_TOP -> {
                newRect.right += dx
                newRect.top += dy
                ratio?.run {
                    var width = newRect.right - newRect.left
                    var height = newRect.bottom - newRect.top
                    if (width < height) {
                        height = width / this
                        newRect.top -= height - newRect.bottom + newRect.top
                    } else {
                        width = height * this
                        newRect.right += width - newRect.right + newRect.left
                    }
                }
            }

            TouchArea.LEFT_BOTTOM -> {
                newRect.left += dx
                newRect.bottom += dy
                ratio?.run {
                    var width = newRect.right - newRect.left
                    var height = newRect.bottom - newRect.top
                    if (width < height) {
                        height = width / this
                        newRect.bottom += height - newRect.bottom + newRect.top
                    } else {
                        width = height * this
                        newRect.left -= width - newRect.right + newRect.left
                    }
                }
            }

            TouchArea.RIGHT_BOTTOM -> {
                newRect.right += dx
                newRect.bottom += dy
                ratio?.run {
                    var width = newRect.right - newRect.left
                    var height = newRect.bottom - newRect.top
                    if (width < height) {
                        height = width / this
                        newRect.bottom += height - newRect.bottom + newRect.top
                    } else {
                        width = height * this
                        newRect.right += width - newRect.right + newRect.left
                    }
                }
            }

            else -> {}
        }

        if (
            (0f <= newRect.left) && (newRect.left < newRect.right) && (newRect.right <= width) &&
            (0f <= newRect.top) && (newRect.top < newRect.bottom) && (newRect.bottom <= height)
        ) {
            cropRect = newRect
        }


        // 限制区域
//        cropRect.left = cropRect.left.coerceAtLeast(0f)
//        cropRect.top = cropRect.top.coerceAtLeast(0f)
//        cropRect.right = cropRect.right.coerceAtMost(width.toFloat())
//        cropRect.bottom = cropRect.bottom.coerceAtMost(height.toFloat())
    }

    private fun getTouchArea(x: Float, y: Float): TouchArea? {
        val r = cropRect

        Log.d("touch", "$r, $x, $y")

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

    fun getCropRect(): RectF {
        return RectF(cropRect)
    }
}
