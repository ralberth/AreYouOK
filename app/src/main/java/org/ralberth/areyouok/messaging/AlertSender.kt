package org.ralberth.areyouok.messaging

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.PermissionsHelper
import org.ralberth.areyouok.RuokIntents
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getCheckinMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getLocationChangedMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getTurnedOffMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getTurnedOnMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.missedCheckinMessage
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
        send(phoneNumber, getTurnedOnMessage(mins, location))
    }


    fun disabled(phoneNumber: String) {
        send(phoneNumber, getTurnedOffMessage())
    }


    fun checkin(phoneNumber: String, mins: Int) {
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, mins)
        val at: String = dtFormat.format(nextCheckin.time)
        val msg = getCheckinMessage(at)
        send(phoneNumber, msg)
    }


    fun locationChanged(phoneNumber: String, newLocation: String) {
        send(phoneNumber, getLocationChangedMessage(newLocation))
    }


    fun unresponsive(phoneNumber: String, location: String) {
        send(phoneNumber, missedCheckinMessage(location))
    }
}
