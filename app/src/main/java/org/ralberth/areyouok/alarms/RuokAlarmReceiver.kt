package org.ralberth.areyouok.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.alarms.RuokNotifier.Companion.CHANNEL_HIGH
import org.ralberth.areyouok.alarms.RuokNotifier.Companion.CHANNEL_LOW
import org.ralberth.areyouok.alarms.RuokNotifier.Companion.CHANNEL_MEDIUM
import javax.inject.Inject


/*
 * Alarm Receiver: class created by Android (see AndroidManifest.xml)
 * instead of participating with Hilt or Dagger.  This is a small
 * adapter class that just calls the RuokChannels class, which does
 * all the real work.
 */
@AndroidEntryPoint
class RuokAlarmReceiver @Inject constructor(
    private val notifier: RuokNotifier
) : BroadcastReceiver() {

    init {
        print("Create RuokAlarmReceiver entrypoint")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val minsLeft = intent?.getIntExtra(EXTRA_KEY_MINS_LEFT, 9) ?: return

        val channelId = when (minsLeft) {
            0 -> CHANNEL_HIGH
            1 -> CHANNEL_MEDIUM
            else -> CHANNEL_LOW
        }

        val message = when (minsLeft) {
            0 -> "Times up!  Sent TXT message to family."
            1 -> "ONE MINUTE LEFT"
            2 -> "Two minutes left"
            3 -> "Three minutes left"
            else -> "Time is running out"
        }
        val iconColor = when (minsLeft) {
            0 -> Color.argb(200, 255, 0, 0)
            1 -> Color.argb(200, 255, 0, 0)
            2 -> Color.argb(255, 255, 255, 0)
            3 -> Color.argb(255, 255, 255, 0)
            else -> Color.argb(200, 0, 255, 0)
        }

        notifier.sendNotification(channelId, "Check-In Reminder", message, iconColor)
    }
}
