package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.SettingsRow


@Composable
fun SettingsScreen(navController: NavController, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var volumeEdit by remember { mutableStateOf(uiState.volumePercent) }

    SettingsUI(
        navController,
        volumeEdit
    )
}


@Composable
fun SettingsUI(
    navController: NavController?,
    volume: Float?
) {
    RuokScaffold(
        navController = navController,
        route = "settings",
        title = "Settings"
    ) {
        val volumeDisplay = if (volume == null)
            "Use system volume"
        else
            "Override system volume to ${(volume * 100).toInt()}%"

        SettingsRow(
            label = "Alert Volume Level",
            value = volumeDisplay,
            onEdit = { navController?.navigate("volumesetting") }
        )

        HorizontalDivider()

        SettingsRow(
            label = "Foreground on Alerts",
            value = "Off: display can sleep, lock, and turn off",
            onEdit = { }
        )

        HorizontalDivider()

        SettingsRow(
            label = "Something Complicated",
            value = "Disabled",
            onEdit = { }
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
                4.5f
            )
        }
    }
}
