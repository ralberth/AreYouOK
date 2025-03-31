package org.ralberth.areyouok.countdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ralberth.areyouok.navigation.RuokNavigationActions
import org.ralberth.areyouok.navigation.RuokTopBar
import org.ralberth.areyouok.navigation.RuokBottomBar
import org.ralberth.areyouok.settings.SettingsViewModel


@Composable
fun SettingsScreen(
//    navActions: RuokNavigationActions,
//    modifier: Modifier = Modifier,
//    viewModel: SettingsViewModel = viewModel()
    delayMinutes: Int,
    onUpdateDelayMinutes: (Int) -> Unit
) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DurationSelectSlider(
        delayMinutes,
        onUpdateDelayMinutes
    )
}


@Composable
fun DurationSelectSlider(minutes: Int, onChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(24.dp)) {
        Column {
            Text("Countdown Delay",fontWeight = FontWeight.Bold)
            Slider(
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
            Text("$minutes minutes")
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    AreYouOkTheme {
//        SettingsScreen(Modifier, SettingsViewModel())
//    }
//}
