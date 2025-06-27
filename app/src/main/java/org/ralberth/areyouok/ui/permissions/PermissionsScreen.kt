package org.ralberth.areyouok.ui.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
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

        PermissionsRow(
            label = "send TXT messages",
            description = "Required for the app to work correctly.  When the countdown timer " +
                    "ends the app needs to tell your contact that you're in trouble, or call them.",
            permission = android.Manifest.permission.SEND_SMS,
            hasPermission = hasSms,
            onAskedForPermission = { hasSms = it }
        )

        HorizontalDivider()

        PermissionsRow(
            label = "make phone calls",
            description = "Optional.  When enabled, you can click a button and the app will call" +
                    "your contact on speakerphone if you're in trouble.",
            permission = android.Manifest.permission.CALL_PHONE,
            hasPermission = hasPhone,
            onAskedForPermission = { hasPhone = it }
        )

        HorizontalDivider()

        PermissionsRow(
            label = "read contacts",
            description = "Optional.  When enabled, you can pick your contact from your phone's " +
                    "list of contacts.  If not enabled, you'll have to type in phone numbers" +
                    "manually.",
            permission = android.Manifest.permission.READ_CONTACTS,
            hasPermission = hasContacts,
            onAskedForPermission = { hasContacts = it }
        )

        HorizontalDivider()

        PermissionsRow(
            label = "bring app to foreground",
            description = "Optional.  When enabled, the app will come to the foreground in front " +
                    "of any other app when the countdown is near the end or ended.  This is so " +
                    "you can reset it or click a button to call your contact if you are in " +
                    "trouble.  You can't turn this on here.  Go to the app's permissions outside " +
                    "the app and turn this on there.",
            permission = android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            hasPermission = true,    // prevents the thing from being clickable
            onAskedForPermission = { }
        )
    }
}


@Composable
fun PermissionsRow(
    label: String,
    permission: String,
    description: String,
    hasPermission: Boolean,
    onAskedForPermission: (hasPerm: Boolean) -> Unit
) {
    // Dunno...see for details:
    // https://stackoverflow.com/questions/64476827/how-to-resolve-the-error-lifecycleowners-must-call-register-before-they-are-sta
    // TODO: integrate this with PermissionsHelper?
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        isGranted ->
            println("Handle result! $isGranted")
            onAskedForPermission(isGranted)
    }

    val emoji = if (hasPermission) "😊" else "😡"
    SettingsRow(
        label = "Permission to $label",
        value = if (hasPermission) "$emoji Can $label" else "$emoji Cannot $label",
        description = description,
        canEdit = !hasPermission,
        onEdit = { launcher.launch(permission) }  // { permissionsHelper.askForPermission(permission = permission) }
    )
}
