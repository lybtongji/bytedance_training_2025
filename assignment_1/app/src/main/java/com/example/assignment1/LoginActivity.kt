package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var userDatabase: UserDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<EditText>(R.id.login_input_password).setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val editText = view as EditText
                val drawableEnd = editText.compoundDrawablesRelative[2] // 2 表示 end/right

                if (drawableEnd != null) {
                    val drawableEndBounds = drawableEnd.bounds
                    val drawableWidth = drawableEndBounds.width()

                    if (
                        event.rawX >= (editText.right - drawableWidth - editText.paddingEnd) &&
                        event.rawX <= editText.right
                    ) {
                        onPasswordVisibleChange(editText)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        findViewById<Button>(R.id.login_by_wechat).setOnClickListener { view ->
            val btn = view as Button
            Toast.makeText(view.context, btn.text, Toast.LENGTH_SHORT)
                .show()
        }
        findViewById<Button>(R.id.login_by_apple).setOnClickListener { view ->
            val btn = view as Button
            Toast.makeText(view.context, btn.text, Toast.LENGTH_SHORT)
                .show()
        }

        createDatabase()

        findViewById<Button>(R.id.login_btn).setOnClickListener { _ ->
            val username = findViewById<EditText>(R.id.login_input_email).text.toString()
            val password = findViewById<EditText>(R.id.login_input_password).text.toString()
            if (userDatabase.check_user(username, password)) {
                val avatars = arrayOf(
                    R.drawable.my_selected,
                    R.drawable.my_unselected,
                )
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("avatar", avatars.random())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "用户名/密码错误", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onPasswordVisibleChange(view: EditText) {
        if (view.transformationMethod is PasswordTransformationMethod) {
            view.transformationMethod = HideReturnsTransformationMethod.getInstance()
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_login_password,
                0,
                R.drawable.ic_login_password_show,
                0,
            )
        } else {
            view.transformationMethod = PasswordTransformationMethod.getInstance()
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_login_password,
                0,
                R.drawable.ic_login_password_hide,
                0,
            )
        }
        view.setSelection(view.text?.length ?: 0)
    }

    private fun createDatabase() {
        userDatabase = UserDatabase(this, null, null, 1)
        userDatabase.add_user("admin", "admin")
        userDatabase.add_user("user", "123456")
    }
}