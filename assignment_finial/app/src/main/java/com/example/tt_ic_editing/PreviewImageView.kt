package com.example.tt_ic_editing

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.graphics.Matrix
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector

class PreviewImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    //    private val matrixValues = FloatArray(9)
    private var scaleGestureDetector: ScaleGestureDetector
    private var gestureDetector: GestureDetector
    private val imageMatrixInternal = Matrix()

    private var currentScale = 1f
    private val minScale = 0.5f
    private val maxScale = 2f

    private var lastX = 0f
    private var lastY = 0f
    private var isDragging = false

    init {
        scaleType = ScaleType.MATRIX
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        if (event.pointerCount == 1) {
            handleDrag(event)
        }

        return true
    }

    // 处理拖动
    private fun handleDrag(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                isDragging = true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    val dx = event.x - lastX
                    val dy = event.y - lastY

                    imageMatrixInternal.postTranslate(dx, dy)
                    fixTranslate()

                    imageMatrix = imageMatrixInternal
                    lastX = event.x
                    lastY = event.y
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false
            }
        }
    }

    // 边界修正
    private fun fixTranslate() {
        val rect = getMatrixRectF()
        var dx = 0f
        var dy = 0f

        if (rect.left >= width / 2) dx = width / 2 - rect.left
        if (rect.right <= width / 2) dx = width / 2 - rect.right
        if (rect.top >= height / 2) dy = height / 2 - rect.top
        if (rect.bottom <= height / 2) dy = height / 2 - rect.bottom

        if (rect.right - rect.left <= width) dx = (width - rect.left - rect.right) / 2
        if (rect.bottom - rect.top <= height) dy = (height - rect.top - rect.bottom) / 2

        imageMatrixInternal.postTranslate(dx, dy)
    }

    // 获取当前图片变换后的 Rect
    private fun getMatrixRectF(): RectF {
        val drawable = drawable ?: return RectF()
        val rect =
            RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        val matrix = imageMatrixInternal
        matrix.mapRect(rect)
        return rect
    }

    // 缩放监听
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor

            val newScale = currentScale * scaleFactor
            if (newScale !in minScale..maxScale) return false

            imageMatrixInternal.postScale(
                scaleFactor,
                scaleFactor,
                detector.focusX,
                detector.focusY
            )
            imageMatrix = imageMatrixInternal

            currentScale = newScale
            fixTranslate()

            return true
        }
    }

    // 双击放大
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            val targetScale = if (currentScale < 2f) 2f else 1f
            val factor = targetScale / currentScale

            imageMatrixInternal.postScale(factor, factor, e.x, e.y)
            imageMatrix = imageMatrixInternal
            currentScale = targetScale
            fixTranslate()

            return true
        }
    }
}