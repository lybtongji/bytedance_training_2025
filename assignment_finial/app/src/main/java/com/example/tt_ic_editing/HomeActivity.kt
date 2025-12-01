package com.example.tt_ic_editing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File


class HomeActivity : AppCompatActivity() {
    private val photoFilename = "photo.jpg"
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.entry_camera).setOnClickListener { _ ->
            val photoFile = File(this.getExternalFilesDir(null), photoFilename)
            photoUri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                photoFile
            )
            photoUri?.let { uri ->
                takePhotoLauncher.launch(uri)
            }
        }
        findViewById<Button>(R.id.entry_photo).setOnClickListener { _ ->
            pickImageLauncher.launch("image/*")
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val intent = Intent(this, EditActivity::class.java).apply {
                data = it
            }
            startActivity(intent)
        }
    }
    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            if (photoUri == null) {
                Toast.makeText(this, "无法获取照片", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, EditActivity::class.java).apply {
                    data = photoUri
                }
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT).show()
        }
    }
}