package org.ralberth.areyouok.messaging

import android.telephony.SmsManager
import org.ralberth.areyouok.PermissionsHelper
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
    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    private fun send(phoneNumber: String, msg: String) {
        val txtMsg = "⚕️ $msg"
        print("Sending sms message '$txtMsg' to '$phoneNumber' ...")
        permHelper.guard(
            android.Manifest.permission.SEND_SMS,
            success = {
                smsManager.sendTextMessage(phoneNumber, null, txtMsg, null, null)
                println("done")
            },
            fallback = { println("permission denied") }
        )
    }


    fun enabled(phoneNumber: String, mins: Int) {
        println("AlertSender.enabled($mins): send \"Alerting turned on\"")
        send(phoneNumber, "⚪ Alerting turned on.  Check-ins every $mins minutes.")
    }


    fun disabled(phoneNumber: String) {
        println("AlertSender.disbaled(): send \"Alerting turned off\"")
        send(phoneNumber, "⚫ Alerting turned off.")
    }


    fun checkin(phoneNumber: String, mins: Int) {
        println("AlertSender.checkin($mins): send \"Check-in!\"")
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, mins)
        val at: String = dtFormat.format(nextCheckin.time)
        send(phoneNumber, "👍 Check-in!  Next check-in at $at")
    }


    fun unresponsive(phoneNumber: String) {
        println("AlertSender.unresponsive(): send \"MISSED LAST CHECK-IN\"")
        send(phoneNumber, "🚨 MISSED LAST CHECK-IN 🚨")
    }
}
