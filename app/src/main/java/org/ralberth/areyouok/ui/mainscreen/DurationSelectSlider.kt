package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp


@Composable
fun DurationSelectSlider(enabled: Boolean, minutes: Int, onChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(18.dp)) {
        Column {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Countdown Delay:   ")
                    }
                    append("$minutes minutes")
                }
            )
            Slider(
                enabled = enabled,
                value = minutes.toFloat(),
                onValueChange = { onChange(it.toInt()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 10,
                valueRange = 5f..60f
            )
        }
    }
}
