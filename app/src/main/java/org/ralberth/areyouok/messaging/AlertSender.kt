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

/*
 *
 * Ideas for other emojis:
 *      ❓ ( (?)🪧🚑🚨⚕️
 */
@Singleton
class AlertSender @Inject constructor(
    private val permHelper: PermissionsHelper
) {
    val smsManager: SmsManager = SmsManager.getDefault()
    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    fun _send(msg: String) {

        // FIXME: have to get perms before sending messages...might be passed-out at this time

        val txtMsg: String = "⚕️ $msg"
        println("Sending sms message '$txtMsg' ...")
        permHelper.guard(
            PackageManager.FEATURE_TELEPHONY,
            android.Manifest.permission.SEND_SMS,
            success = {
                smsManager.sendTextMessage(NOTIFY_PHONE_NUMBER, null, txtMsg, null, null)
                println("Sent via SMS!")
            },
            fallback = { println("Couldn't send sms message, permissions denied: '$txtMsg'") }
        )
    }


    fun enabled(mins: Int) {
        _send("⚪ Alerting turned on.  Check-ins every $mins minutes.")
    }


    fun disabled() {
        _send("⚫ Alerting turned off.")
    }


    fun checkin(mins: Int) {
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, mins)
        val at: String = dtFormat.format(nextCheckin.getTime())
        _send("👍 Check-in!  Next check-in at $at")
    }


    fun unresponsive() {
        _send("🚨 MISSED LAST CHECK-IN 🚨")
    }
}
