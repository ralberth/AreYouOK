package org.ralberth.areyouok.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.MainActivity
import org.ralberth.areyouok.R
import javax.inject.Inject
import javax.inject.Singleton


//TODO: new Activity like phone alarmclock, pops-up whole new UI with animated clock.  Add button to check-in and cancel.
//TODO: Maybe new AppState that is just a POJO.  methods like start() and checkin() handle sounds, alerts, alarms, everything.  This makes the viewmodel simpler.
//Intent for notifications opens MainActivity
/*
 * This doesn't use a channel's ability to play sounds when notifications are sent.
 * Android tries to be polite, so it won't play sounds on every notification.
 * This won't work for us, so there's a separate SoundEffects class that handles
 * playing all sounds.  Also, the final sound when time runs out is played periodically
 * instead of only one time when the notification goes out.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Singleton
class RuokNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_RUDE:   String = "rude"
        const val CHANNEL_POLITE: String = "polite"
    }

    private val notificationMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private val silentMp3 = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.resources.getResourcePackageName(R.raw.silent))
        .appendPath(context.resources.getResourceTypeName(R.raw.silent))
        .appendPath(context.resources.getResourceEntryName(R.raw.silent))
        .build()
    private val audioAttrs = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    init {
        // Highest prio: always shows notifications, bypass do-not-disturb, use lights
        // Used for T-1 minute and when time runs out
        _createChannel(
            CHANNEL_RUDE,
            NotificationManager.IMPORTANCE_HIGH,
            Color.BLUE,
            true
        )

        // Polite: don't pop-up toasts, respect do-not-disturb
        // Used when there is more than 1 minute left
        _createChannel(
            CHANNEL_POLITE,
            NotificationManager.IMPORTANCE_DEFAULT,
            null,
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun _createChannel(
        channelId: String,
        importance: Int,
        lightColor: Int?,
        bypassDoNotDisturb: Boolean
    ) {
        val channel = NotificationChannel(
            channelId,
            "RUOK reminder channel (${channelId})",
            importance
        )
        if (lightColor != null) {
            channel.lightColor = lightColor
            channel.shouldShowLights()
        }
        channel.setBypassDnd(bypassDoNotDisturb)
        channel.setSound(silentMp3, audioAttrs)
        notificationMgr.createNotificationChannel(channel)
    }


    fun _createIntent(): PendingIntent {
        val uiIntent = Intent(context, MainActivity::class.java)//.apply {
//            data = "hither"
//        }
        return PendingIntent.getActivity(
            context,
            123,
            uiIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    fun canSendNotifications(): Boolean {
        return notificationMgr.areNotificationsEnabled()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(channelId: String, message: String, iconColor: Int) {
        // Ugh, see https://developer.android.com/develop/ui/views/notifications/channels
        // Must set PRIORITY that matches the channel's IMPORTANCE.
        val prio = when (channelId) {
            CHANNEL_RUDE -> NotificationCompat.PRIORITY_HIGH
            else -> NotificationCompat.PRIORITY_DEFAULT
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(iconColor)
            .setContentTitle("Check-in Reminder")
            .setContentText(message)
            .setContentIntent(_createIntent())
            .setPriority(prio)  // NotificationCompat.PRIORITY_HIGH)

        cancelAll()   // we only ever have one notification visible at a time
        notificationMgr.notify(123, builder.build())
    }


    fun cancelAll() {
        notificationMgr.cancelAll()
    }
}
