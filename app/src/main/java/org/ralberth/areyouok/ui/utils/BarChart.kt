package org.ralberth.areyouok.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.ralberth.areyouok.ui.theme.AreYouOkTheme

// https://medium.com/better-programming/custon-charts-in-android-using-jetpack-compose-87b395c1d515
// TODO: there's a cool gauge display at this website too


@Composable
internal fun BarChart(
    maxHeight: Dp = 200.dp,
    maxValue: Float,
    cutoff: Float,
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier.then(
            Modifier
                .height(maxHeight)
                .drawBehind {
                    val cutoffHeight = size.height - (cutoff / maxValue * size.height)
                    drawRect(
                        color = borderColor,
                        style = Stroke(width = 1f)
                    )
                    drawLine(
                        color = Color.Red,
                        start = Offset(x = 0f, y = cutoffHeight),
                        end = Offset(x = size.width, y = cutoffHeight),
                        strokeWidth = 3f
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { item ->
            val color = if (item > cutoff) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
            Bar(
                value = item,
                color = color,
                maxHeight = maxHeight,
                maxValue = maxValue
            )
        }
    }
}


@Composable
private fun RowScope.Bar(
    value: Float,
    color: Color,
    maxHeight: Dp,
    maxValue: Float
) {
    val itemHeight = remember(value) { value / maxValue * maxHeight.value }

    Column(modifier = Modifier.weight(1f)) {
        Spacer(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(itemHeight.dp)
                .background(color)
                .fillMaxWidth()
        )
    }
}


@PreviewLightDark
@Composable
fun PreviewBarChart() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BarChart(
                values = (0..10).map { (it * 2).toFloat() },  // (1..20).random().toFloat() },
                maxValue = 20f,
                cutoff = 13f
            )
        }
    }
}
