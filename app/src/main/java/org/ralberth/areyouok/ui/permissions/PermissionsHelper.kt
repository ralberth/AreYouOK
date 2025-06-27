package org.ralberth.areyouok.ui.permissions

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


// Request runtime permissions: https://developer.android.com/training/permissions/requesting#principles


@Singleton
class PermissionsHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var activity: ComponentActivity? = null

    fun registerComponentActivity(mainActivity: ComponentActivity) {
        this.activity = mainActivity
    }

    fun has(permission: String): Boolean {
        val resp = ContextCompat.checkSelfPermission(context, permission)
        return resp == PERMISSION_GRANTED
    }


    fun askForPermission(feature: String? = null, permission: String) {
        val mapKey = if (feature != null) "$feature|$permission" else permission
        println("ASK for permission $mapKey")

        // STEP 1: does the phone have the feature?
        if (feature != null) {
            if (!context.packageManager.hasSystemFeature(feature)) {
                println("Device doesn't have $feature")
                return
            }
        }

        // STEP 2: Do we already have the permissions we need?
        val hasPerm: Int = ContextCompat.checkSelfPermission(context, permission)
        if (hasPerm == PERMISSION_GRANTED) {
            println("Already have permission $permission")
            return
        }

        // STEP 3: Ask the user to grant us permissions, then get the permissions to act
        val launcher =
            activity!!.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                isGranted: Boolean ->
                    println("ASKED for perm $mapKey, result $isGranted")
            }
        launcher.launch(permission)   // This runs the callback to launcher above
    }


    fun guard(
        permission: String,    // like android.Manifest.permission.SEND_SMS
        success: () -> Unit,   // call this if you have/get permission
        fallback: () -> Unit   // call this if you don't
    ) {
        val hasPerm: Int = ContextCompat.checkSelfPermission(context, permission)
        if (hasPerm == PERMISSION_GRANTED)
            success()
        else
            fallback()
    }
}
