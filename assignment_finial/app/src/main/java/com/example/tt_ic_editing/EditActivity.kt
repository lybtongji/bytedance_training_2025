package com.example.tt_ic_editing

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EditActivity : AppCompatActivity() {
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
        findViewById<ImageView>(R.id.edit_image).setImageURI(intent.data)
        findViewById<Button>(R.id.edit_return_btn).setOnClickListener { _ ->
            this.finish()
        }

        val rView = findViewById<RecyclerView>(R.id.edit_sub_view)
        val linearLayoutManager = LinearLayoutManager(this)
        rView.setLayoutManager(linearLayoutManager)
        val orientation = when (getResources().configuration.orientation) {
            Configuration.ORIENTATION_UNDEFINED -> LinearLayoutManager.HORIZONTAL
            Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager.HORIZONTAL
            Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager.VERTICAL
            else -> LinearLayoutManager.HORIZONTAL
        }
        linearLayoutManager.setOrientation(orientation)
        findViewById<Button>(R.id.edit_cut).setOnClickListener { _ ->
            rView.setAdapter(EditCutAdapter())
        }
        findViewById<Button>(R.id.edit_rotate).setOnClickListener { _ ->
            rView.setAdapter(EditRotateAdapter())
        }
        findViewById<Button>(R.id.edit_light).setOnClickListener { _ ->
            rView.setAdapter(null)
        }
        findViewById<Button>(R.id.edit_text).setOnClickListener { _ ->
            rView.setAdapter(null)
        }
        findViewById<Button>(R.id.edit_text_style).setOnClickListener { _ ->
            rView.setAdapter(null)
        }
    }
}