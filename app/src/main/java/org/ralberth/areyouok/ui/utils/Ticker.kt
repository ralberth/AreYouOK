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
 * UI Composable with no visible things.  This manages a side-effect coroutine that
 * ticks down once every second until a destination time happens.
 * Each time the tick happens, it modifies timeRemaining and causes a recompose of
 * itself and the @Composable passed into Ticker.
 * See https://stackoverflow.com/questions/71191340/how-can-i-implement-a-timer-in-a-portable-way-in-jetpack-compose
 * for inspiration for this.
 *
 * Key design points:
 *    1. LaunchedEffect() runs a coroutine anytime this becomes visible or disappears from the
 *       display.
 *    2. The coroutine happily ticks down and assigns new values to timeRemaining.
 *    3. timeRemaining is remember() and is passed to the composable passed-in, so it
 *       causes a recompose.
 *    4. LifecycleResumeEffect is called anytime the Ticker becomes visible or invisible.
 *       We use this to change the coroutine from ticking down to not ticking down: there's
 *       no point in recomposing this every second when nothing is visible.
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

    content.invoke(timeRemaining)

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
//                println("TICKER: timeLeft = ${timeLeft.toMillis()}ms, isNegative=${timeLeft.isNegative}, timeRemaining = ${timeRemaining!!.toMillis()}ms")
                delay(tickLengthMS)
            }
        }
    }
}
