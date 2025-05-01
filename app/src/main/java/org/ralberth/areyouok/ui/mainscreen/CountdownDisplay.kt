package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ralberth.areyouok.ui.theme.ProgressBarDanger
import org.ralberth.areyouok.ui.theme.ProgressBarOK
import org.ralberth.areyouok.ui.theme.ProgressBarWarning
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


var formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())


fun instant2humanReadableTime(t: Instant): String {
    val localTime = t.atZone(ZoneId.systemDefault()).toLocalTime();
    return localTime.format(formatter) ?: ""
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

    Row(modifier = Modifier.padding(18.dp)) {
        Column {
            if (start != null && end != null && remaining != null) {   // to protect all vars below
                Row {
                    Text("start ", color = Color.DarkGray)
                    Text(instant2humanReadableTime(start), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text("end ", color = Color.DarkGray)
                    Text(instant2humanReadableTime(end), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text("left ", color = Color.DarkGray)
                    Text("${remaining} min${if (remaining != 1) "s" else ""}", fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { remaining.toFloat() / length.toFloat() },
                    color = barColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            } else {
                // Create something with the smae height as above, but doesn't render anything
                Row {
                    Text(" ", fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(
                    progress = { 0f },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = MaterialTheme.colorScheme.background,
                    trackColor = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}
