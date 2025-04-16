package org.ralberth.areyouok

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton


// Request runtime permissions: https://developer.android.com/training/permissions/requesting#principles


@Singleton
class PermissionsHelper @Inject constructor(
    @ApplicationContext private val context: Context,
//    private val activity: MainActivity // -- not sure I can wire this up here.  If not, use registerComponentActivity() below in MyApplication
) {
    private var activity: ComponentActivity? = null
    private val permissionsMap: MutableMap<String, Boolean> = mutableMapOf()

    fun registerComponentActivity(mainActivity: ComponentActivity) {
        this.activity = mainActivity
    }

    fun askForPermission(feature: String, permission: String) {
        val mapKey: String = "$feature|$permission"
        println("ASK for permission $mapKey")

        // STEP 1: does the phone have the feature?
        if (! context.packageManager.hasSystemFeature(feature)) {
            println("Device doesn't have $feature")
            permissionsMap.put(mapKey, false)
            return
        }

        // STEP 2: Do we already have the permissions we need?
        val hasPerm: Int = ContextCompat.checkSelfPermission(context, permission)
        if (hasPerm == PERMISSION_GRANTED) {
            println("Already have permission $permission")
            permissionsMap.put(mapKey, true)
            return
        }

        // STEP 3: Ask the user to grant us permissions, then get the permissions to act
        val launcher =
            activity!!.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                isGranted: Boolean ->
                    permissionsMap.put(mapKey, isGranted)
                    println("ASKED for perm $mapKey, result $isGranted")
            }
        launcher.launch(permission)   // This runs the callback to launcher above
    }


    fun guard(
        feature: String,       // like PackageManager.FEATURE_TELEPHONY
        permission: String,    // like android.Manifest.permission.SEND_SMS
        success: () -> Unit,   // call this if you have/get permission
        fallback: () -> Unit   // call this if you don't
    ) {
        val mapKey = "$feature|$permission"
        val hasPerms: Boolean? = permissionsMap[mapKey]
        if (hasPerms != null && hasPerms)
            success()
        else
            fallback()
    }
}
