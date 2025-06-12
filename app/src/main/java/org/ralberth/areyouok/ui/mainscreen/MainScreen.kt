package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold


@Composable
fun MainScreen(
    navController: NavController,
    pickNewPhoneNumberCallback: () -> Unit,
    viewModel: MainViewModel
) {
    RuokScaffold() {
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

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            text = "blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah "
        )

        DurationSelectSlider(
            appIsUsable && uiState.countdownStart == null,
            uiState.countdownLength,
            viewModel::updateCountdownLength
        )

        PhoneNumber(
            appIsUsable && uiState.countdownStart == null,
            uiState.phoneName,
            uiState.phoneNumber,
            pickNewPhoneNumberCallback
        )

        //            EnableDisableToggle(
        //                appIsUsable = appIsUsable && uiState.phoneNumber.length > 0,
        //                isEnabled = appIsUsable && uiState.countdownStart != null,
        //                onChange = viewModel::updateEnabled
        //            )


        Button(
            enabled = appIsUsable && uiState.phoneNumber.length > 0,
            onClick = { navController.navigate("countdown") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(50.dp)
        ) {
            Text("Let's Go!")
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


