package com.example.tasktrackerapp.firebase

import android.util.Log
import com.example.tasktrackerapp.ui.activities.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        //users are stored under the name of "users" collection in firestore
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id) //set user's unique id as document id
            //pull all info entered by user, merge them together, send it firestore
            .set(userInfo, SetOptions.merge())
            //if registration is successful
            .addOnSuccessListener {
                //call basic activity and transfer results
                activity.userRegistrationSuccess()
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
}