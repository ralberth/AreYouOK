package org.ralberth.areyouok.alarms

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
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
 *
 * The system creates a new BroadcastReceiver component object to handle each broadcast that it
 * receives. This object is valid only for the duration of the call to onReceive(Context, Intent).
 */
@AndroidEntryPoint
class RuokAlarmReceiver: AppWidgetProvider() {
    init {
        println("Create RuokAlarmReceiver entrypoint")
    }

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var coordinator: Coordinator


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent != null) {
            val minsLeft = intent.getIntExtra(EXTRA_KEY_MINS_LEFT, -1)
            if (minsLeft >= 0) {
                println("Received broadcast message: $minsLeft minutes left")
                coordinator.minutesLeft(minsLeft)
            } else {
                println("Received broadcast message: attribute \"$EXTRA_KEY_MINS_LEFT\" was missing")
                throw IllegalArgumentException("missing attr on broadcast message")
            }
        } else {
            println("Received broadcast message: no intent found!")
            throw IllegalArgumentException("no intent on broadcast message")
        }
    }
}
