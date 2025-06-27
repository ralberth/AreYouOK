package org.ralberth.areyouok.ui.settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.SettingsRow


@Composable
fun SettingsScreen(navController: NavController, viewModel: RuokViewModel) {
    // We don't update the uiState here.  Other destinations handle this.  We do need to display
    // the right value, so keep a remember here to avoid going back to the viewmodel every time.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var volumeEdit by remember { mutableStateOf(uiState.volumePercent) }
    var foregroundEdit by remember { mutableStateOf(uiState.foregroundOnAlerts) }

    SettingsUI(
        navController,
        volumeEdit,
        foregroundEdit
    )
}


@Composable
fun SettingsUI(
    navController: NavController?,
    volume: Float?,
    foregroundOnAlerts: Boolean
) {
    RuokScaffold(
        navController = navController,
        route = "settings",
        title = "Settings"
    ) {
        val volumeDisplay = if (volume == null)
            "Use system volume"
        else
            "Override volume to ${(volume * 100).toInt()}% of max"

        val foregroundDisplay = if (foregroundOnAlerts)
            "Foreground at 1min and 0min left"
        else
            "Only use notification banners"

        SettingsRow(
            label = "Alert Volume Level",
            value = volumeDisplay,
            onEdit = { navController?.navigate("volumesetting") }
        )

        HorizontalDivider()

        SettingsRow(
            label = "Foreground on Alerts",
            value = foregroundDisplay,
            onEdit = { navController?.navigate("foregroundsetting") }
        )
    }
}


@PreviewLightDark
@Composable
fun SettingsUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsUI(
                null,
                4.5f,
                false
            )
        }
    }
}
