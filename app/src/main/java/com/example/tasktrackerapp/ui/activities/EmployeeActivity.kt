package com.example.tasktrackerapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.models.Task
import com.example.tasktrackerapp.ui.adapters.TasksListAdapter
import kotlinx.android.synthetic.main.activity_employee.*

class EmployeeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        // populate recyclerview with items
        getTasks()

        findViewById<Button>(R.id.btn_employee_add_new_task).setOnClickListener {
            val intent = Intent(this, AddNewTaskActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_employee_log_out).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getTasks()
    }

    // gets tasks from firestore
    private fun getTasks() {
        showProgressDialog()
        FirestoreClass().getTasksList(this@EmployeeActivity)
        hideProgressDialog()
    }

    // when items are successfully received
    fun successEmployeeTasksListFromFireStore(list: ArrayList<Task>){
        hideProgressDialog()

        if (list.size > 0){
            rv_employee_tasks.layoutManager = LinearLayoutManager(this@EmployeeActivity)
            rv_employee_tasks.setHasFixedSize(true)

            val adapter = TasksListAdapter(this, list)
            rv_employee_tasks.adapter = adapter
        }
    }
}