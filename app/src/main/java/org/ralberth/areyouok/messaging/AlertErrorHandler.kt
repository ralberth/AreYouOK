package org.ralberth.areyouok.messaging

import android.app.Activity
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ralberth.areyouok.RuokIntents.Companion.EXTRA_KEY_MSGTYPE
import org.ralberth.areyouok.RuokIntents.Companion.EXTRA_VAL_MSGTYPE_TXTMSG
import org.ralberth.areyouok.notifications.RuokNotifier
import javax.inject.Inject


@AndroidEntryPoint
class AlertErrorHandler: AppWidgetProvider() {
    init {
        println("Create TxtMsgErrorHandler entrypoint")
    }

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var notifier: RuokNotifier


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent != null) {
            val alarmType = intent.getStringExtra(EXTRA_KEY_MSGTYPE) ?: ""
            if (alarmType != EXTRA_VAL_MSGTYPE_TXTMSG)
                throw IllegalArgumentException("AlertErrorHandler.onReceive() didn't get a msgtype txtmsg")

            val resultCode = this.resultCode
//            if (resultCode != Activity.RESULT_OK)
                notifier.sendErrorNotification("Error sending TXT msg: $resultCode")
        }
    }
}
