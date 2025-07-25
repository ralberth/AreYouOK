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
import org.ralberth.areyouok.ui.utils.NavSettingsRow


@Composable
fun PermissionsScreen(
    navController: NavController,
    permHelper: PermissionsHelper,
    hasOverlayPermission: () -> Boolean,
    askForOverlayPermission: () -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "permissions",
        title = "Permissions"
    ) {
        var hasSms        by remember { mutableStateOf(permHelper.has(android.Manifest.permission.SEND_SMS)) }
        var hasPhone      by remember { mutableStateOf(permHelper.has(android.Manifest.permission.CALL_PHONE)) }
        var hasContacts   by remember { mutableStateOf(permHelper.has(android.Manifest.permission.READ_CONTACTS)) }

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
                    " your contact on speakerphone if you're in trouble.",
            permission = android.Manifest.permission.CALL_PHONE,
            hasPermission = hasPhone,
            onAskedForPermission = { hasPhone = it }
        )

        HorizontalDivider()

        PermissionsRow(
            label = "read contacts",
            description = "Optional.  When enabled, you can pick your contact from your phone's " +
                    "list of contacts.  If not enabled, you'll have to type in phone numbers " +
                    "manually.",
            permission = android.Manifest.permission.READ_CONTACTS,
            hasPermission = hasContacts,
            onAskedForPermission = { hasContacts = it }
        )

        HorizontalDivider()

        // Oh bother, can't use a remember for this permission since changing the permission is
        // done outside the application.  So, there's no guaranteed recompose or at least it doesn't
        // participate in remember.  So, check every time so we're always accurate.

        val canForeground = hasOverlayPermission()
        NavSettingsRow(
            label = "Permission to bring app to foreground",
            description = "Optional.  When enabled, the app will come to the foreground in front " +
                    "of any other app when the countdown is near the end or ended.  This is so " +
                    "you can reset it or click a button to call your contact if you are in " +
                    "trouble.  You can't turn this on here.  Go to the app's permissions outside " +
                    "the app and turn this on there.",
            value = if (canForeground)
                "😊 Can bring app to foreground"
            else
                "😡 Cannot bring app to foreground",
            rowIsClickable = true,   // can always click to bring up the app info window
            onClickRow = askForOverlayPermission
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
    NavSettingsRow(
        label = "Permission to $label",
        value = if (hasPermission) "$emoji Can $label" else "$emoji Cannot $label",
        description = description,
        rowIsClickable = !hasPermission,
        onClickRow = { launcher.launch(permission) }  // { permissionsHelper.askForPermission(permission = permission) }
    )
}
