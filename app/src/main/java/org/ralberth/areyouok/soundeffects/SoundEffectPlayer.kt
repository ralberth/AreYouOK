package org.ralberth.areyouok.soundeffects

import android.content.res.AssetFileDescriptor
import android.media.AudioManager
import android.media.MediaPlayer


class SoundEffectPlayer(
    private val audioManager: AudioManager,
    assetFd: AssetFileDescriptor,
    playContinuously: Boolean
) {
    companion object {
        const val SOUND_EFFECT_STREAM = AudioManager.STREAM_ALARM
    }

    val player = MediaPlayer().apply {
        setDataSource(assetFd)
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


    fun release() {
        player.release()   // Important!  Removes system resources.
    }
}
