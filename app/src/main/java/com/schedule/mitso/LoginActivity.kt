package com.schedule.mitso

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : AppCompatActivity() {

    lateinit var loginBtn: Button
    lateinit var skipBtn: Button
    lateinit var loginInput: EditText
    lateinit var passInput: EditText
    lateinit var loginLayout: TextInputLayout
    lateinit var passLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginBtn = findViewById(R.id.login_button)
        skipBtn = findViewById(R.id.skip_button)
        loginInput = findViewById(R.id.login_input)
        passInput = findViewById(R.id.pass_input)
        loginLayout = findViewById(R.id.login_layout)
        passLayout = findViewById(R.id.pass_layout)

        loginBtn.setOnClickListener {
            loginLayout.error = null
            passLayout.error = null
            val login = loginInput.text.toString()
            val pass = passInput.text.toString()
            if (login.isEmpty()){
                loginLayout.error = "Это поле не может быть пустым"
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                passLayout.error = "Это поле не может быть пустым"
                return@setOnClickListener
            }
            doAsync {
                val auth = Parser.auth(login, pass)
                uiThread {
                    val toast = if (auth) {
                        val intent = Intent(this@LoginActivity, PagesActivity::class.java)
                        startActivity(intent)
                    } else {
                        loginLayout.error = "Ошибка авторизации"
                        passLayout.error = ("Ошибка авторизации")
                    }
                }
            }
        }

        skipBtn.setOnClickListener{
            val intent = Intent(this@LoginActivity, PagesActivity::class.java)
            startActivity(intent)
        }
    }
}
