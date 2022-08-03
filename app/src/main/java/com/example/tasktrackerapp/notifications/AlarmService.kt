package com.example.tasktrackerapp.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.tasktrackerapp.R
import com.example.tasktrackerapp.ui.activities.LoginActivity
import com.example.tasktrackerapp.utils.Constants

class AlarmService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification() {
       // sends user to login activity when notification gets clicked
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            Intent(context, LoginActivity::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_ONE_SHOT
            } else {
                0
            }
        )

        // will be displayed in notifications segment of ui
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
            .setContentTitle("Task Tracker")
            .setContentText("What tasks have you completed today?")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }
}