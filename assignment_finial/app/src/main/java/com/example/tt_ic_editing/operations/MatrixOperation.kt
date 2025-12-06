package com.example.tt_ic_editing.operations

import android.graphics.Bitmap
import android.graphics.Matrix
import com.example.tt_ic_editing.interfaces.Operation

class MatrixOperation(private val matrix: Matrix) : Operation<Bitmap> {
    override fun apply(target: Bitmap): Bitmap {
        return Bitmap.createBitmap(
            target,
            0,
            0,
            target.width,
            target.height,
            matrix,
            true,
        )
    }
}