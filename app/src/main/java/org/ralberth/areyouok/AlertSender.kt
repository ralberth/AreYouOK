package org.ralberth.areyouok

import android.content.pm.PackageManager
import android.telephony.SmsManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlertSender @Inject constructor(
    private val permHelper: PermissionsHelper
) {
    private val smsManager: SmsManager = SmsManager.getDefault()
    private val phoneNumber: String = "7032299874"
    private val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    init {
        println("Create AlertSender")
    }


    private fun send(msg: String) {
        val txtMsg = "[RUOK?] $msg"
        println("Sending sms message '$txtMsg' ...")
        permHelper.guard(
            PackageManager.FEATURE_TELEPHONY,
            android.Manifest.permission.SEND_SMS,
            success = {
                smsManager.sendTextMessage(phoneNumber, null, txtMsg, null, null)
                println("Sent via SMS!")
            },
            fallback = { println("Couldn't send sms message, permission denied: '$txtMsg'") }
        )
    }


    fun enabled(minutes: Int) {
        send("Alerting turned on.  Check-ins every $minutes minutes.")
    }


    fun disabled() {
        send("Alerting turned off.")
    }


    fun checkIn(minutes: Int) {
        val nextCheckIn = Calendar.getInstance()
        nextCheckIn.add(Calendar.MINUTE, minutes)
        val at: String = dtFormat.format(nextCheckIn.time)
        send("Check-in!  Next check-in at $at")
    }


    fun unresponsive() {
        send("Last check-in missed.  CALL ME, I might be in trouble!")
    }
}
