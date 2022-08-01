package com.example.tasktrackerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.models.Employee
import com.example.tasktrackerapp.models.User
import com.example.tasktrackerapp.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<Button>(R.id.btn_register_from_register).setOnClickListener {
            registerUser()
        }
    }

    private fun validateDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<EditText>(R.id.et_register_name).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter your name",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(findViewById<EditText>(R.id.et_register_surname).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter your surname",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(findViewById<EditText>(R.id.et_register_department).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter your department",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(findViewById<EditText>(R.id.et_register_email).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(findViewById<EditText>(R.id.et_register_password).text.toString().trim() {it <= ' '}) -> {
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

    fun registerUser(){
        if (validateDetails()) {
            showProgressDialog()

            val email: String = findViewById<EditText>(R.id.et_register_email).text.toString().trim() {it <= ' '}
            val password: String = findViewById<EditText>(R.id.et_register_password).text.toString().trim() {it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val type: String = (
                                    if (findViewById<RadioButton>(R.id.rb_register_employee).isChecked) {
                                Constants.EMPLOYEES
                            } else {
                                Constants.DIRECTORS
                            })

                            val user = User(
                                firebaseUser.uid,
                                findViewById<EditText>(R.id.et_register_name).text.toString().trim() {it <= ' '},
                                findViewById<EditText>(R.id.et_register_surname).text.toString().trim() {it <= ' '},
                                email,
                                findViewById<EditText>(R.id.et_register_department).text.toString().trim() {it <= ' '},
                                type,
                                //task
                            )

                            FirestoreClass().registerUser(this, user)
                        } else {
                            hideProgressDialog()
                            Toast.makeText(
                                this,
                                task.exception!!.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
        }
    }

    fun registrationSuccess() {
        hideProgressDialog()

        Toast.makeText(
                this,
                "Registered successfully",
                Toast.LENGTH_SHORT
            ).show()

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.putExtra("registered_email", findViewById<EditText>(R.id.et_register_email).text.toString().trim() {it <= ' '})
        intent.putExtra("registered_password", findViewById<EditText>(R.id.et_register_password).text.toString().trim() {it <= ' '})
        intent.putExtra("registered_type",
            if (findViewById<RadioButton>(R.id.rb_register_employee).isChecked) {
            Constants.EMPLOYEES
        } else {
            Constants.DIRECTORS
        })
        setResult(RESULT_OK, intent)
        startActivity(intent)
        finish()
    }
}