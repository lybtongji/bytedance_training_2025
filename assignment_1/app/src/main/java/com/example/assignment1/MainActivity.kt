package com.example.assignment1

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MainActivity : Activity() {
    private lateinit var settingItems: List<SettingItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createSettingItems()
        val mView = findViewById<RecyclerView>(R.id.setting_view)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        mView.setLayoutManager(linearLayoutManager)
        val adapter = SettingAdapter(settingItems)
        mView.setAdapter(adapter)
        val avatar = intent.extras?.getInt("avatar")
        if (avatar != null) {
            findViewById<ShapeableImageView>(R.id.avatar).setImageResource(avatar)
        }
        val username = intent.extras?.getString("username")
        if (username != null) {
            findViewById<TextView>(R.id.username_text).text = username
        }
        setDescription()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setDescription() {
        val userPrefs = getSharedPreferences("user_settings", MODE_PRIVATE)
        val description = userPrefs.getString("description", "欢迎来到信息App")
        findViewById<TextView>(R.id.hello_msg).text = description
    }

    private fun createSettingItems() {
        val data = arrayOf(
            Pair("个人信息", R.drawable.icon_1),
            Pair("我的收藏", R.drawable.icon_2),
            Pair("浏览历史", R.drawable.icon_3),
            Pair("设置", R.drawable.icon_4),
            Pair("关于我们", R.drawable.icon_5),
            Pair("意见反馈", R.drawable.icon_6),
        )
        settingItems = data.map { (title, resId) -> SettingItem(title, resId) }
    }
}