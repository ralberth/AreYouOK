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
    val smsManager: SmsManager = SmsManager.getDefault()
    val phoneNumber: String = "7039676933"
    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    fun _send(msg: String) {

        // FIXME: have to get perms before sending messages...might be passed-out at this time

        val txtMsg = "[RUOK?] $msg"
        println("Sending sms message '$txtMsg' ...")
        permHelper.guard(
            PackageManager.FEATURE_TELEPHONY,
            android.Manifest.permission.SEND_SMS,
            success = {
                smsManager.sendTextMessage(phoneNumber, null, txtMsg, null, null)
                println("Sent via SMS!")
            },
            fallback = { println("Couldn't send sms message, permissions denied: '$txtMsg'") }
        )
    }


    fun enabled(mins: Int) {
        _send("Alerting turned on.  Check-ins every $mins minutes.")
    }


    fun disabled() {
        _send("Alerting turned off.")
    }


    fun checkin(mins: Int) {
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, mins)
        val at: String = dtFormat.format(nextCheckin.getTime())
        _send("Check-in!  Next check-in at $at")
    }


    fun unresponsive() {
        _send("!!! MISSED LAST CHECK-IN !!!")
    }
}
