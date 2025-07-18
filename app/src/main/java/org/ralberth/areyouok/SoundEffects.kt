package org.ralberth.areyouok

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.net.toUri
import org.ralberth.areyouok.SoundEffects.Companion.SOUND_EFFECT_STREAM
import org.ralberth.areyouok.datamodel.RuokDatastore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SoundEffects @Inject constructor(
    private val application: Application,
    ruokDatastore: RuokDatastore
) {
    companion object {
        const val SOUND_EFFECT_STREAM = AudioManager.STREAM_ALARM
    }

    val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun newPlayer(id: Int, continuous: Boolean = false): SoundEffectPlayer {
        return SoundEffectPlayer(
            context = application,
            audioManager = audioManager,
            rawResourceId = id,
            playContinuously = continuous
        )
    }

    private val toggle          = newPlayer(R.raw.toggle_sound)
    private val checkIn         = newPlayer(R.raw.checkin_sound)
    private val yellowWarning   = newPlayer(R.raw.yellow_warning_sound)
    private val redWarning      = newPlayer(R.raw.red_warning_sound)
    private val timesUpOneShot  = newPlayer(R.raw.times_up_sound)
    private val timesUpLooping  = newPlayer(R.raw.times_up_sound, true)

    private val noMovement      = newPlayer(R.raw.no_movement_sound)
    private val mvmtCall5Sec    = newPlayer(R.raw.movement_call_contact_5_sec_sound)
    private val movement        = newPlayer(R.raw.movement_detected_sound)

    var overrideVolumePercent: Float? = ruokDatastore.getVolumePercent()

    fun newOverrideVolumePercent(newVolumePercent: Float?) {
        overrideVolumePercent = newVolumePercent
    }


    fun toggle()         { toggle.play()  }  // not affected by override volume
    fun checkIn()        { checkIn.play() }  // not affected by override volume
    fun yellowWarning()  { yellowWarning.play(overrideVolumePercent) }
    fun redWarning()     { redWarning.play(overrideVolumePercent) }
    fun timesUpOneShot() { timesUpOneShot.play(overrideVolumePercent) }
    fun timesUpLooping() { timesUpLooping.play(overrideVolumePercent) }

    fun noMovement()     { noMovement.play(overrideVolumePercent) }
    fun mvmtCall5Sec()   { mvmtCall5Sec.play() }
    fun movement()       { movement.play() }

    fun stopAll() {
        // Only sounds that are more than an instant long
        redWarning.stop()
        timesUpLooping.stop()
    }
}


class SoundEffectPlayer(
    context: Context,
    private val audioManager: AudioManager,
    rawResourceId: Int,
    playContinuously: Boolean
) {
    val packageName = context.getPackageName()
    val uri = "android.resource://${packageName}/${rawResourceId}".toUri()
    val player = MediaPlayer().apply {
        setDataSource(context, uri)
        setAudioStreamType(SOUND_EFFECT_STREAM)
        setOnCompletionListener {
            if (normalVolume != null) {
                audioManager.setStreamVolume(
                    SOUND_EFFECT_STREAM,
                    normalVolume!!,
                    0
                )
                println("Sound complete, put audio volume back to ${normalVolume!!}")
            } else
                println("No override volume, so don't have to reset anything")
        }
        isLooping = playContinuously
        prepare()
    }
    var normalVolume: Int? = null


    fun play(overrideVolume: Float? = null) {
        if (overrideVolume != null) {
            normalVolume = audioManager.getStreamVolume(SOUND_EFFECT_STREAM)
            val maxVolume = audioManager.getStreamMaxVolume(SOUND_EFFECT_STREAM)
            val newVolume = (maxVolume.toFloat() * overrideVolume).toInt() + 5
            audioManager.setStreamVolume(SOUND_EFFECT_STREAM, newVolume, 0)
            println("About to play sound, normal volume $normalVolume set to $newVolume")
        } else
            normalVolume = null   // triggers callback above not to touch the stream volume

        player.start()
    }


    fun stop() {
        if (player.isPlaying) {
            player.stop()  // triggers the listener to adjust volume back where it was
            player.prepare() // can't play() without a prepare()
        }
    }
}