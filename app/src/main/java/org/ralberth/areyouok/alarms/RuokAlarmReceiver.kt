package org.ralberth.areyouok.alarms

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.coordinator.Coordinator
import javax.inject.Inject


/*
 * Alarm Receiver: class created by Android (see AndroidManifest.xml) instead of participating
 * with Hilt or Dagger.  Android requires a no-arg constructor, so we handle injection after
 * creation with a lateinit.  AppWidgetProvider is a subclass of BroadcastReceiver, so Android
 * will recognize it and call appropriate parent class methods.
 *
 * This is a tiny adapter class that can be created and called by the android runtime and just
 * sends everything to the Coordinator.  The Coordinator is in charge of everything that the
 * whole app does (other than UI drawing stuff).
 */
@AndroidEntryPoint
class RuokAlarmReceiver: AppWidgetProvider() {
    init {
        print("Create RuokAlarmReceiver entrypoint")
    }

    @Inject
    lateinit var coordinator: Coordinator


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val minsLeft = intent?.getIntExtra(EXTRA_KEY_MINS_LEFT, 9) ?: return
        coordinator.minutesLeft(minsLeft)
    }
}
