package com.example.tt_ic_editing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
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

    private var currentBrightness = 0f
    private var currentContrast = 1f

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

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        bm?.run {
            val dx = (this@PreviewImageView.width - width) / 2f
            val dy = (this@PreviewImageView.height - height) / 2f

            currentScale = 1f

            imageMatrixInternal.reset()
            imageMatrixInternal.postTranslate(dx, dy)
            imageMatrix = imageMatrixInternal
        }
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
        val rect = RectF(
            0f,
            0f,
            drawable.intrinsicWidth.toFloat(),
            drawable.intrinsicHeight.toFloat(),
        )
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
            val targetScale = if (currentScale < maxScale) maxScale else 1f
            val factor = targetScale / currentScale

            imageMatrixInternal.postScale(factor, factor, e.x, e.y)
            imageMatrix = imageMatrixInternal
            currentScale = targetScale
            fixTranslate()

            return true
        }
    }

    //    fun Crop(rectF: RectF): RectF? {
//        val inverseMatrix = Matrix()
//        imageMatrixInternal.invert(inverseMatrix)
//        inverseMatrix.mapRect(rectF)
//
//        val m = FloatArray(9)
//        imageMatrixInternal.getValues(m)
//        val rotation = (atan2(m[Matrix.MSKEW_X], m[Matrix.MSCALE_X]) * 180f / Math.PI).toFloat()
//
//        val originalBitmap = (drawable as BitmapDrawable).bitmap
//
//        val left = rectF.left.coerceIn(0f, originalBitmap.width.toFloat())
//        val top = rectF.top.coerceIn(0f, originalBitmap.height.toFloat())
//
//        val right = rectF.right.coerceIn(0f, originalBitmap.width.toFloat())
//        val bottom = rectF.bottom.coerceIn(0f, originalBitmap.height.toFloat())
//
//        if (left < right && top < bottom) {
////            val newMatrix = Matrix().apply {
////                postTranslate(-left, -top)
////                postConcat(imageMatrixInternal)
////                postTranslate(left, top)
////            }
//
//            val originalWidth = originalBitmap.width
//            val originalHeight = originalBitmap.height
//
//            val width = right - left
//            val height = bottom - top
//
//            val cropped = Bitmap.createBitmap(
//                originalBitmap,
//                left.toInt(),
//                top.toInt(),
//                width.toInt(),
//                height.toInt(),
//            )
//
//            val scale = currentScale
//
//            setImageBitmap(cropped)
//
//            currentScale = scale
//
//            val dx = width * scale / 2f
//            val dy = height * scale / 2f
//
//            imageMatrixInternal.apply {
//                postTranslate(-dx, -dy)
//                postScale(scale, scale)
//                postRotate(rotation)
//                postTranslate(dx, dy)
//            }
//
////            imageMatrixInternal.set(newMatrix)
//            imageMatrix = imageMatrixInternal
//
//            return RectF(
//                left / originalWidth,
//                top / originalHeight,
//                right / originalWidth,
//                bottom / originalHeight,
//            )
//
////            MatrixOperation({ _ -> imageMatrixInternal }).apply(originalBitmap)
//
////            return normalizedRect
//        }
//
//        return null
//    }
//
    fun Crop(rectF: RectF): RectF? {
        val inverseMatrix = Matrix()
        imageMatrixInternal.invert(inverseMatrix)
        inverseMatrix.mapRect(rectF)

        val originalBitmap = (drawable as BitmapDrawable).bitmap

        val left = rectF.left.coerceIn(0f, originalBitmap.width.toFloat())
        val top = rectF.top.coerceIn(0f, originalBitmap.height.toFloat())

        val right = rectF.right.coerceIn(0f, originalBitmap.width.toFloat())
        val bottom = rectF.bottom.coerceIn(0f, originalBitmap.height.toFloat())

        if (left < right && top < bottom) {
            val originalWidth = originalBitmap.width
            val originalHeight = originalBitmap.height

            val cropped = Bitmap.createBitmap(
                originalBitmap,
                left.toInt(),
                top.toInt(),
                (right - left).toInt(),
                (bottom - top).toInt(),
            )

            setImageBitmap(cropped)

            return RectF(
                left / originalWidth,
                top / originalHeight,
                right / originalWidth,
                bottom / originalHeight,
            )
        }

        return null
    }

    fun getImageMatrixInternal(): Matrix {
        return imageMatrixInternal
    }

    fun setImageMatrixInternal(value: Matrix) {
        imageMatrixInternal.set(value)
        imageMatrix = imageMatrixInternal
    }

    fun applyMatrix(getMatrix: ((Bitmap) -> Matrix)) {
        val originalBitmap = (drawable as BitmapDrawable).bitmap
//        val cx = originalBitmap.width / 2f
//        val cy = originalBitmap.height / 2f

        val matrix = getMatrix(originalBitmap)
//
//        val pts = floatArrayOf(cx, cy)
//        imageMatrixInternal.mapPoints(pts)

//        Log.d("apply", "${pts[0]}, ${pts[1]}")

//        imageMatrixInternal.apply {
//            postTranslate(-pts[0], -pts[1])
//            postConcat(matrix)
//            postTranslate(pts[0], pts[1])
//        }

        imageMatrixInternal.postConcat(matrix)
        imageMatrix = imageMatrixInternal
    }

    fun updateTone(brightness: Float? = null, contrast: Float? = null) {
        brightness?.let { currentBrightness = it.coerceIn(-100f, 100f) }
        contrast?.let { currentContrast = ((it + 50f) / 100f).coerceIn(0f, 2.5f) }

        val cm = ColorMatrix()

        val scale = currentContrast
        val translate = (-0.5f * scale + 0.5f) * 255f
        cm.set(floatArrayOf(
            scale, 0f, 0f, 0f, translate + currentBrightness * 255 / 200,
            0f, scale, 0f, 0f, translate + currentBrightness * 255 / 200,
            0f, 0f, scale, 0f, translate + currentBrightness * 255 / 200,
            0f, 0f, 0f, 1f, 0f
        ))

        colorFilter = ColorMatrixColorFilter(cm)
        invalidate()
    }
}