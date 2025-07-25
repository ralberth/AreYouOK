package org.ralberth.areyouok

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.telecom.TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.alarms.RuokAlarmReceiver
import org.ralberth.areyouok.messaging.AlertErrorHandler
import org.ralberth.areyouok.movement.MovementService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RuokIntents @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val EXTRA_KEY_MSGTYPE = "MESSAGE_TYPE"
        const val EXTRA_VAL_MSGTYPE_MINSLEFT = "MINUTES_LEFT"
        const val EXTRA_VAL_MSGTYPE_RUOKUI = "RUOK_UI"
        const val EXTRA_VAL_MSGTYPE_CHECKIN = "CHECKIN"
        const val EXTRA_VAL_MSGTYPE_TXTMSG = "TXT_MSG"
        const val EXTRA_KEY_MINS_LEFT = "MINUTES_LEFT"
        const val REQUEST_CODE_CHECKIN = 90
        const val REQUEST_CODE_RUOKUI = 91
        const val REQUEST_CODE_TXTMSG = 92
        const val REQUEST_CODE_BASE_MINSLEFT = 100

        const val ACTION_SMS_SENT = "org.ralberth.areyouok.action.SMS_SENT"
        const val ACTION_START_SERVICE = "org.ralberth.areyouok.action.START_SERVICE"
        const val ACTION_STOP_SERVICE = "org.ralberth.areyouok.action.STOP_SERVICE"
    }


    fun getRequestCodeForMinsLeft(minsLeft: Int): Int {
        return REQUEST_CODE_BASE_MINSLEFT + minsLeft
    }


    fun createMinsLeftPendingIntent(minsLeft: Int): PendingIntent {
        val intent: Intent = Intent(context, RuokAlarmReceiver::class.java).apply {
            putExtra(EXTRA_KEY_MSGTYPE, EXTRA_VAL_MSGTYPE_MINSLEFT)
            putExtra(EXTRA_KEY_MINS_LEFT, minsLeft)
        }

        return PendingIntent.getBroadcast(
            context,
            getRequestCodeForMinsLeft(minsLeft),   // each alarm needs a unique requestCode
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    fun createRuokUiPendingIntent(): PendingIntent {
        val intent: Intent = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_KEY_MSGTYPE, EXTRA_VAL_MSGTYPE_RUOKUI)
        }

        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_RUOKUI,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    fun createTxtMessageSentPendingIntent(messageId: Long, message: String, msgNumber: Int? = null, msgCount: Int? = null): PendingIntent {
        val uriFragment = if (msgNumber != null) "$messageId:$msgNumber:$msgCount" else messageId.toString()
        val intent = Intent(
            ACTION_SMS_SENT,
            Uri.fromParts("app", "org.ralberth.areyouok", uriFragment),
            context,
            AlertErrorHandler::class.java
        ).apply {
            putExtra(EXTRA_KEY_MSGTYPE, EXTRA_VAL_MSGTYPE_TXTMSG)
            putExtra("messagePartText", message)
            if (msgNumber != null)
                putExtra("messagePartIndex", msgNumber)
            if (msgCount != null)
                putExtra("messagePartCount", msgCount)
        }

        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_TXTMSG,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    // See https://stackoverflow.com/questions/24673595/how-to-get-sms-sent-confirmation-for-each-contact-person-in-android/24845193#24845193
    // for a full example and description of how this has to work.
    fun createTxtMessageSentPendingIntents(messageId: Long, textParts: ArrayList<String>): ArrayList<PendingIntent> {
        var pendingIntents: ArrayList<PendingIntent> = arrayListOf()
        for ((index, value) in textParts.withIndex()) {
            pendingIntents += createTxtMessageSentPendingIntent(
                messageId,
                value,
                index,
                textParts.size
            )
        }

        return pendingIntents
    }


    fun createBringTaskToForegroundIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }


    // TODO: Created for a "Check-in" button on notifications, but not currently used.
    fun createCheckinPendingIntent(): PendingIntent {
        val intent: Intent = Intent(context, RuokAlarmReceiver::class.java).apply {
            putExtra(EXTRA_KEY_MSGTYPE, EXTRA_VAL_MSGTYPE_CHECKIN)
        }

        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_CHECKIN,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    fun createMakePhoneCallIntent(phoneNumber: String): Intent {
        return Intent(Intent.ACTION_CALL, "tel:${phoneNumber}".toUri()).apply {
            putExtra(EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
            setFlags(FLAG_ACTIVITY_NEW_TASK) // because we're outside of our own Activity
        }
    }


    fun createStartMovementServiceIntent(): Intent {
        return Intent(context, MovementService::class.java).apply {
            setAction(ACTION_START_SERVICE)
        }
    }


    fun createStopMovementServiceIntent(): Intent {
        return Intent(context, MovementService::class.java).apply {
            setAction(ACTION_STOP_SERVICE)
        }
    }
}
