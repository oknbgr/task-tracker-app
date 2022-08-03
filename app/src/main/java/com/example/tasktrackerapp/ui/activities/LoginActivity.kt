package com.example.tasktrackerapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    // default checked radio button
    private var checkedRadioButton: String = Constants.EMPLOYEES

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
                checkedRadioButton = Constants.DIRECTORS
            } else if (type == Constants.EMPLOYEES) {
                findViewById<RadioGroup>(R.id.rg_login_user_type).check(R.id.rb_login_employee)
                checkedRadioButton = Constants.EMPLOYEES
            }
        }

        findViewById<RadioGroup>(R.id.rg_login_user_type).setOnCheckedChangeListener { radioGroup, checkedID ->
            checkedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
        }

        findViewById<Button>(R.id.btn_register_from_login).setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            loginUser()
        }
    }

    fun startIntentWithUserType(type: String){
        if (type != checkedRadioButton) {
            Toast.makeText(
                this,
                "This user is not one of $checkedRadioButton",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (type == Constants.DIRECTORS) {
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

    private fun loginUser() {
        if (validateLogin()) {
            showProgressDialog()

            val email = findViewById<EditText>(R.id.et_login_email).text.toString().trim() {it <= ' '}
            val password = findViewById<EditText>(R.id.et_login_password).text.toString().trim() {it <= ' '}

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideProgressDialog()
                        FirestoreClass().getCurrentUserType(this)
                    } else {
                        hideProgressDialog()
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
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