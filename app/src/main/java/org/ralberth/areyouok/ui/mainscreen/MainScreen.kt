package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.utils.SettingsRow


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: RuokViewModel,
    askForContactPhoneNumber: () -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "main",
        title = "Home",
        showNavigateUp = false,
        description = "Turn on and your phone will text your point of contact if you don't check-in every period."
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        SettingsRow(
            leftIcon = Icons.Filled.Refresh,
            label = "Countdown Length",
            value = "${uiState.countdownLength} minutes",
            onEdit = { navController.navigate("durationselect") }
        )

        HorizontalDivider()

        val phoneValue = when {
            uiState.phoneName.isEmpty() && uiState.phoneNumber.isEmpty() -> "Pick a contact"
            uiState.phoneName.isEmpty() -> uiState.phoneNumber   // uiState.phoneNumber.length guaranteed > 0 at this point
            else -> "${uiState.phoneName}   ${uiState.phoneNumber}"
        }
        SettingsRow(
            leftIcon = Icons.Filled.Face,
            label = "Contact",
            value = phoneValue,
            onEdit = { askForContactPhoneNumber() }
        )

        HorizontalDivider()

        SettingsRow(
            leftIcon = Icons.Filled.Home,
            label = "Location",
            value = if (uiState.location == "") "Pick a location" else uiState.location,
            onEdit = { navController.navigate("locationselect") }
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable")
            Spacer(Modifier.weight(1f))
            Switch(
                enabled = uiState.phoneNumber.isNotEmpty(),
                checked = uiState.isCountingDown(),
                onCheckedChange = { viewModel.updateEnabled(it) }
            )
        }
    }
}
