package org.ralberth.areyouok.coordinator

import android.graphics.Color
import org.ralberth.areyouok.SoundEffects
import org.ralberth.areyouok.alarms.RuokAlarms
import org.ralberth.areyouok.messaging.AlertSender
import org.ralberth.areyouok.notifications.RuokNotifier
import org.ralberth.areyouok.notifications.RuokNotifier.Companion.CHANNEL_RUDE
import org.ralberth.areyouok.notifications.RuokNotifier.Companion.CHANNEL_POLITE
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Coordinator @Inject constructor(
    private val soundEffects: SoundEffects,
    private val alarms: RuokAlarms,
    private val notifier: RuokNotifier,
    private val alertSender: AlertSender
) {
    private var delayMins: Int = 20   // cached here between calls to enabled() and checkin()


    fun enabled(delayMins: Int) {
        this.delayMins = delayMins // used elsewhere in this class
        soundEffects.toggle()
        alarms.setAlarms(delayMins)
        notifier.cancelAll()  // just in case
        alertSender.enabled(delayMins)
    }


    fun disabled() {
        soundEffects.stop()  // in case we're still playing the whoop whoop
        soundEffects.toggle()
        alarms.cancelAllAlarms()
        notifier.cancelAll()
        alertSender.disabled()
    }


    fun minutesLeft(minsLeft: Int) {
        when (minsLeft) {
            0 -> {
                soundEffects.timesUp()
                notifier.sendNotification(
                    CHANNEL_RUDE,
                    "Times up!  Sent TXT message to family.",
                    Color.argb(200, 255, 0, 0)
                )
                alertSender.unresponsive()
            }
            1 -> {
                soundEffects.redWarning()
                notifier.sendNotification(
                    CHANNEL_RUDE,
                    "* ONE MINUTE LEFT *",
                    Color.argb(200, 255, 0, 0)
                )
            }
            2 -> {
                soundEffects.yellowWarning()
                notifier.sendNotification(
                    CHANNEL_POLITE,
                    "Two minutes left",
                    Color.argb(200, 255, 255, 0)
                )
            }
            3 -> {
                soundEffects.yellowWarning()
                notifier.sendNotification(
                    CHANNEL_POLITE,
                    "Three minutes left",
                    Color.argb(200, 255, 255, 0)
                )
            }
        }
    }


    fun checkin() {
        soundEffects.stop()
        soundEffects.toggle()
        alarms.cancelAllAlarms()
        alarms.setAlarms(this.delayMins)
        notifier.cancelAll()
        alertSender.checkin(this.delayMins)
    }
}
