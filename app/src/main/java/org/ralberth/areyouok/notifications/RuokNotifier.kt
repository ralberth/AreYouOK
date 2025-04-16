package org.ralberth.areyouok.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.R
import javax.inject.Inject
import javax.inject.Singleton


//TODO: why won't this actually *SHOW* all notifications?!  They're there in the tray, but don't pop-up.

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
@Singleton
class RuokNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_HIGH: String = "RuokChannelHigh"
        const val CHANNEL_MEDIUM: String = "RuokChannelMedium"
        const val CHANNEL_LOW: String = "RuokChannelLow"
    }

    private val notificationMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    private var nextId: Int = 0


    // TODO: no need keeping this private val around, only ref them by channelId string
    @RequiresApi(Build.VERSION_CODES.O)
    private val channels = mapOf(
        CHANNEL_HIGH to _createChannel(
                        CHANNEL_HIGH,
                        "Checkin Reminders Channel 0",
                        NotificationManager.IMPORTANCE_HIGH,
                        "Notifications when time runs out (TXT messages sent)",
                        null,
                        true,
//                        AudioAttributes.USAGE_ALARM,
//                        R.raw.times_up_sound
                    ),
        CHANNEL_MEDIUM to _createChannel(
                        CHANNEL_MEDIUM,
                        "Checkin Reminders Channel 1",
                        NotificationManager.IMPORTANCE_HIGH,
                        "One minute before time runs out",
                        null,
                        true,
//                        AudioAttributes.USAGE_ALARM,
//                        R.raw.times_up_sound   // red_warning_sound
                    ),
        CHANNEL_LOW to _createChannel(
                        CHANNEL_LOW,
                        "Checkin Reminders Channel 2",
                        NotificationManager.IMPORTANCE_DEFAULT,
                        "Time is running out",
                        null,
                        false,
//                        AudioAttributes.USAGE_NOTIFICATION,
//                        R.raw.yellow_warning_sound
                    )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun _createChannel(
        channelId: String,
        channelName: String,
        importance: Int,
        channelDescription: String,
        lightColor: Int?,
        bypassDoNotDisturb: Boolean,
//        soundUsage: Int,  // AudioAttributes.USAGE_NOTIFICATION or USAGE_ALARM
//        soundResourceId: Int  // like "R.raw.mysoundfile"
    ) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            importance
        )
        channel.description = channelDescription
        if (lightColor != null) {
            channel.lightColor = lightColor
            channel.shouldShowLights()
        }
        channel.setBypassDnd(bypassDoNotDisturb)

//        val uri: Uri = with (context.resources) {
//            Uri.Builder()
//                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//                .authority(getResourcePackageName(soundResourceId))
//                .appendPath(getResourceTypeName(soundResourceId))
//                .appendPath(getResourceEntryName(soundResourceId))
//                .build()
//        }
//        val audioAttrs = AudioAttributes.Builder()
//            .setUsage(soundUsage)
//            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//            .build()
//        channel.setSound(uri, audioAttrs)

        notificationMgr.createNotificationChannel(channel)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(channelId: String, message: String, iconColor: Int) {
        // Ugh, see https://developer.android.com/develop/ui/views/notifications/channels
        // Must set PRIORITY that matches the channel's IMPORTANCE.
        val prio = when (channelId) {
            CHANNEL_HIGH -> NotificationCompat.PRIORITY_HIGH
            CHANNEL_MEDIUM -> NotificationCompat.PRIORITY_HIGH
            else -> NotificationCompat.PRIORITY_DEFAULT
        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(iconColor)
            .setContentTitle("Check-in Reminder")
            .setContentText(message)
//                .setContentIntent
            .setPriority(prio)  // NotificationCompat.PRIORITY_HIGH)

//        this.nextId += 1
        cancelAll()   // we only ever have one notification visible at a time
        notificationMgr.notify(this.nextId, builder.build())
    }


    fun cancelAll() {
        notificationMgr.cancelAll()
//        this.nextId = 0
    }
}
