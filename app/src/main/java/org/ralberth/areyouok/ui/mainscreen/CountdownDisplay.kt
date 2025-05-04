package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ralberth.areyouok.ui.theme.ProgressBarDanger
import org.ralberth.areyouok.ui.theme.ProgressBarOK
import org.ralberth.areyouok.ui.theme.ProgressBarWarning
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


var formatter = DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault())


fun instant2humanReadableTime(t: Instant?): String {
    if (t != null) {
        val localTime = t.atZone(ZoneId.systemDefault()).toLocalTime();
        return localTime.format(formatter) ?: ""
    } else {
        return ""
    }
}


@Composable
fun CountdownDisplay(start: Instant?, end: Instant?, length: Int, remaining: Int?) {
    val barColor: Color = when(remaining) {
        3    -> ProgressBarWarning
        2    -> ProgressBarWarning
        1    -> ProgressBarDanger
        0    -> ProgressBarDanger
        else -> ProgressBarOK
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (start != null) 1.0f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    Row(modifier = Modifier
        .padding(18.dp)
        .graphicsLayer { alpha = animatedAlpha }
    ) {
        Column {
            Row {
                Text("start ", color = Color.DarkGray)
                Text(instant2humanReadableTime(start), fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("end ", color = Color.DarkGray)
                Text(instant2humanReadableTime(end), fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("left ", color = Color.DarkGray)
                val remainingLabel = if (remaining != null)
                    "${remaining} min${if (remaining != 1) "s" else ""}"
                else
                    ""
                Text(remainingLabel, fontWeight = FontWeight.Bold)
            }

            val r = if (remaining != null) remaining else 0
            LinearProgressIndicator(
                progress = { r.toFloat() / length.toFloat() },
                color = barColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
        }
    }
}
