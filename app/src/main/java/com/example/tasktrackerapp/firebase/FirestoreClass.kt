package com.example.tasktrackerapp.firebase

import android.app.Activity
import android.util.Log
import com.example.tasktrackerapp.models.Employee
import com.example.tasktrackerapp.models.Task
import com.example.tasktrackerapp.models.User
import com.example.tasktrackerapp.ui.activities.AddNewTaskActivity
import com.example.tasktrackerapp.ui.activities.DirectorActivity
import com.example.tasktrackerapp.ui.activities.EmployeeActivity
import com.example.tasktrackerapp.ui.activities.RegisterActivity
import com.example.tasktrackerapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        //users are stored under the name of "users" collection in firestore
        mFireStore.collection(Constants.USERS)
            .document(userInfo.user_id) //set user's unique id as document id
            //pull all info entered by user, merge them together, send it firestore
            .set(userInfo, SetOptions.merge())
            //if registration is successful
            .addOnSuccessListener {
                //call basic activity and transfer results
                activity.registrationSuccess()
            }
            //if registration is not successful
            .addOnFailureListener { e ->
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName,
                    "registerUser: Error occurred.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String{
        //creating a instance of current user
        val currentUser = FirebaseAuth.getInstance().currentUser
        //variable for storing user id
        var currentUserID = ""

        if (currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun addNewTask(activity: AddNewTaskActivity, taskInfo: Task){
        mFireStore.collection(Constants.TASKS)
            .document()
            .set(taskInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addNewTaskSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "addNewTask: Error in adding new task",
                    e
                )
            }
    }

    fun getTasksList(activity: Activity) {
        when (activity) {
            is EmployeeActivity -> {
                mFireStore.collection(Constants.TASKS)
                    .whereEqualTo(Constants.USER_ID, getCurrentUserID())
                    .get()
                    .addOnSuccessListener { document ->
                        val list: ArrayList<Task> = ArrayList()

                        for(item in document.documents) {
                            val task = item.toObject(Task::class.java)
                            task!!.task_id = item.id
                            list.add(task)
                        }

                        activity.successEmployeeTasksListFromFireStore(list)
                    }
            }
            is DirectorActivity -> {
                mFireStore.collection(Constants.TASKS)
                    .get()
                    .addOnSuccessListener { document ->
                        val list: ArrayList<Task> = ArrayList()

                        for(item in document.documents) {
                            val task = item.toObject(Task::class.java)
                            task!!.task_id = item.id
                            list.add(task)
                        }

                        activity.successDirectorTasksListFromFireStore(list)
                    }
            }
        }
    }
}