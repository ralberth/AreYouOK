package org.ralberth.areyouok

import android.app.Application
import android.media.MediaPlayer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SoundEffects @Inject constructor(private val application: Application) {
    private val togglePlayer:  MediaPlayer = MediaPlayer.create(application, R.raw.button_click_sound)
    private val warningPlayer: MediaPlayer = MediaPlayer.create(application, R.raw.warning_sound)
    private val alarmPlayer:   MediaPlayer = MediaPlayer.create(application, R.raw.tng_red_alert2)

    fun toggle()  {  this.togglePlayer.start()   }
    fun warning() {  this.warningPlayer.start()  }
    fun alarm()   {  this.alarmPlayer.start()    }
}
