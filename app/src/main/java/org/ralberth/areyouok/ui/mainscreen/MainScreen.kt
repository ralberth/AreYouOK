package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BottomAppBarDefaults.ContentPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold


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
        description = "Turn on and your phone will text your point of contact if you don't check-in every period."
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val appIsUsable = viewModel.hasAlarmPermission && viewModel.hasNotifyPermission

        if (!viewModel.hasAlarmPermission) {
            NeedPermissionBanner("alarms (set timers)")
            HorizontalDivider()
        }

        if (!viewModel.hasNotifyPermission) {
            NeedPermissionBanner("notifications")
            HorizontalDivider()
        }

        SummaryRow(
            label = "Duration",
            onEdit = { navController.navigate("durationselect") }
        ) {
            Text("${uiState.countdownLength} mins")
        }

        SummaryRow(
            label = "Contact",
            onEdit = { askForContactPhoneNumber() }
        ) {
            when {
                uiState.phoneName.length == 0 && uiState.phoneNumber.length == 0 -> Text("Pick a contact")
                uiState.phoneName.length == 0 -> Text(uiState.phoneNumber)   // uiState.phoneNumber.length guaranteed > 0 at this point
                else -> Text(uiState.phoneName)
            }
        }

        SummaryRow(
            label = "Location",
            onEdit = { navController.navigate("locationselect") }
        ) {
            Text(if (uiState.location == "") "Pick a location" else uiState.location)
        }

        SummaryRow("Enable") {
            Switch(
                enabled = appIsUsable && uiState.phoneNumber.length > 0,
                checked = uiState.countdownStart != null,
                onCheckedChange = {
                    viewModel.updateEnabled(it)
//                    navController.navigate("countdown")
                }
            )
        }
    }
}


@Composable
fun NeedPermissionBanner(type: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Need $type permission",
            color = Color.Red
        )
    }
}


@Composable
fun EditButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            Icons.Filled.Edit,
            "Change")
    }
}


@Composable
fun SummaryRow(
    label: String,
    onEdit: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 18.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        content()
        if (onEdit != null)
            EditButton(enabled = true/* FIXME */, onClick = onEdit)
    }
}


//@Composable
//fun EnableDisableToggle(appIsUsable: Boolean, isEnabled: Boolean, onChange: (Boolean) -> Unit) {
//    Row(
//        modifier = Modifier.padding(18.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text("Enable", fontWeight = FontWeight.Bold)
//        Spacer(Modifier.weight(1f))
//        Switch(
//            enabled = appIsUsable,
//            checked=isEnabled,
//            onCheckedChange=onChange
//        )
//    }
//}
