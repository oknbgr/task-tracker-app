package com.example.tasktrackerapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.utils.Constants

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (intent.hasExtra("registered_email")) {
            val mail = intent.getStringExtra("registered_email")
            val password = intent.getStringExtra("registered_password")
            val type = intent.getStringExtra("registered_type")

            findViewById<EditText>(R.id.et_login_email).setText(mail)
            findViewById<EditText>(R.id.et_login_password).setText(password)

            if (type == Constants.DIRECTORS) {
                findViewById<RadioGroup>(R.id.rg_login_user_type).check(R.id.rb_login_director)
            } else if (type == Constants.EMPLOYEES) {
                findViewById<RadioGroup>(R.id.rg_login_user_type).check(R.id.rb_login_employee)
            }
        }

        findViewById<Button>(R.id.btn_register_from_login).setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            if (validateLogin()) {
                if (findViewById<RadioButton>(R.id.rb_login_director).isChecked) {
                    val intent = Intent(this@LoginActivity, DirectorActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@LoginActivity, EmployeeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun validateLogin(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<EditText>(R.id.et_login_email).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(findViewById<EditText>(R.id.et_login_password).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter your password",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> {
                true
            }
        }
    }
}