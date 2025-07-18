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
import org.ralberth.areyouok.coordinator.Coordinator
import org.ralberth.areyouok.datamodel.RuokDatastore
import org.ralberth.areyouok.notifications.RuokNotifier.Companion.NOTIFY_CHANNEL_NAME
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


@AndroidEntryPoint
class MovementService : Service(), Runnable {

    @Inject
    lateinit var movementSource: MovementSource

    @Inject
    lateinit var coordinator: Coordinator

    @Inject
    lateinit var prefs: RuokDatastore


    private var thread = Thread(this)
    private var threadShouldStop = AtomicBoolean(false)


    override fun onCreate() {
        println("MovementService.onCreate()")
        super.onCreate()
        thread.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        println("MovementService.onDestroy()")
        threadShouldStop.set(true)
    }



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
            .setContentTitle("R U OK?")
            .setContentText("Movement Monitor")
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

        return super.onStartCommand(intent, flags, startId)
    }


    override fun run() {
        var periodsWithNoMovement = 0
        while(! threadShouldStop.get()) {
            val cutoff = prefs.getNoMovementThreshold()
            val history = movementSource.getHistory()
            if (history.size >= 12) {  // so we don't do checks immediately after starting to listen
                val hasAnyMovement = history.any { it > cutoff }
                if (hasAnyMovement)
                    periodsWithNoMovement = 0
                else
                    periodsWithNoMovement += 1
                coordinator.periodsWithNoMovement(periodsWithNoMovement)
            }

            Thread.sleep(5000)
        }
    }
}
