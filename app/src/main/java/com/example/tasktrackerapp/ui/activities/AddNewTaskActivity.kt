package com.example.tasktrackerapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.models.Task

class AddNewTaskActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        FirestoreClass().getTasksList(this)

        findViewById<Button>(R.id.btn_add_add_new_task).setOnClickListener {
            uploadTask()
        }
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

    private fun uploadTask() {
        if (validateTask()) {
            val task = Task(
                FirestoreClass().getCurrentUserID(),
                findViewById<EditText>(R.id.et_add_task_title).text.toString(),
                findViewById<EditText>(R.id.et_add_task_description).text.toString(),
                findViewById<EditText>(R.id.et_add_task_time).text.toString()
            )

            FirestoreClass().addNewTask(this, task)
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