package com.example.tt_ic_editing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

class ScaledBitmap: NormalBitmap() {
    fun load(context: Context, uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        return try {
            // 第一次解码：只获取图片尺寸，不加载图片
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true  // 只获取边界信息，不分配像素内存
            }

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            // 计算合适的采样率
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

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

    /**
     * 计算采样率 inSampleSize
     * @param options 包含原始图片尺寸的 Options
     * @param reqWidth 目标宽度
     * @param reqHeight 目标高度
     * @return 2的幂次方的采样率（1, 2, 4, 8...）
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // 原始图片的宽高
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        // 如果原始尺寸大于目标尺寸，计算采样率
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // 计算最大的 inSampleSize 值，该值为2的幂
            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}