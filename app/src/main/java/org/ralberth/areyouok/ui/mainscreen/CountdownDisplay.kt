package org.ralberth.areyouok.ui.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import kotlinx.coroutines.delay
import org.ralberth.areyouok.minutesBeforeEnd
import org.ralberth.areyouok.progressPercent
import org.ralberth.areyouok.ui.theme.ProgressBarDanger
import org.ralberth.areyouok.ui.theme.ProgressBarOK
import org.ralberth.areyouok.ui.theme.ProgressBarWarning
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


var formatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())


fun instant2humanReadableTime(t: Instant?, zone: ZoneId = ZoneId.systemDefault()): String {
    if (t != null) {
        val localTime = t.atZone(zone).toLocalTime();
        return localTime.format(formatter) ?: ""
    } else {
        return ""
    }
}


@SuppressLint("DefaultLocale")
fun duration2humanreadable(d: Duration?): String {
    if (d == null)
        return ""

    // TODO: why doesn't Java 8 + Android 29 have a better way to do this?!
    val hours:   Int = d.toHours().toInt()
    val justSeconds: Int = (d.seconds - (hours * 60 * 60)).toInt()
    val minutes = justSeconds / 60
    val seconds = justSeconds % 60

    return when {
        hours == 0 && minutes == 0 -> String.format("%ds", seconds)
        hours == 0                 -> String.format("%dm %ds", minutes, seconds)
        else                       -> String.format("%dh %dm %ds", hours, minutes, seconds)
    }
}


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
fun Ticker(targetTime: Instant?, content: @Composable (timeRemaining: Duration?) -> Unit) {
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
            while (true) {
                timeRemaining = Duration.between(Instant.now(), targetTime)
                delay(1000)
            }
        }
    }
}


@Composable
fun CountdownDisplay(start: Instant?, end: Instant?, modifier: Modifier = Modifier) {
    Ticker(end) {
        timeRemaining ->
            Row(modifier = modifier.padding(18.dp)) {
                Column {
                    Row {
                        Text("start ", color = Color.DarkGray)
                        Text(instant2humanReadableTime(start))
                        Spacer(Modifier.weight(1f))
                        Text("end ", color = Color.DarkGray)
                        Text(instant2humanReadableTime(end))
                        Spacer(Modifier.weight(1f))
                        Text("left ", color = Color.DarkGray)
                        Text(duration2humanreadable(timeRemaining))
                    }

                    val minsLeft = minutesBeforeEnd(end)
                    val barColor: Color = when(minsLeft) {
                        3    -> ProgressBarWarning
                        2    -> ProgressBarWarning
                        1    -> ProgressBarDanger
                        0    -> ProgressBarDanger
                        else -> ProgressBarOK
                    }

                    // TODO: still jumpy even with smooth values for progressPercent()
                    LinearProgressIndicator(
                        progress = { 1f - progressPercent(start, end) },
                        color = barColor,
                        gapSize = (-15).dp,
                        drawStopIndicator = {},
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )
                }
            }
    }
}
