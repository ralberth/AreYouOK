package org.ralberth.areyouok.movement

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.MainActivity
import org.ralberth.areyouok.R
import org.ralberth.areyouok.notifications.RuokNotifier
import org.ralberth.areyouok.notifications.RuokNotifier.Companion.NOTIFY_CHANNEL_NAME
import javax.inject.Inject


@AndroidEntryPoint
class MovementService : Service() {
//    @Inject
//    lateinit var notifier: RuokNotifier

    @Inject
    lateinit var movementSource: MovementSource


//    override fun onCreate() {
//        super.onCreate()
//        println("MovementService.onCreate()")
//    }


//    override fun onDestroy() {
//        super.onDestroy()
//        println("MovementService.onDestroy()")
//        movementSource.stopListening(this)
//    }



    inner class LocalBinder : Binder() {
        fun getService(): MovementService = this@MovementService
    }
    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("MovementService.onStartCommand()")

        val notification = NotificationCompat.Builder(this, NOTIFY_CHANNEL_NAME)
            .setContentTitle("ServiceTitle")
            .setContentText("ServiceText")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 12345, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            })
            .build()

        ServiceCompat.startForeground(
            this,
            1,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        )

//        movementSource.listenForUpdates(this)

        return super.onStartCommand(intent, flags, startId)
    }
}
