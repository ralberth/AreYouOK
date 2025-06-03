package org.ralberth.areyouok

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.alarms.RuokAlarmReceiver
import org.ralberth.areyouok.messaging.AlertErrorHandler
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
    }

    fun createMinsLeftPendingIntent(minsLeft: Int): PendingIntent {
        val intent: Intent = Intent(context, RuokAlarmReceiver::class.java).apply {
            putExtra(EXTRA_KEY_MSGTYPE, EXTRA_VAL_MSGTYPE_MINSLEFT)
            putExtra(EXTRA_KEY_MINS_LEFT, minsLeft)
        }

        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_BASE_MINSLEFT + minsLeft,   // each alarm needs a unique requestCode
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

    fun createTxtMessageSentPendingIntent(): PendingIntent {
        val intent: Intent = Intent(context, AlertErrorHandler::class.java).apply {
            putExtra(EXTRA_KEY_MSGTYPE, EXTRA_VAL_MSGTYPE_TXTMSG)
        }

        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_TXTMSG,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
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
}
