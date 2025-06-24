package org.ralberth.areyouok.messaging

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager.RESULT_ERROR_GENERIC_FAILURE
import android.telephony.SmsManager.RESULT_ERROR_NO_SERVICE
import android.telephony.SmsManager.RESULT_ERROR_NULL_PDU
import android.telephony.SmsManager.RESULT_ERROR_RADIO_OFF
import android.telephony.SmsManager.RESULT_NETWORK_ERROR
import android.telephony.SmsManager.RESULT_NO_DEFAULT_SMS_APP
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.RuokIntents.Companion.ACTION_SMS_SENT
import org.ralberth.areyouok.notifications.RuokNotifier
import javax.inject.Inject


@AndroidEntryPoint
class AlertErrorHandler: BroadcastReceiver() {

    @Inject
    lateinit var notifier: RuokNotifier


    override fun onReceive(context: Context?, intent: Intent?) {
        /*
         * Careful!  You're running on the main thread.  Android does things if this takes longer
         * than 10 seconds to complete.  Don't do anything real here.
         * This object is no longer alive after returning from this function: don't do async
         * things here that assume the process/Activity stays around if it isn't running already.
         */
        val action = intent?.action
        val data = intent?.dataString ?: ""
        val resultCode = this.resultCode
        val errorCode = intent?.getStringExtra("errorCode") ?: ""
        println("AlertErrorHandler: got intent: action=$action, data=$data, resultCode=${resultCode}")
        when (action) {
            ACTION_SMS_SENT -> handleTxtMessageSent(data, resultCode, errorCode)
            else -> throw IllegalArgumentException("AlertErrorHandler.onReceive() got unknown action of $action")
        }
    }


    private fun handleTxtMessageSent(data: String, resultCode: Int, errorCode: String) {
        if (resultCode == Activity.RESULT_OK) {
            println("Msg sent successfully")
        } else {
            val error = when (resultCode) {
                RESULT_ERROR_GENERIC_FAILURE -> "generic failure (error code $errorCode)"
                RESULT_ERROR_NO_SERVICE -> "no service"
                RESULT_ERROR_NULL_PDU -> "no PDU"
                RESULT_ERROR_RADIO_OFF -> "radio turned off"
                RESULT_NETWORK_ERROR -> "network error"
                RESULT_NO_DEFAULT_SMS_APP -> "no default TXT message app"
                else -> "unknown (code $resultCode)"
            }
            notifier.sendErrorNotification("Error sending TXT msg: $error")
        }
    }
}
