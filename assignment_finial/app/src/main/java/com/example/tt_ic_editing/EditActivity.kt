package com.example.tt_ic_editing

import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EditActivity : AppCompatActivity() {
    private val operationViewModel: OperationViewModel<Bitmap> by viewModels()
    private val scaledViewModel: ScaledBitmapViewModal by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        intent.data?.let { scaledViewModel.uri = it } ?: run {
//            Toast.makeText(this, "图片路径为空", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }

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

        val thumbImage = findViewById<ImageView>(R.id.edit_image)
        thumbImage.post {
            intent.data?.let {
                if (it == scaledViewModel.uri) {
                    scaledViewModel.bitmap.getImage()?.let { im ->
                        thumbImage.setImageBitmap(operationViewModel.sequence.execute(im))
                    } ?: run {
                        Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    scaledViewModel.uri = it
                    scaledViewModel.bitmap.load(
                        this,
                        it,
                        thumbImage.width,
                        thumbImage.height,
                    )?.let {
                        thumbImage.setImageBitmap(operationViewModel.sequence.execute(it))
                    } ?: run {
                        Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
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

        val subView = findViewById<RecyclerView>(R.id.edit_sub_view)
        subView.layoutManager = LinearLayoutManager(subView.context)
        (subView.layoutManager as? LinearLayoutManager)?.orientation =
            when (getResources().configuration.orientation) {
                Configuration.ORIENTATION_UNDEFINED -> LinearLayoutManager.HORIZONTAL
                Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager.HORIZONTAL
                Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager.VERTICAL
                else -> LinearLayoutManager.HORIZONTAL
            }

        mainView.adapter = EditSelectAdapter(subView)
    }
}