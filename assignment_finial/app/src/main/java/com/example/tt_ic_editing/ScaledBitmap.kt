package com.example.tt_ic_editing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface

class ScaledBitmap : NormalBitmap() {
    fun load(context: Context, uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            // 第一次解码：只获取图片尺寸，不加载图片
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true  // 只获取边界信息，不分配像素内存
            }

            val orientation = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
                ExifInterface(inputStream).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            }

            val rawWidth: Int
            val rawHeight: Int
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                orientation == ExifInterface.ORIENTATION_ROTATE_270
            ) {
                rawWidth = options.outHeight
                rawHeight = options.outWidth
            } else {
                rawWidth = options.outWidth
                rawHeight = options.outHeight
            }

            Log.d(
                "App",
                "outWidth: ${options.outWidth}, " +
                        "outHeight: ${options.outHeight}"
            )
            Log.d(
                "App",
                "rawWidth: $rawWidth " +
                        "rawHeight: $rawHeight"
            )

            // 计算合适的采样率
            options.inSampleSize = calculateInSampleSize(rawWidth, rawHeight, reqWidth, reqHeight)

            Log.d("App", "sample: ${options.inSampleSize}")
            Log.d(
                "App",
                "width: ${reqWidth / options.inSampleSize}, " +
                        "height: ${reqHeight / options.inSampleSize}"
            )

            // 第二次解码：按采样率加载缩小后的图片
            options.inJustDecodeBounds = false

            _image = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            _image
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun calculateInSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1

        val reqWidth = reqWidth * 2
        val reqHeight = reqHeight * 2

        while (width / inSampleSize > reqWidth || height / inSampleSize > reqHeight) {
            inSampleSize *= 2
        }

        return inSampleSize
    }
}