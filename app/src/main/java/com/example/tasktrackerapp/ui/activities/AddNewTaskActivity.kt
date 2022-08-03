package com.example.tasktrackerapp.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.models.Task
import java.util.*

class AddNewTaskActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        FirestoreClass().getTasksList(this)

        findViewById<Button>(R.id.btn_add_add_new_task).setOnClickListener {
            uploadTask()
        }

        findViewById<Button>(R.id.btn_open_date_time_picker).setOnClickListener {
            pickDateTime()
        }
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                // do something here

                val convertedToString = "$day/$month/$year at $hour:$minute"

                findViewById<EditText>(R.id.et_add_task_time).setText(convertedToString)

            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun validateTask(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<EditText>(R.id.et_add_task_title).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter task title",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            TextUtils.isEmpty(findViewById<EditText>(R.id.et_add_task_description).text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this,
                    "Please enter task description",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> {
                true
            }
        }
    }

    fun userInfoReceived(id: String, info: String) {
        val task = Task(
            id,
            findViewById<EditText>(R.id.et_add_task_title).text.toString(),
            findViewById<EditText>(R.id.et_add_task_description).text.toString(),
            findViewById<EditText>(R.id.et_add_task_time).text.toString(),
            info
        )

        FirestoreClass().addNewTask(this, task)
    }

    private fun uploadTask() {
        if (validateTask()) {
            val userID = FirestoreClass().getCurrentUserID()

            FirestoreClass().getUserInfoFromUserID(userID, this)
        }
    }

    fun addNewTaskSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this,
            "Task added successfully",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}