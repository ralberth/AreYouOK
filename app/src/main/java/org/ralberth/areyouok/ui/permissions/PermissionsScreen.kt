package org.ralberth.areyouok.ui.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.utils.SettingsRow


@Composable
fun PermissionsScreen(
    navController: NavController,
    permissionsHelper: PermissionsHelper
) {
    RuokScaffold(
        navController = navController,
        route = "permissions",
        title = "Permissions"
    ) {
        var hasSms      by remember { mutableStateOf(permissionsHelper.has(android.Manifest.permission.SEND_SMS)) }
        var hasPhone    by remember { mutableStateOf(permissionsHelper.has(android.Manifest.permission.CALL_PHONE)) }
        var hasContacts by remember { mutableStateOf(permissionsHelper.has(android.Manifest.permission.READ_CONTACTS)) }
        var hasAlertWin by remember { mutableStateOf(permissionsHelper.has(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) }

        PermissionsRow(
            "send TXT messages",
            android.Manifest.permission.SEND_SMS,
            hasSms,
            { hasSms = it }
        )

        HorizontalDivider()

        PermissionsRow(
            "make phone calls",
            android.Manifest.permission.CALL_PHONE,
            hasPhone,
            { hasPhone = it }
        )

        HorizontalDivider()

        PermissionsRow(
            "read contacts",
            android.Manifest.permission.READ_CONTACTS,
            hasContacts,
            { hasContacts = it }
        )

        HorizontalDivider()

        PermissionsRow(
            "bring app to foreground",
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            hasAlertWin,
            { hasAlertWin = it }
        )
    }
}


@Composable
fun PermissionsRow(
    label: String,
    permission: String,
    hasPermission: Boolean,
    onAskedForPermission: (hasPerm: Boolean) -> Unit
) {
    // Dunno...see for details:
    // https://stackoverflow.com/questions/64476827/how-to-resolve-the-error-lifecycleowners-must-call-register-before-they-are-sta
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        isGranted ->
            println("Handle result! $isGranted")
            onAskedForPermission(isGranted)
    }

    val emoji = if (hasPermission) "😊" else "😡"
    SettingsRow(
        label = "Permission to $label",
        value = if (hasPermission) "$emoji Can $label" else "$emoji Cannot $label",
        canEdit = !hasPermission,
        onEdit = { launcher.launch(permission) }  // { permissionsHelper.askForPermission(permission = permission) }
    )
}


//@PreviewLightDark
//@Composable
//fun SettingsUIPreview() {
//    AreYouOkTheme {
//        Surface(color = MaterialTheme.colorScheme.background) {
//            PermissionsUI(
//                null,
//                4.5f
//            )
//        }
//    }
//}
