package org.ralberth.areyouok.soundeffects

import android.app.Application
import android.content.Context
import android.media.AudioManager
import org.ralberth.areyouok.datamodel.RuokDatastore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SoundEffects @Inject constructor(
    application: Application,
    ruokDatastore: RuokDatastore
) {
    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val assets = application.assets

    private fun newPlayer(name: String, continuous: Boolean = false): SoundEffectPlayer {
        val filename = "sounds/$name.mp3"
        val mmapFile = assets.openFd(filename)
        try {
            return SoundEffectPlayer(
                audioManager = audioManager,
                assetFd = mmapFile,
                playContinuously = continuous
            )
        } finally {
            mmapFile.close()
        }
    }


    private var toggle = newPlayer("toggle")   // always uses the same sound

    private var checkIn:        SoundEffectPlayer? = null
    private var yellowWarning:  SoundEffectPlayer? = null
    private var redWarning:     SoundEffectPlayer? = null
    private var timesUpOneShot: SoundEffectPlayer? = null
    private var timesUpLooping: SoundEffectPlayer? = null
    private var noMovement:     SoundEffectPlayer? = null
    private var mvmtCall5Sec:   SoundEffectPlayer? = null
    private var movement:       SoundEffectPlayer? = null


    private var soundStyle: String = ""   // see init below

    fun setSoundStyle(newStyle: String) {
        if (soundStyle != newStyle) {   // Never re-load the same sounds if we can avoid it
            soundStyle = newStyle

            checkIn?.release()
            yellowWarning?.release()
            redWarning?.release()
            timesUpOneShot?.release()
            timesUpLooping?.release()
            noMovement?.release()
            mvmtCall5Sec?.release()
            movement?.release()

            checkIn        = newPlayer("$soundStyle/check-in")
            yellowWarning  = newPlayer("$soundStyle/countdown_yellow_warning")
            redWarning     = newPlayer("$soundStyle/countdown_red_warning")
            timesUpOneShot = newPlayer("$soundStyle/countdown_expired")
            timesUpLooping = newPlayer("$soundStyle/countdown_expired", true)
            noMovement     = newPlayer("$soundStyle/movement_none")
            mvmtCall5Sec   = newPlayer("$soundStyle/movement_call_contact_5_sec")
            movement       = newPlayer("$soundStyle/movement_detected")
        }
    }

    init {
        setSoundStyle(ruokDatastore.getSoundStyle())   // players are never null: create on class init
    }


    var overrideVolumePercent: Float? = ruokDatastore.getVolumePercent()

    fun newOverrideVolumePercent(newVolumePercent: Float?) {
        overrideVolumePercent = newVolumePercent
    }


    fun toggle()         { toggle.play()  }   // not affected by override volume
    fun checkIn()        { checkIn?.play() }  // not affected by override volume
    fun yellowWarning()  { yellowWarning?.play(overrideVolumePercent) }
    fun redWarning()     { redWarning?.play(overrideVolumePercent) }
    fun timesUpOneShot() { timesUpOneShot?.play(overrideVolumePercent) }
    fun timesUpLooping() { timesUpLooping?.play(overrideVolumePercent) }

    fun noMovement()     { noMovement?.play(overrideVolumePercent) }
    fun mvmtCall5Sec()   { mvmtCall5Sec?.play(overrideVolumePercent) }
    fun movement()       { movement?.play() }  // not affected by override volume


    fun stopAll() {
        // Only sounds that are more than an instant long
        redWarning?.stop()
        timesUpLooping?.stop()
    }
}
