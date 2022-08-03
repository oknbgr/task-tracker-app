package com.example.tasktrackerapp.ui.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.notifications.AlarmManager
import com.example.tasktrackerapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    // default checked radio button
    private var checkedRadioButton: String = Constants.EMPLOYEES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // schedules notifications to be sent at 14:00 and 16:00
        createNotificationChannels()
        AlarmManager.startReminder(this, 1, "14:00")
        AlarmManager.startReminder(this, 2, "16:00")

        // if user has just registered to the system,
        // carry info from register activity to here
        checkIfRegistered()

        // if selected user type is switched
        findViewById<RadioGroup>(R.id.rg_login_user_type).setOnCheckedChangeListener { radioGroup, checkedID ->
            checkedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
        }

        // if user wants to register to the system
        findViewById<Button>(R.id.btn_register_from_login).setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            loginUser()
        }
    }

    private fun createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATIONS_CHANNEL_ID,
                "Task Tracker",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = "Notifies employees twice a day."

            ContextCompat.getSystemService(this, NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun checkIfRegistered() {
        if (intent.hasExtra(Constants.REGISTERED_EMAIL)) {
            val mail = intent.getStringExtra(Constants.REGISTERED_EMAIL)
            val password = intent.getStringExtra(Constants.REGISTERED_PASSWORD)
            val type = intent.getStringExtra(Constants.REGISTERED_TYPE)

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
    }

    // after successful login response came from firebase,
    // last step for checking if user selected the correct user type
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

    // checking login info from firebase
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

    // checking if user filled all edit text fields
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