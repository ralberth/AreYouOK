package org.ralberth.areyouok.ui.settings.sound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TryItOut(
    onPlayReminder: () -> Unit,
    onPlayImminent: () -> Unit,
    onPlayEmergency: () -> Unit,
    onPlayNoMovement: () -> Unit,
    onPlayCallIn5Sec: () -> Unit,
    onPlayMovement: () -> Unit
) {
    Column {
        Text(
            text = "Try it out",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TryItOutButton("reminder", onPlayReminder)
                TryItOutButton("still", onPlayNoMovement)
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TryItOutButton("imminent", onPlayImminent)
                TryItOutButton("5sec still", onPlayCallIn5Sec)
            }
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TryItOutButton("emergency", onPlayEmergency)
                TryItOutButton("movement", onPlayMovement)
            }
            Spacer(Modifier.weight(1f))
        }

    }
}


@Composable
fun TryItOutButton(
    label: String,
    onTryItOut: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 18.dp)
    ) {
        FilledTonalButton(
            onClick = onTryItOut,
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            Icon(Icons.Filled.Notifications, "alert")
        }
        Text(label)
    }
}
