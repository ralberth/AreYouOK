package org.ralberth.areyouok

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.ralberth.areyouok.messaging.AlertSender
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    // TODO: do we need this here?  AlertSender is @Singleton and @Inject...
    @Inject
    lateinit var alertSender: AlertSender
}
