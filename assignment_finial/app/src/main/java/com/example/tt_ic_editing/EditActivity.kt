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