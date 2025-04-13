package org.ralberth.areyouok

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var alertSender: AlertSender

    override fun onCreate() {
        super.onCreate()
        val channelId = "CheckinRemindersChannel"
        val channelName = "Checkin Reminders"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
}
