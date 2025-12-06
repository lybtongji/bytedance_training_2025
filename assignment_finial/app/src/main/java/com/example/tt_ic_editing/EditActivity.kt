package com.example.tt_ic_editing

import android.content.ContentValues
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_ic_editing.interfaces.OnEditSelectedListener
import com.example.tt_ic_editing.operations.CropOperation
import com.example.tt_ic_editing.recycler_adapter.EditCropAdapter
import com.example.tt_ic_editing.recycler_adapter.EditSelectAdapter
import com.example.tt_ic_editing.viewmodel.EditViewModel
import com.example.tt_ic_editing.viewmodel.OperationViewModel
import com.example.tt_ic_editing.viewmodel.ScaledBitmapViewModal

class EditActivity : AppCompatActivity() {
    private val editSelectViewModel: EditViewModel by viewModels()
    private val operationViewModel: OperationViewModel<Bitmap> by viewModels()
    private val scaledViewModel: ScaledBitmapViewModal by viewModels()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("selectedPosition", editSelectViewModel.editSelectAdapter.selectedPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        editSelectViewModel.editSelectAdapter.selectedPosition =
            savedInstanceState.getInt("selectedPosition", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.edit_title_text).text = intent.data.toString()
//        findViewById<ImageView>(R.id.edit_image).setImageURI(intent.data)
        findViewById<Button>(R.id.edit_return_btn).setOnClickListener { _ ->
            this.finish()
        }
        findViewById<Button>(R.id.edit_save_btn).setOnClickListener { _ ->
            val filename = "IMG_${System.currentTimeMillis()}.jpg"
            val mimeType = "image/jpeg"
            val compressFormat = Bitmap.CompressFormat.JPEG
            val quality = 90

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            val resolver = this.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let { outUri ->
                scaledViewModel.uri?.let { inUri ->
                    scaledViewModel.bitmap.load(this, inUri)?.let { bitmap ->
                        try {
                            resolver.openOutputStream(outUri)?.use { outStream ->
                                operationViewModel.sequence.execute(bitmap)
                                    .compress(compressFormat, quality, outStream)
                            }
                            contentValues.clear()
                            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                            resolver.update(uri, contentValues, null, null)
                            Toast.makeText(this, "保存成功到相册", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(this, "文件保存失败", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    } ?: run {
                        Toast.makeText(this, "无法打开输出文件", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "图片文件路径丢失", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "无法获取保存路径", Toast.LENGTH_SHORT).show()
            }
        }

        val thumbImage = findViewById<PreviewImageView>(R.id.edit_image)
        thumbImage.post {
            intent.data?.let { uri ->
                if (uri == scaledViewModel.uri) {
                    scaledViewModel.bitmap.getImage()?.let { im ->
                        thumbImage.setImageBitmap(im)
                    } ?: run {
                        Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    scaledViewModel.uri = uri
                    scaledViewModel.bitmap.load(
                        this,
                        uri,
                        thumbImage.width,
                        thumbImage.height,
                    )?.let { im ->
                        thumbImage.setImageBitmap(im)
                    } ?: run {
                        Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } ?: run {
                Toast.makeText(this, "图片路径为空", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val mainView = findViewById<RecyclerView>(R.id.edit_main_view)
        mainView.layoutManager = LinearLayoutManager(mainView.context)
        (mainView.layoutManager as? LinearLayoutManager)?.orientation =
            when (getResources().configuration.orientation) {
                Configuration.ORIENTATION_UNDEFINED -> LinearLayoutManager.HORIZONTAL
                Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager.HORIZONTAL
                Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager.VERTICAL
                else -> LinearLayoutManager.HORIZONTAL
            }
        editSelectViewModel.editSelectAdapter.getRootView = { findViewById(R.id.main) }

        val subView = findViewById<RecyclerView>(R.id.edit_sub_view)
        subView.layoutManager = LinearLayoutManager(subView.context)
        (subView.layoutManager as? LinearLayoutManager)?.orientation =
            when (getResources().configuration.orientation) {
                Configuration.ORIENTATION_UNDEFINED -> LinearLayoutManager.HORIZONTAL
                Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager.HORIZONTAL
                Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager.VERTICAL
                else -> LinearLayoutManager.HORIZONTAL
            }

//        mainView.adapter = EditSelectAdapter(subView)
        mainView.adapter = editSelectViewModel.editSelectAdapter
        (mainView.adapter as EditSelectAdapter).onEditSelectedListener =
            object : OnEditSelectedListener {
                override fun onItemSelected(position: Int) {
                    subView.adapter = (mainView.adapter as EditSelectAdapter).adapter
                    val cropVisibility =
                        when (subView.adapter) {
                            is EditCropAdapter -> View.VISIBLE
                            else -> View.GONE
                        }
                    setCropViewVisibility(cropVisibility)
                }
            }
//        subView.adapter = (mainView.adapter as EditSelectAdapter).adapter
        findViewById<View>(R.id.btn_crop_done).setOnClickListener { _ ->
            val cropRect = findViewById<CropOverlayView>(R.id.crop_overlay).getCropRect()
            thumbImage.Crop(cropRect)?.let {
                operationViewModel.sequence.add(
                    CropOperation(it)
                )
            }
            setCropViewVisibility(View.GONE)
        }
    }

    private fun setCropViewVisibility(visibility: Int) {
        findViewById<View>(R.id.crop_overlay).visibility = visibility
        findViewById<View>(R.id.btn_crop_done).visibility = visibility
//        findViewById<View>(R.id.btn_crop_cancel).visibility = visibility
    }
}