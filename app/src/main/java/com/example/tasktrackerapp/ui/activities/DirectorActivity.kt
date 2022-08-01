package com.example.tasktrackerapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.firebase.FirestoreClass
import com.example.tasktrackerapp.models.Task
import com.example.tasktrackerapp.ui.adapters.TasksListAdapter
import kotlinx.android.synthetic.main.activity_director.*
import kotlinx.android.synthetic.main.activity_employee.*

class DirectorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director)

        getTasks()

        findViewById<Button>(R.id.btn_director_refresh).setOnClickListener {
            getTasks()
            Toast.makeText(
                this,
                "List refreshed",
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<Button>(R.id.btn_director_logout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getTasks()
    }

    private fun getTasks() {
        showProgressDialog()
        FirestoreClass().getTasksList(this@DirectorActivity)
        hideProgressDialog()
    }

    fun successDirectorTasksListFromFireStore(list: ArrayList<Task>){
        hideProgressDialog()

        if (list.size > 0){
            rv_director_tasks.layoutManager = LinearLayoutManager(this@DirectorActivity)
            rv_director_tasks.setHasFixedSize(true)

            val adapter = TasksListAdapter(this, list)
            rv_director_tasks.adapter = adapter
        }
    }
}