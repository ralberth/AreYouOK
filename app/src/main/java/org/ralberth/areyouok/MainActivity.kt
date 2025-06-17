package org.ralberth.areyouok

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.ui.mainscreen.CountdownScreen
import org.ralberth.areyouok.ui.mainscreen.MainScreen
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.callcontactscreen.CallContactScreen
import org.ralberth.areyouok.ui.settings.DurationSelectScreen
import org.ralberth.areyouok.ui.settings.LocationScreen
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    companion object {
        const val CONTACTS_REQUEST_CODE = 12
    }


    @Inject
    lateinit var permHelper: PermissionsHelper

    val viewModel: RuokViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permHelper.registerComponentActivity(this)
        permHelper.askForPermission(
            PackageManager.FEATURE_TELEPHONY,
            android.Manifest.permission.SEND_SMS
        )
        permHelper.askForPermission(
            PackageManager.FEATURE_TELEPHONY,
            android.Manifest.permission.CALL_PHONE
        )
        setContent {
            AreYouOkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { MainScreen(navController, viewModel, { askForContactPhoneNumber() }) }
                        composable("durationselect") { DurationSelectScreen(navController, viewModel) }
                        composable("locationselect") { LocationScreen(navController, viewModel) }
                        composable("countdown") { CountdownScreen(navController, viewModel) }
                        composable("callcontact") { CallContactScreen(navController, viewModel) }
                    }
                }
            }
        }
    }


//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        if (intent != null) {
//            when (intent.getStringExtra(EXTRA_KEY_MSGTYPE)) {
//                EXTRA_VAL_MSGTYPE_TXTMSG -> handleTxtmsgIntent(intent)
//            }
//        }
//    }
//
//
//    private fun handleTxtmsgIntent(intent: Intent) {
//        println("Result of txtmsg: ${intent.}")
////        Toast stuff!
//    }


    fun askForContactPhoneNumber() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
        ActivityCompat.startActivityForResult(this, intent, CONTACTS_REQUEST_CODE, null);
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("Recycle", "MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        val uri: Uri? = intent?.getData()
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
}
