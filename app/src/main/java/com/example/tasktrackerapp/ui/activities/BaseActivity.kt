package com.example.tasktrackerapp.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import com.example.tasktrackerapp.R

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false

    private lateinit var mProgressDialog: Dialog

    fun showProgressDialog(){
        mProgressDialog = Dialog(this)

        //set screen content from a layout resource, which will inflate to top-level view of the screen
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            "Press back button again to exit",
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {doubleBackToExitPressedOnce = false},
            2000
        ) //if users presses back button 2 secs later,
    }  //it will not be count as double pressed
}