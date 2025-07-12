package org.ralberth.areyouok.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewLightDark
import org.ralberth.areyouok.ui.theme.AreYouOkTheme

// https://medium.com/better-programming/custon-charts-in-android-using-jetpack-compose-87b395c1d515

@Composable
internal fun BarChart(
    maxHeight: Dp = 200.dp,
    maxValue: Float,
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .drawBehind {
                    drawRect(
                        color = borderColor,
                        style = Stroke(width = 1f)
                    )
                }
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { item ->
            Bar(
                value = item,
                color = MaterialTheme.colorScheme.primary,
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
    val valuePctOfMax = remember(value) { value / maxValue }
    val itemHeight = remember(value) { valuePctOfMax * maxHeight.value }

    Spacer(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .height(itemHeight.dp)
            .weight(1f)
            .background(color)
    )
}


@PreviewLightDark
@Composable
fun PreviewBarChart() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BarChart(
                values = (0..10).map { (1..20).random().toFloat() },
                maxValue = 20f
            )
        }
    }
}
