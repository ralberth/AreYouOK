package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.permissions.PermissionsHelper
import org.ralberth.areyouok.ui.utils.ErrorStripe
import org.ralberth.areyouok.ui.utils.NavSettingsRow
import org.ralberth.areyouok.ui.utils.ToggleSettingsRow


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: RuokViewModel,
    perms: PermissionsHelper,
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
        val hasEnoughPermsToRun =
            perms.has(android.Manifest.permission.SEND_SMS)  // &&
//            perms.has(android.Manifest.permission.POST_NOTIFICATIONS) &&  // will need this in T
//            perms.has(android.Manifest.permission.SCHEDULE_EXACT_ALARM)   // will need this in S

        val toggleEnabled =
            uiState.countdownStart != null ||   // if already running, can definitely turn it off!
                (
                        uiState.phoneNumber.isNotBlank() &&
                        hasEnoughPermsToRun
                )

        NavSettingsRow(
            leftIcon = Icons.Filled.Refresh,
            label = "Countdown Length",
            value = "${uiState.countdownLength} minutes",
            onClickRow = { navController.navigate("durationselect") }
        )

        HorizontalDivider()

        val phoneValue = when {
            uiState.phoneName.isEmpty() && uiState.phoneNumber.isEmpty() -> "Pick a contact"
            uiState.phoneName.isEmpty() -> uiState.phoneNumber   // uiState.phoneNumber.length guaranteed > 0 at this point
            else -> "${uiState.phoneName}   ${uiState.phoneNumber}"
        }
        NavSettingsRow(
            leftIcon = Icons.Filled.Face,
            label = "Contact",
            value = phoneValue,
            onClickRow = { askForContactPhoneNumber() }
        )

        HorizontalDivider()

        NavSettingsRow(
            leftIcon = Icons.Filled.Home,
            label = "Location",
            value = if (uiState.location == "") "Pick a location" else uiState.location,
            onClickRow = { navController.navigate("locationselect") }
        )

        HorizontalDivider()

        val movementValue = if (uiState.alarmOnNoMovement)
            "Alarm if phone stays still"
        else
            "Ignore phone movement"
        ToggleSettingsRow(
            leftIcon = Icons.Filled.AccountCircle,
            label = "Movement",
            value = movementValue,
            isSwitchedOn = uiState.alarmOnNoMovement,
            onToggle = { viewModel.updateAlarmOnNoMovement(it) }
        )

        HorizontalDivider()

        ToggleSettingsRow(
            label = "Enable",
            toggleEnabled = toggleEnabled,
            isSwitchedOn = uiState.isCountingDown(),
            onToggle = { viewModel.updateEnabled(it) }
        )

        ErrorStripe(
            shouldDisplay = !hasEnoughPermsToRun,
            message = "Need to enable permissions before starting"
        )
        ErrorStripe(
            shouldDisplay = uiState.phoneNumber.isBlank(),
            "Pick a contact or enter a phone number before starting"
        )
    }
}
