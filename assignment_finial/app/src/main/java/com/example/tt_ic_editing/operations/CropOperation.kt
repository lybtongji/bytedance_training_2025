package com.example.tt_ic_editing.operations

import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import com.example.tt_ic_editing.interfaces.Operation

class CropOperation(
    private val normalizedRect: RectF,
) : Operation<Bitmap> {
    override fun apply(target: Bitmap): Bitmap {
        val left = (normalizedRect.left * target.width).toInt()
        val top = (normalizedRect.top * target.height).toInt()
        val width = ((normalizedRect.right - normalizedRect.left) * target.width).toInt()
        val height = ((normalizedRect.bottom - normalizedRect.top) * target.height).toInt()

        return Bitmap.createBitmap(target, left, top, width, height)
    }
}