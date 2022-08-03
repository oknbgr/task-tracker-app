package com.example.tasktrackerapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// gets triggered if phone reboots
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            AlarmManager.startReminder(context, 1, "14:00")
            AlarmManager.startReminder(context, 2, "16:00")
        }
    }
}