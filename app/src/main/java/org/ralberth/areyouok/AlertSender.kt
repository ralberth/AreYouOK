package org.ralberth.areyouok

import android.telephony.SmsManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlertSender @Inject constructor() {
//    val smsManager: SmsManager = SmsManager.getDefault()
//    val phoneNumber: String = "7032299874"
    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)

    fun _send(msg: String) {
        val txtMsg = "[RUOK?] $msg"
//        smsManager.sendTextMessage(phoneNumber,null, txtMsg,null,null)
        println(txtMsg)
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
        _send("Last check-in missed.")
    }
}
