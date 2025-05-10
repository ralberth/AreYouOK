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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.ui.mainscreen.MainScreen
import org.ralberth.areyouok.ui.mainscreen.MainViewModel
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    companion object {
        const val CONTACTS_REQUEST_CODE = 12
    }


    @Inject
    lateinit var permHelper: PermissionsHelper

    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permHelper.registerComponentActivity(this)
        permHelper.askForPermission(
            PackageManager.FEATURE_TELEPHONY,
            android.Manifest.permission.SEND_SMS
        )
        setContent {
            AreYouOkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(this)
                }
            }
        }
    }


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


@Composable
fun MainApp(activity: MainActivity) {
    MainScreen(
        activity,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}
