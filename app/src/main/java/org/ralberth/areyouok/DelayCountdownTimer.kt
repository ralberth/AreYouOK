package org.ralberth.areyouok;

import android.os.CountDownTimer
import kotlin.math.roundToInt


//val TICK2MS = 60 * 1000   // one minute

// Uncomment for testing: sets the tick amount to 2 seconds so you
// can debug easier
val TICK2MS = 2000L


class MinuteTickTimer(
    val mins: Int,
    val onMinuteElapsed: (Int) -> Unit,    // Callback once a minute while this is running
    val onComplete: () -> Unit             // Callback once when timer runs out

): CountDownTimer(
    mins * TICK2MS,
    TICK2MS
) {
    override fun onTick(millisUntilFinished: Long) {
        // millisUntilFinished might not be a precise multiple of MS_TO_MIN, so be safe
        // and do a floating-point division. This gets us a float that's as close to what
        // we want (like 5.044837 when we want 5).
        val minsElapsed = (millisUntilFinished.toFloat() / TICK2MS.toFloat()).roundToInt()
        onMinuteElapsed(minsElapsed)
    }

    override fun onFinish() {
        onComplete()
    }
}

class DelayCountdownTimer(
    val onMinuteElapsed: (Int) -> Unit,    // Callback once a minute while this is running
    val onComplete: () -> Unit             // Callback once when timer runs out
) {
    var timer: MinuteTickTimer? = null
    var lastMins: Int = 0

    fun start(mins: Int) {
        lastMins = mins
        timer = MinuteTickTimer(
            mins,
            onMinuteElapsed,
            onComplete
        )
        timer!!.start()
    }

    fun reset() {
        this.cancel()
        this.start(this.lastMins)
    }

    fun cancel() {
        timer?.cancel()
        timer = null
    }
}
