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

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification() {
        val intent = Intent(context, LoginActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }
        )

        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATIONS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
            .setContentTitle("Task Tracker")
            .setContentText("What tasks have you completed today?")
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }
}