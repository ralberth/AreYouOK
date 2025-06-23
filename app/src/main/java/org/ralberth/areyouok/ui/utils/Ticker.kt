package org.ralberth.areyouok.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant


/*
 * UI Composable with no visible things.  This manages a side-effect coroutine that ticks down once
 * every *tickLengthMS* until a destination time happens.  Each time the tick happens, it modifies
 * timeRemaining and causes a recompose of itself and the @Composable passed into Ticker.
 *
 * See https://stackoverflow.com/questions/71191340/how-can-i-implement-a-timer-in-a-portable-way-in-jetpack-compose
 * for inspiration for this.
 *
 * **WARNING:** this will cause `content` to be called ***at least*** the number of times based on
 * tickLengthMS, and probably a lot more.  Anytime this `Ticker` gets a recompose it will call
 * `content` to recompose itself, even though the coroutine hasn't ticked down to the next value
 * based on tickLengthMS.
 *
 * This means you can't rely on this calling `content` exactly once every ticketLengthMS milliseconds
 * and you can't rely on it not calling `content` more than once for any particular value or even
 * calling `content` more than once with a zero value.
 *
 * Because targetTime can be null, this will also call `content(null)`, possibly a lot of times
 * based on other things that cause a recompose.  The caller of `Ticker` has to do all the right
 * things to avoid logic errors based on this, unfortunately.
 *
 * Case in point:
 * ```
 *    Ticker(Instant.now()) {
 *        TextEdit(...)
 *    }
 * ```
 *
 * Every time the TextEdit value changes, it might cause a recompose based on callbacks.  The
 * Ticker ***has*** to recompose its children when it is recomposed or the whole thing won't work
 * right.  So, the stuff "below" the Ticker is responsible for dealing with duplicates, nulls, etc.
 *
 * Key design points:
 *    1. LaunchedEffect() runs a coroutine anytime this becomes visible or disappears from the
 *       display.
 *    2. The coroutine happily ticks down and assigns new values to timeRemaining.
 *    3. timeRemaining is remember() and is passed to the composable passed-in, so it
 *       causes a recompose.
 *    4. LifecycleResumeEffect is called anytime the Ticker becomes visible or invisible.
 *       We use this to change the coroutine from ticking down to not ticking down: there's
 *       no point in recomposing this every tickLengthMS when nothing is visible.
 */
@Composable
fun Ticker(
    targetTime: Instant?,
    tickLengthMS: Long = 1000, // call content() every this many milliseconds
    content: @Composable (timeRemaining: Duration?) -> Unit
) {
    var timeRemaining by remember(targetTime) {
        mutableStateOf(
            if (targetTime != null) Duration.between(Instant.now(), targetTime) else null
        )
    }

//    println("Ticker recompose: timeRemaining=${timeRemaining?.toMillis()} (ms)")
    content.invoke(timeRemaining)    // called a LOT more than only when the coroutine below causes a recompose

    var isVisible by remember { mutableStateOf(true) }
    LifecycleResumeEffect(Unit) {
        isVisible = true
        onPauseOrDispose { isVisible = false }
    }

    LaunchedEffect(isVisible, targetTime) {
        if (isVisible && targetTime != null) {
            // Don't let timeRemaining go negative and make 100% sure it emits a last value of
            // zero for timeRemaining.seconds and timeRemaining.milliseconds.  This way, consumers
            // get a final recomposition with the timer down to "zero".
            var haveEmittedZeroValue = false
            while (true) {
                var timeLeft = Duration.between(Instant.now(), targetTime)
                if (timeLeft.isNegative)
                    if (!haveEmittedZeroValue) {
                        timeLeft = Duration.ofMillis(0L)
                        haveEmittedZeroValue = true
                    } else
                        break
                timeRemaining = timeLeft
//                println("TICKER: timeLeft = ${timeLeft.toMillis()}ms, isNegative=${timeLeft.isNegative}, timeRemaining = ${timeRemaining!!.toMillis()}ms, haveEmittedZeroValue=$haveEmittedZeroValue")
                delay(tickLengthMS)
            }
        }
    }
}
