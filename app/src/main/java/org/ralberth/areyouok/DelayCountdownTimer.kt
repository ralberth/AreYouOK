package org.ralberth.areyouok;

import android.os.CountDownTimer
import kotlin.math.roundToInt


val MS_IN_MIN = 60 * 1000


class DelayCountdownTimer(
    val mins: Int,                         // Total number of minutes until timer is complete
    val onMinuteElapsed: (Int) -> Unit,    // Callback once a minute while this is running
    val onComplete: () -> Unit             // Callback once when timer runs out
)
: CountDownTimer(
    (mins * MS_IN_MIN).toLong(),          // Convert minutes to milliseconds
    MS_IN_MIN.toLong()                    // Each "onTick" callback happens once a minute
) {

    override fun onTick(millisUntilFinished: Long) {
        // millisUntilFinished might not be a precise multiple of MS_TO_MIN, so be safe
        // and do a floating-point division. This gets us a float that's as close to what
        // we want (like 5.044837 when we want 5).
        val minsElapsed = (millisUntilFinished.toFloat() / MS_IN_MIN.toFloat()).roundToInt()
        onMinuteElapsed(minsElapsed)
    }

    override fun onFinish() {
        onComplete()
    }
}
