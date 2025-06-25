package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.SoundEffects
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme


@Composable
fun VolumeScreen(
    navController: NavController,
    viewModel: RuokViewModel,
    soundEffects: SoundEffects
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VolumeUI(
        navController,
        uiState.volumePercent,
        { println(it); viewModel.updateVolumePercent(it) },
        onPlayReminder = { soundEffects.yellowWarning() },
        onPlayImminent = { soundEffects.redWarning() },
        onPlayEmergency = { soundEffects.timesUpOneShot() },
    )
}


@Composable
fun VolumeUI(
    navController: NavController?,
    volumePercent: Float?,
    onVolumePercentChange: (Float?) -> Unit = {},
    onPlayReminder: () -> Unit = {},
    onPlayImminent: () -> Unit = {},
    onPlayEmergency: () -> Unit = {}
) {
    RuokScaffold(
        navController = navController,
        route = "volumesetting",
        title = "Alert Volume Level",
        description = "By default, all alert sounds are played at the phone's volume level.  You " +
                "can override this below so alerts from R U OK are always loud.  BE CAREFUL: this " +
                "applies when the phone is locked and on Do Not Disturb!"
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            RadioRow(
                isSelected = volumePercent == null,
                label = "Use system volume level",
                { onVolumePercentChange(null) },
                modifier = Modifier.padding(top = 20.dp)
            )
            RadioRow(
                isSelected = volumePercent != null,
                label = "Override system volume for all alerts",
                { onVolumePercentChange(0.5f) }
            )

            Column(
                modifier = Modifier.alpha(if (volumePercent != null) 1f else 0f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 26.dp)
                ) {
                    Text("Volume for all R U OK alerts:")
                }

                Slider(
                    enabled = volumePercent != null,
                    value = if (volumePercent != null) volumePercent else 0f,
                    onValueChange = { onVolumePercentChange(it) },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 11,
                    valueRange = 0f..1f
                )

                Text(
                    text = "Try it out",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.weight(1f))
                    TryItOutButton("reminder", onPlayReminder)
                    Spacer(Modifier.weight(1f))
                    TryItOutButton("imminent", onPlayImminent)
                    Spacer(Modifier.weight(1f))
                    TryItOutButton("emergency", onPlayEmergency)
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun RadioRow(
    isSelected: Boolean,
    label: String,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .selectable(
                selected = isSelected,
                onClick = onSelect,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null   // null recommended for accessibility with screen readers
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}


@Composable
fun TryItOutButton(
    label: String,
    onTryItOut: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = onTryItOut,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.size(70.dp)
        ) {
            Icon(Icons.Filled.Notifications, "alert")
        }
        Text(label)
    }
}


@PreviewLightDark
@Composable
fun VolumeUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VolumeUI(
                null,
                3f
            )
        }
    }
}
