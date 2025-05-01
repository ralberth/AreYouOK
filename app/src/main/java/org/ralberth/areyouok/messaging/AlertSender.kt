package org.ralberth.areyouok.messaging

import android.content.pm.PackageManager
import android.telephony.SmsManager
import org.ralberth.areyouok.Constants.Companion.NOTIFY_PHONE_NUMBER
import org.ralberth.areyouok.PermissionsHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

// Ideas for other emojis: ❓ ( (?)🪧🚑🚨⚕️

@Singleton
class AlertSender @Inject constructor(
    private val permHelper: PermissionsHelper
) {
    val smsManager: SmsManager = SmsManager.getDefault()
    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    private fun send(msg: String) {
        val txtMsg = "⚕️ $msg"
        print("Sending sms message '$txtMsg' ...")
        permHelper.guard(
            android.Manifest.permission.SEND_SMS,
            success = {
                smsManager.sendTextMessage(NOTIFY_PHONE_NUMBER, null, txtMsg, null, null)
                println("done")
            },
            fallback = { println("permission denied") }
        )
    }


    fun enabled(mins: Int) {
        println("AlertSender.enabled($mins): send \"Alerting turned on\"")
        send("⚪ Alerting turned on.  Check-ins every $mins minutes.")
    }


    fun disabled() {
        println("AlertSender.disbaled(): send \"Alerting turned off\"")
        send("⚫ Alerting turned off.")
    }


    fun checkin(mins: Int) {
        println("AlertSender.checkin($mins): send \"Check-in!\"")
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, mins)
        val at: String = dtFormat.format(nextCheckin.time)
        send("👍 Check-in!  Next check-in at $at")
    }


    fun unresponsive() {
        println("AlertSender.unresponsive(): send \"MISSED LAST CHECK-IN\"")
        send("🚨 MISSED LAST CHECK-IN 🚨")
    }
}
