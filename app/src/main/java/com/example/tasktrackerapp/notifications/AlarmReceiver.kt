package com.example.tasktrackerapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// gets triggered when scheduled time is reached
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AlarmService(context).sendNotification()
    }
}