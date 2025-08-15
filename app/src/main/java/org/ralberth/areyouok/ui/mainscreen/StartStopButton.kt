package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun StartStopButton(
    enabled: Boolean,
    showStop: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val icon  = if (showStop) Icons.Outlined.Close else Icons.Outlined.PlayArrow
    val label = if (showStop) " Stop" else " Start"

    Button(
        enabled = enabled,
        onClick = { onClick(!showStop) },
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Start/Stop"
        )
        Text(label)
    }
}
