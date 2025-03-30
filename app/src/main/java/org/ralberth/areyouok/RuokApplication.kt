package org.ralberth.areyouok

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class RuokApplication : Application() {

    @Inject
    lateinit var alertSender: AlertSender
}
