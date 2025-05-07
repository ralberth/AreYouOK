package org.ralberth.areyouok.ui.mainscreen;

import android.os.CountDownTimer
import org.ralberth.areyouok.Constants.Companion.MS_PER_MIN
import java.time.Instant
import kotlin.math.roundToInt


/*
 * PRIVATE class used only by DelayCountdownTimer below.
 * This abstracts away the CountDownTimer that comes with the android runtime.  CountDownTimer
 * deals in milliseconds instead of seconds, and has more callbacks than we need.  Wrap it
 * here for simplicity.
 *
 * One edge case: when time runs out, this calls *BOTH* onMinuteElapsed() and onComplete().
 */
class MinuteTickTimer(
    mins: Int,                             // How many minutes total to countdown
    val onMinuteElapsed: (Int) -> Unit,    // Callback once a minute while this is running
    val onComplete: () -> Unit             // Callback once when timer runs out

): CountDownTimer(
    mins * MS_PER_MIN,
    MS_PER_MIN
) {
    // Called by CountDownTimer parent class once every minute
    override fun onTick(millisUntilFinished: Long) {
        // millisUntilFinished might not be a precise multiple of MS_TO_MIN, so be safe
        // and do a floating-point division. This gets us a float that's as close to what
        // we want (like 5.044837 when we want 5).
        val minsElapsed = (millisUntilFinished.toFloat() / MS_PER_MIN.toFloat()).roundToInt()
        onMinuteElapsed(minsElapsed)
    }


    // Called by CountDownTimer parent class when time runs out
    override fun onFinish() {
        onMinuteElapsed(0)
        onComplete()
    }
}


/*
 * Non-UI class that ticks down every minute, calling callbacks as it goes.
 * Used *ONLY* by the UI to show progress on UI widgets.
 */
class DelayCountdownTimer(
    val onMinuteElapsed: (Int) -> Unit,    // Callback once a minute while this is running
    val onComplete: () -> Unit             // Callback once when timer runs out
) {
    var timer: MinuteTickTimer? = null
    var lastMins: Int = 0

    fun onTick(millisUntilFinished: Long) {

    }

    fun onFinish() {

    }

    fun start(stopTime: Instant) {
//        lastMins = mins
//        timer = CountdownTimer(
//            stopTime.epochSecond,
//            DelayCountdownTimer::onTick,
//            DelayCountdownTimer::onFinish
//        )
//        timer!!.start()
    }


    fun reset() {
        this.cancel()
//        this.start(this.lastMins)
    }


    fun cancel() {
        timer?.cancel()
//        timer = null
    }
}
