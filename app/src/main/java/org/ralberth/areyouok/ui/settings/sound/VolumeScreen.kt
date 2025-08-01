package org.ralberth.areyouok.ui.settings.sound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.soundeffects.SoundEffects
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.CenteredButton
import org.ralberth.areyouok.ui.utils.RadioButtonRow


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
        { viewModel.updateVolumePercent(it) },
        onPlayReminder = { soundEffects.yellowWarning() },
        onPlayImminent = { soundEffects.redWarning() },
        onPlayEmergency = { soundEffects.timesUpOneShot() },
        onDone = { navController.navigateUp() }
    )
}


@Composable
fun VolumeUI(
    navController: NavController?,
    volumePercent: Float?,
    onVolumePercentChange: (Float?) -> Unit = {},
    onPlayReminder: () -> Unit = {},
    onPlayImminent: () -> Unit = {},
    onPlayEmergency: () -> Unit = {},
    onPlayNoMovement: () -> Unit = {},
    onPlayCallIn5Sec: () -> Unit = {},
    onPlayMovement: () -> Unit = {},
    onDone: () -> Unit = {}
) {
    RuokScaffold(
        navController = navController,
        route = "volumesetting",
        title = "Alert Volume Level",
        description = "By default, all alert sounds are played at the phone's volume level.  You " +
                "can override this below so alerts from R U OK are always loud.  BE CAREFUL: this " +
                "might apply when the phone is locked or on Do Not Disturb!"
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            RadioButtonRow(
                isSelected = volumePercent == null,
                label = "Use system volume level",
                onSelect =  { onVolumePercentChange(null) },
                modifier = Modifier.padding(top = 20.dp)
            )
            RadioButtonRow(
                isSelected = volumePercent != null,
                label = "Override system volume",
                onSelect = { onVolumePercentChange(0.5f) }
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
                    value = volumePercent ?: 0f,
                    onValueChange = { onVolumePercentChange(it) },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 9,
                    valueRange = 0f..1f
                )
            }

            TryItOut(onPlayReminder, onPlayImminent, onPlayEmergency,
                onPlayNoMovement, onPlayCallIn5Sec, onPlayMovement)


            CenteredButton(
                label = "OK",
                onClick = onDone,
                modifier = Modifier.padding(top = 36.dp)
            )
        }
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
