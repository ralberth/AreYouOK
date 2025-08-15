package org.ralberth.areyouok.ui.settings.sound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.soundeffects.SoundEffects
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.RadioButtonRow


val SOUND_STYLES = arrayOf(
    Pair("Default", "Simple sounds; unobtrusive"),
    Pair("Chimes", "Natural, resonant sounds"),
    Pair("Amy", "Human voice prompts"),
    Pair("Retro", "80s arcade and retro sounds")
)


@Composable
fun SoundStyleScreen(
    navController: NavController,
    viewModel: RuokViewModel,
    soundEffects: SoundEffects
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SoundStyleUI(
        navController,
        uiState.soundStyle,
        { viewModel.updateSoundStyle(it) },
        onPlayReminder = { soundEffects.yellowWarning() },
        onPlayImminent = { soundEffects.redWarning() },
        onPlayEmergency = { soundEffects.timesUpOneShot() },
        onPlayNoMovement = { soundEffects.noMovement() },
        onPlayCallIn5Sec = { soundEffects.mvmtCall5Sec() },
        onPlayMovement = { soundEffects.movement() }
    )
}


@Composable
fun SoundStyleUI(
    navController: NavController?,
    soundStyle: String,
    onSoundStyleChange: (String) -> Unit = {},
    onPlayReminder: () -> Unit = {},
    onPlayImminent: () -> Unit = {},
    onPlayEmergency: () -> Unit = {},
    onPlayNoMovement: () -> Unit = {},
    onPlayCallIn5Sec: () -> Unit = {},
    onPlayMovement: () -> Unit = {}
) {
    RuokScaffold(
        navController = navController,
        route = "soundstyle",
        title = "Choose Alert and Alarm Sounds"
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Available Sound Packs",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
            )

            SOUND_STYLES.forEach {
                RadioButtonRow(
                    isSelected = soundStyle == it.first,
                    label = it.first,
                    description = it.second,
                    { onSoundStyleChange(it.first) },
                    modifier = Modifier.padding(start = 20.dp, bottom = 16.dp)
                )
            }

            TryItOut(onPlayReminder, onPlayImminent, onPlayEmergency,
                onPlayNoMovement, onPlayCallIn5Sec, onPlayMovement)
        }
    }
}


@PreviewLightDark
@Composable
fun SoundStyleUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SoundStyleUI(
                null,
                "Default"
            )
        }
    }
}
