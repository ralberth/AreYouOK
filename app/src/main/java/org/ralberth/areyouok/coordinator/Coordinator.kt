package org.ralberth.areyouok.coordinator

import android.graphics.Color
import org.ralberth.areyouok.SoundEffects
import org.ralberth.areyouok.alarms.RuokAlarms
import org.ralberth.areyouok.datamodel.RuokDatastore
import org.ralberth.areyouok.messaging.AlertSender
import org.ralberth.areyouok.notifications.RuokNotifier
import org.ralberth.areyouok.notifications.RuokNotifier.Companion.CHANNEL_RUDE
import org.ralberth.areyouok.notifications.RuokNotifier.Companion.CHANNEL_POLITE
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Coordinator @Inject constructor(
    private val soundEffects: SoundEffects,
    private val alarms: RuokAlarms,
    private val notifier: RuokNotifier,
    private val alertSender: AlertSender,
    private val prefs: RuokDatastore
) {
    private var delayMins: Int = 20   // cached here between calls to enabled() and checkin()

    fun enabled(delayMins: Int) {
        println("Coordinator.enabled($delayMins): set alarms, send TXT message")
        this.delayMins = delayMins // used elsewhere in this class
        soundEffects.toggle()
        alarms.setAlarms(delayMins)
        notifier.cancelAll()  // just in case
        alertSender.enabled(prefs.getPhoneNumber(), delayMins)
    }


    fun disabled() {
        println("Coordinator.disabled(): turn everything off, send TXT message")
        soundEffects.stop()  // in case we're still playing the whoop whoop
        soundEffects.toggle()
        alarms.cancelAllAlarms()
        notifier.cancelAll()
        alertSender.disabled(prefs.getPhoneNumber())
    }


    @Throws(IllegalArgumentException::class)
    fun minutesLeft(minsLeft: Int) {
        when (minsLeft) {
            0 -> {
                println("Coordinator.minutesLeft($minsLeft): send TXT message, sound alarm")
                soundEffects.timesUp()
                notifier.sendNotification(
                    CHANNEL_RUDE,
                    "🚨 Times up!  Sent TXT message to family. 🚨",
                    Color.argb(200, 255, 0, 0)
                )
                alertSender.unresponsive(prefs.getPhoneNumber())
            }
            1 -> {
                println("Coordinator.minutesLeft($minsLeft): play sound, new notification")
                soundEffects.redWarning()
                notifier.sendNotification(
                    CHANNEL_RUDE,
                    "😮 ONE MINUTE LEFT 😮",
                    Color.argb(200, 255, 0, 0)
                )
            }
            2 -> {
                println("Coordinator.minutesLeft($minsLeft): play sound, new notification")
                soundEffects.yellowWarning()
                notifier.sendNotification(
                    CHANNEL_POLITE,
                    "😥 Two minutes left 😥",
                    Color.argb(200, 255, 255, 0)
                )
            }
            3 -> {
                println("Coordinator.minutesLeft($minsLeft): play sound, new notification")
                soundEffects.yellowWarning()
                notifier.sendNotification(
                    CHANNEL_POLITE,
                    "🪧 Three minutes left 🪧",
                    Color.argb(200, 255, 255, 0)
                )
            }
            else -> {
                throw IllegalArgumentException("Coordinator.minsLeft($minsLeft) called with bad value")
            }
        }
    }


    fun checkin() {
        println("Coordinator.checkin(): reschedule all alarms, cancel notifications, send TXT message")
        soundEffects.stop()
        soundEffects.checkIn()
        alarms.cancelAllAlarms()
        alarms.setAlarms(this.delayMins)
        notifier.cancelAll()
        alertSender.checkin(prefs.getPhoneNumber(), this.delayMins)
    }
}
