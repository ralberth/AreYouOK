package org.ralberth.areyouok.ui.settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.NavSettingsRow


@Composable
fun SettingsScreen(navController: NavController, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsUI(
        navController,
        uiState.volumePercent,
        uiState.foregroundOnAlerts
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

        NavSettingsRow(
            label = "Alert Volume Level",
            value = volumeDisplay,
            onClickRow = { navController?.navigate("volumesetting") }
        )

        HorizontalDivider()

        NavSettingsRow(
            label = "Foreground on Alerts",
            value = foregroundDisplay,
            onClickRow = { navController?.navigate("foregroundsetting") }
        )

        HorizontalDivider()

        NavSettingsRow(
            label = "Phone Movement",
            value = "Calibrate when phone isn't moving",
            onClickRow = { navController?.navigate("movement") }
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
