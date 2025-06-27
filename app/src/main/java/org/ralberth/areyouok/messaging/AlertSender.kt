package org.ralberth.areyouok.messaging

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.ui.permissions.PermissionsHelper
import org.ralberth.areyouok.RuokIntents
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getCallingYouNow
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getChangedContact
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getCheckinMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getDurationChangedMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getLocationChangedMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getTurnedOffMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.getTurnedOnMessage
import org.ralberth.areyouok.messaging.RuokMessageStrings.Companion.missedCheckinMessage
import java.text.SimpleDateFormat
import java.time.Instant
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
        context.getSystemService(SmsManager::class.java)
    } else {
        SmsManager.getDefault()
    }

    val dtFormat: SimpleDateFormat = SimpleDateFormat("hh:mm aa", Locale.US)


    private fun createMessageId(): Long {
        return Instant.now().toEpochMilli()
    }


    private fun nextCheckinTime(countdownLength: Int): String {
        val nextCheckin = Calendar.getInstance()
        nextCheckin.add(Calendar.MINUTE, countdownLength)
        return dtFormat.format(nextCheckin.time)
    }


    private fun send(phoneNumber: String, msg: String) {
        val txtMsg = "⚕️ $msg"
        print("Sending sms message '$txtMsg' to '$phoneNumber' ... ")
        permHelper.guard(
            android.Manifest.permission.SEND_SMS,
            success = {
                val messageId = createMessageId()
                val messageParts = smsManager.divideMessage(txtMsg)  // Unicode limit is 70 characters
                println("Multi-part messages:")
//                for((index, value) in messageParts.withIndex())
//                    println("____$index \"$value\"")
                smsManager.sendMultipartTextMessage(
                    phoneNumber,
                    null,
                    messageParts,
                    intentGenerator.createTxtMessageSentPendingIntents(messageId, messageParts),
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
        val nextCheckinTime = nextCheckinTime(mins)
        val msg = getCheckinMessage(nextCheckinTime)
        send(phoneNumber, msg)
    }


    fun locationChanged(phoneNumber: String, newLocation: String) {
        send(phoneNumber, getLocationChangedMessage(newLocation))
    }


    fun durationChanged(phoneNumber: String, newDuration: Int) {
        val nextCheckinTime = nextCheckinTime(newDuration)
        send(phoneNumber, getDurationChangedMessage(newDuration, nextCheckinTime))
    }


    fun changedContact(oldNumber: String, newName: String) {
        send(oldNumber, getChangedContact(newName))
    }


    fun unresponsive(phoneNumber: String, location: String) {
        send(phoneNumber, missedCheckinMessage(location))
    }


    fun callingYouNow(phoneNumber: String, sequence: Int) {
        send(phoneNumber, getCallingYouNow(sequence))
    }
}
