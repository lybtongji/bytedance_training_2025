package com.example.tt_ic_editing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

open class NormalBitmap {
    protected open var _image: Bitmap? = null

    fun getImage(): Bitmap? = _image

    open fun load(context: Context, uri: Uri): Bitmap? {
        return try {
            _image = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, null)
            }
            _image
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}