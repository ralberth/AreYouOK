package org.ralberth.areyouok

import android.app.Application
import android.media.MediaPlayer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SoundEffects @Inject constructor(private val application: Application) {
    private val _toggle: MediaPlayer        = MediaPlayer.create(application, R.raw.toggle_sound)
    private val _yellowWarning: MediaPlayer = MediaPlayer.create(application, R.raw.yellow_warning_sound)
    private val _redWarning: MediaPlayer    = MediaPlayer.create(application, R.raw.red_warning_sound)
    private val _timesUp: MediaPlayer       = MediaPlayer.create(application, R.raw.times_up_sound)

    init {
        _timesUp.isLooping = true
        println("Create SoundEffects object")
    }

    fun toggle()        {} // {  this._toggle.start()         }
    fun yellowWarning() {} // {  this._yellowWarning.start()  }
    fun redWarning()    {} // {  this._redWarning.start()     }
    fun timesUp()       {} // {  this._timesUp.start()        }

    fun stop() {
        // Times-up is the only long-running sound.  No need to interrupt the other sounds
        // since they're so short
//        if (this._timesUp.isPlaying) {
//            this._timesUp.stop()
//            this._timesUp.prepare()   // can't start() until you prepare()
//        }
    }
}
