package org.ralberth.areyouok

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.RuokIntents.Companion.EXTRA_KEY_MSGTYPE
import org.ralberth.areyouok.RuokIntents.Companion.EXTRA_VAL_MSGTYPE_RUOKUI
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.callcontactscreen.CallContactScreen
import org.ralberth.areyouok.ui.mainscreen.CountdownScreen
import org.ralberth.areyouok.ui.mainscreen.MainScreen
import org.ralberth.areyouok.ui.permissions.PermissionsHelper
import org.ralberth.areyouok.ui.permissions.PermissionsScreen
import org.ralberth.areyouok.ui.settings.DurationSelectScreen
import org.ralberth.areyouok.ui.settings.ForegroundScreen
import org.ralberth.areyouok.ui.settings.LocationScreen
import org.ralberth.areyouok.ui.settings.SettingsScreen
import org.ralberth.areyouok.ui.settings.VolumeScreen
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    companion object {
        // These codes are sent in Intents as RequestCode values and inspected in this
        // Activity's onResultCode().
        const val CONTACTS_REQUEST_CODE = 12  // sent back by Contacts when user picks a person
        const val OVERLAY_REQUEST_CODE  = 13  // sent back when user changes window overlay mode
    }


    @Inject
    lateinit var permHelper: PermissionsHelper

    @Inject
    lateinit var soundEffects: SoundEffects


    val viewModel: RuokViewModel by viewModels()
    lateinit var navController: NavHostController  // created in onCreate(), needed in onNewIntent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permHelper.registerComponentActivity(this)

        setContent {
            AreYouOkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // If the countdown is running, go straight to the countdown screen
                    // TODO: if the user hasn't met the setup requirements, go there instead of main if the countdown isn't running
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val firstDestination = if (uiState.isCountingDown()) "countdown" else "main"

                    navController = rememberNavController()
                    NavHost(navController = navController, startDestination = firstDestination) {
                        composable("permissions") { PermissionsScreen(navController, permHelper, { hasOverlayPermission() }, { askForOverlayPermission() }) }
                        composable("main") { MainScreen(navController, viewModel, permHelper, { askForContactPhoneNumber() }) }
//                        composable("help") { HelpScreen(navController) }
                        composable("durationselect") { DurationSelectScreen(navController, viewModel) }
                        composable("locationselect") { LocationScreen(navController, viewModel) }
                        composable("countdown") { CountdownScreen(navController, viewModel) }
                        composable("callcontact") { CallContactScreen(navController, viewModel) }
                        composable("settings") { SettingsScreen(navController, viewModel) }
                        composable("volumesetting") { VolumeScreen(navController, viewModel, soundEffects) }
                        composable("foregroundsetting") { ForegroundScreen(navController, permHelper, viewModel) }
                    }
                }
            }
        }
    }


    /*
     * Called when a new Intent is delivered to MainActivity (us).  Our manifest has us SINGLE_TOP
     * so new intents don't create new MainActivity instances, they call this method instead.
     *
     * See createRuokUiPendingIntent() --- we're handling an intent from the user clicking a
     * banner notification.  If this is the intent we're getting, the app is coming to the
     * foreground on its own.  We just need to change the destination so the countdown screen is
     * visible.
     *
     * If we get anything else, print an error.
     */
    override fun onNewIntent(i: Intent) {
        println("onNewIntent()")
        super.onNewIntent(i)
        val msgType = i.getStringExtra(EXTRA_KEY_MSGTYPE)
        if (msgType == EXTRA_VAL_MSGTYPE_RUOKUI)
            navController.navigate("countdown")
    }


    fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this.applicationContext)
    }


    fun askForOverlayPermission() {
        val packageName = this.applicationContext.packageName
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:$packageName".toUri()
        )
        ActivityCompat.startActivityForResult(this, intent, OVERLAY_REQUEST_CODE, null)
    }


    fun askForContactPhoneNumber() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
        ActivityCompat.startActivityForResult(this, intent, CONTACTS_REQUEST_CODE, null);
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("Recycle", "MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            CONTACTS_REQUEST_CODE -> onPickedNewContact(intent?.data)
            OVERLAY_REQUEST_CODE -> onChangedOverlayPermission()
            else -> println("Got unknown requestCode=$requestCode in onActivityResult().  Ignoring it.")
        }
    }


    fun onPickedNewContact(uri: Uri?) {
        if (uri != null) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val phoneIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phoneNumber = cursor.getString(phoneIndex)
                    val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val phoneName = cursor.getString(nameIndex)
                    viewModel.updatePhoneNumber(phoneName, phoneNumber)
                }

                cursor.close()
            }
        }
    }


    fun onChangedOverlayPermission() {
        // also causes a recompose so it re-evaluates if we have this permission and displays correctly
        navController.navigate("permissions")
    }
}
