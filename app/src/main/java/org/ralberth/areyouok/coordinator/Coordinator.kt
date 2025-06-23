package org.ralberth.areyouok.coordinator

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.VibrationEffect
import android.os.Vibrator
import android.telecom.TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.SoundEffects
import org.ralberth.areyouok.alarms.RuokAlarms
import org.ralberth.areyouok.datamodel.RuokDatastore
import org.ralberth.areyouok.messaging.AlertSender
import org.ralberth.areyouok.notifications.RuokNotifier
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Coordinator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val soundEffects: SoundEffects,
    private val alarms: RuokAlarms,
    private val notifier: RuokNotifier,
    private val alertSender: AlertSender,
    private val prefs: RuokDatastore
) {
    // TODO: skip this here and add vibration pattern to the AlertChannel only
    private val vibrator = context.getSystemService(Vibrator::class.java)
    private val vibrationEffect = VibrationEffect.createOneShot(1000L, 128)


    fun enabled(countdownLength: Int) {
        println("Coordinator.enabled($countdownLength): set alarms, send TXT message")
        soundEffects.toggle()
        alarms.setAlarms(countdownLength)
        notifier.cancelLastTimerNotification()  // just in case
        alertSender.enabled(prefs.getPhoneNumber(), countdownLength, prefs.getLocation())
    }


    fun disabled() {
        println("Coordinator.disabled(): turn everything off, send TXT message")
        soundEffects.stop()  // in case we're still playing the whoop whoop
        soundEffects.toggle()
        alarms.cancelAllAlarms()
        notifier.cancelLastTimerNotification()
        alertSender.disabled(prefs.getPhoneNumber())
    }


    @Throws(IllegalArgumentException::class)
    fun minutesLeft(minsLeft: Int) {
        when (minsLeft) {
            0 -> {
                println("Coordinator.minutesLeft($minsLeft): send TXT message, sound alarm")
                vibrator.vibrate(vibrationEffect)
                soundEffects.timesUp()
                notifier.sendTimerNotification(
                    0,
                    "🚨 Times up!  Sent TXT message to family. 🚨",
//                    Color.argb(200, 255, 0, 0)
                )
                alertSender.unresponsive(
                    prefs.getPhoneNumber(),
                    prefs.getLocation())
            }
            1 -> {
                println("Coordinator.minutesLeft($minsLeft): play sound, new notification")
                vibrator.vibrate(vibrationEffect)
                soundEffects.redWarning()
                notifier.sendTimerNotification(
                    1,
                    "😮 ONE MINUTE LEFT 😮",
//                    Color.argb(200, 255, 0, 0)
                )
            }
            2 -> {
                println("Coordinator.minutesLeft($minsLeft): play sound, new notification")
                soundEffects.yellowWarning()
                notifier.sendTimerNotification(
                    2,
                    "😥 Two minutes left 😥",
//                    Color.argb(200, 255, 255, 0)
                )
            }
            3 -> {
                println("Coordinator.minutesLeft($minsLeft): play sound, new notification")
                soundEffects.yellowWarning()
                notifier.sendTimerNotification(
                    3,
                    "🪧 Three minutes left 🪧",
//                    Color.argb(200, 255, 255, 0)
                )
            }
            else -> {
                throw IllegalArgumentException("Coordinator.minsLeft($minsLeft) called with bad value")
            }
        }
    }


    fun checkin(countdownLength: Int) {
        println("Coordinator.checkin(): reschedule all alarms, cancel notifications, send TXT message")
        resetEverything(countdownLength)
        alertSender.checkin(prefs.getPhoneNumber(), countdownLength)
    }


    fun durationChanged(newLength: Int) {
        println("Coordinator.changeDuration(): reschedule all alarms, cancel notifications, send TXT message")
        resetEverything(newLength)
        alertSender.durationChanged(prefs.getPhoneNumber(), newLength)
    }


    private fun resetEverything(countdownLength: Int) {
        soundEffects.stop()
        soundEffects.checkIn()
        alarms.cancelAllAlarms()
        alarms.setAlarms(countdownLength)
        notifier.cancelLastTimerNotification()
    }


    fun updatePhone(oldNumber: String, newName: String, newNumber: String, mins: Int, location: String) {
        alertSender.changedContact(oldNumber, newName)
        alertSender.enabled(newNumber, mins, location)
    }


    fun updateLocation(newLocation: String) {
        // ViewModel makes sure not to call us unless the countdown is alive
        alertSender.locationChanged(prefs.getPhoneNumber(), newLocation)
    }


    fun callContact(phoneNumber: String) {
        val intent = Intent(
            Intent.ACTION_CALL,
            "tel:${phoneNumber}".toUri()
        )
        intent.putExtra(EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK) // because we're outside of our main/only Activity
        context.startActivity(intent)
    }
}
