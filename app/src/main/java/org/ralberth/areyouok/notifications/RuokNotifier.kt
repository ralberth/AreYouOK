package org.ralberth.areyouok.notifications

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.RuokIntents
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton


/*
 * There are two types of channels here for notifications:
 *     1. Channels that handle "the timer is about to expire"
 *     2. Error messages from background events, like sending TXTs
 *
 * All messages from #1 are important and must be seen and heard.  Because of this,
 * we don't use a channel's ability to play sounds when notifications are sent.
 * Android tries to be polite, so it won't play sounds on every notification.
 * This won't work for us, so there's a separate SoundEffects class that handles
 * playing all sounds.  Also, the final sound when time runs out is played periodically
 * instead of only one time when the notification goes out.
 *
 * Messages from #2 can make a sound or not, based on whatever android thinks is
 * best.  They're notifications about a problem that the user might not be able
 * to affect, so being polite isn't that bad.
 *
 * The messages in the expire notification channels are special: we only ever want one
 * of them visible to the user at a time.  For example, if there's a notification
 * up about there being 2 minutes left, that can go away when we display another
 * notification that there is 1 minute left.  Seeing two notifications with different
 * "minutes left" is confusing.  Because of this, we use a feature of NotificationManager:
 * If you re-use a notificationID when posting a notification, it will replace any existing
 * notification with that same ID.  IDs are unique per *application*, so we use the same
 * ID for all T-3, T-2, T-1, and T-0 notifications, and generate unique IDs for the
 * error channel.
 */
@Singleton
class RuokNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
    private val intentGenerator: RuokIntents
) {
    companion object {
        const val TIME_REMAINING_MESSAGE_ID = 900
        const val ALERT_CHANNEL_NAME = "rude"
        const val NOTIFY_CHANNEL_NAME = "polite"
        const val ERROR_CHANNEL_NAME = "errors"

        val SILENT_URI = "file:///android_asset/sounds/silent.mp3".toUri()
    }

    val notificationMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private val timerAlertChannel = RuokChannel(
        notificationMgr,
        context,
        ALERT_CHANNEL_NAME,
        "Time's almost up alerts",
        soundUri = SILENT_URI
    )

    private val timerNotifyChannel = RuokChannel(
        notificationMgr,
        context,
        NOTIFY_CHANNEL_NAME,
        "Time's almost up notifications",
        isHighImportance = false,
        bypassDoNotDisturb = false,
        soundUri = SILENT_URI
    )

    private val errorChannel = RuokChannel(
        notificationMgr,
        context,
        "errors",
        ERROR_CHANNEL_NAME,
        soundUri = "file:///android_asset/sounds/error.mp3".toUri()
    )


    fun sendTimerNotification(minsLeft: Int, message: String) {
        val intent = intentGenerator.createRuokUiPendingIntent()
        val channel = if (minsLeft >= 2) timerNotifyChannel else timerAlertChannel
        channel.sendNotification(
            TIME_REMAINING_MESSAGE_ID,   // reusing the same ID means we replace any existing with same ID
            "Timer Notification",
            message,
            intent
        )
    }


    fun sendErrorNotification(message: String) {
        val messageId = (1751117224 - Instant.now().epochSecond).toInt()
        errorChannel.sendNotification(
            messageId,
            "Error",
            message
        )
    }


    fun cancelAll() {
        notificationMgr.cancelAll()
    }
}
