package org.ralberth.areyouok.messaging

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.PermissionsHelper
import org.ralberth.areyouok.RuokIntents
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AlertSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val intentGenerator: RuokIntents,
    private val permHelper: PermissionsHelper
) {
    companion object {
        fun getTurnedOnMessage(mins: Int, location: String): String {
            return "⚪ Alerting turned on.  Check-ins every $mins minutes.  Current location: $location"
        }


        fun getTurnedOffMessage(): String {
            return "⚫ Alerting turned off."
        }


        fun getCheckinMessage(nextCheckin: String): String {
            return "👍 Check-in!  Next check-in at $nextCheckin"
        }


        fun missedCheckinMessage(location: String): String {
            return "🚨 MISSED LAST CHECK-IN 🚨  Last known location: $location"
        }
    }


    val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService<SmsManager>(SmsManager::class.java)
    } else {
        SmsManager.getDefault()
    }

    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    private fun send(phoneNumber: String, msg: String) {
        val txtMsg = "⚕️ $msg"
        print("Sending sms message '$txtMsg' to '$phoneNumber' ...")
        permHelper.guard(
            android.Manifest.permission.SEND_SMS,
            success = {
                smsManager.sendTextMessage(
                    phoneNumber,
                    null,
                    txtMsg,
                    intentGenerator.createTxtMessageSentPendingIntent(),
                    null
                )
                println("done")
            },
            fallback = { println("permission denied") }
        )
    }



    fun enabled(phoneNumber: String, mins: Int, location: String) {
        println("AlertSender.enabled($mins): send \"Alerting turned on\"")
        send(phoneNumber, getTurnedOnMessage(mins, location))
    }


    fun disabled(phoneNumber: String) {
        println("AlertSender.disbaled(): send \"Alerting turned off\"")
        send(phoneNumber, getTurnedOffMessage())
    }


    fun checkin(phoneNumber: String, mins: Int) {
        println("AlertSender.checkin($mins): send \"Check-in!\"")
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, mins)
        val at: String = dtFormat.format(nextCheckin.time)
        send(phoneNumber, getCheckinMessage(at))
    }


    fun unresponsive(phoneNumber: String, location: String) {
        println("AlertSender.unresponsive(): send \"MISSED LAST CHECK-IN\"")
        send(phoneNumber, missedCheckinMessage(location))
    }
}
