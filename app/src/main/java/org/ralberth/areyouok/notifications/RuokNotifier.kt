package org.ralberth.areyouok.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.R
import org.ralberth.areyouok.RuokIntents
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
 * "minutes left" is confusing.  Because of this, we keep track of certain Intents so
 * we can clear them later.  All error notifications from "#2" above are not affected
 * by this logic.
 */
@Singleton
class RuokNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
    private val intentGenerator: RuokIntents
) {
    private val timerAlertChannel = RuokChannel(
        context,
        "rude",
        "Time's almost up alerts"
    )

    private val timerNotifyChannel = RuokChannel(
        context,
        "polite",
        "Time's almost up notifications",
        isHighImportance = false,
        bypassDoNotDisturb = false
    )

    private val errorChannel = RuokChannel(
        context,
        "errors",
        "Error messages",
        soundResourceId = R.raw.error_sound
    )


    private var nextNotificationId = 1   // every notification gets a unique ID, this is a "one-up"
    private var lastTimerNotificationId: Int? = null   // last msg ID to RUDE or POLITE

    private fun getNextNotificationId(): Int {
        val next = nextNotificationId
        nextNotificationId += 1
        return next
    }


    fun canSendNotifications(): Boolean {
        return errorChannel.notificationMgr.areNotificationsEnabled()
    }



    fun sendTimerNotification(minsLeft: Int, message: String) {
        val intent = intentGenerator.createRuokUiPendingIntent()

        cancelLastTimerNotification()
        val id = getNextNotificationId()
        lastTimerNotificationId = id

        val channel = if (minsLeft >= 2) timerNotifyChannel else timerAlertChannel
        channel.sendNotification(
            id,
            "Timer Notification",
            message,
            intent
        )
    }


    fun sendErrorNotification(message: String) {
        errorChannel.sendNotification(
            getNextNotificationId(),
            "Error",
            message
        )
    }

    fun cancelLastTimerNotification() {
        if (lastTimerNotificationId != null) {
            errorChannel.notificationMgr.cancel(lastTimerNotificationId!!)
            lastTimerNotificationId = null
        }
    }
}
