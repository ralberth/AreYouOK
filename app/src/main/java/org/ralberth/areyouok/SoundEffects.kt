package org.ralberth.areyouok

import android.app.Application
import android.media.MediaPlayer
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SoundEffects @Inject constructor(application: Application) {

    /*
     * Going with the lower-level, simpler MediaPlayer for playing sounds.  We need something
     * simple that always works when the app isn't running and might be triggered by an
     * alert in the AlarmReceiver.  Also, don't need any UI controls for these.
     */
    private val toggle:        MediaPlayer = MediaPlayer.create(application, R.raw.toggle_sound)
    private val checkIn:       MediaPlayer = MediaPlayer.create(application, R.raw.checkin_sound)
    private val yellowWarning: MediaPlayer = MediaPlayer.create(application, R.raw.yellow_warning_sound)
    private val redWarning:    MediaPlayer = MediaPlayer.create(application, R.raw.red_warning_sound)
    private val timesUp:       MediaPlayer = MediaPlayer.create(application, R.raw.times_up_sound)

    init {
        println("Create SoundEffects object")
        timesUp.isLooping = true  //  The final notification sound plays continuously
    }

    fun toggle()        {  this.toggle.start()         }
    fun checkIn()       {  this.checkIn.start()        }
    fun yellowWarning() {  this.yellowWarning.start()  }
    fun redWarning()    {  this.redWarning.start()     }
    fun timesUp()       {  this.timesUp.start()        }

    fun stop() {
        // Times-up is the only long-running sound.  No need to interrupt the other sounds
        // since they're so short
        if (this.timesUp.isPlaying) {
            this.timesUp.stop()
            this.timesUp.prepare()   // can't start() until you prepare()
        }
    }
}
