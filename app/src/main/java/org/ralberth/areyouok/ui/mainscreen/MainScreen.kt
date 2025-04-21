package org.ralberth.areyouok.ui.mainscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("R U OK ?")
                }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxHeight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val appIsUsable = viewModel.hasAlarmPermission && viewModel.hasNotifyPermission

            if (! viewModel.hasAlarmPermission) {
                NeedPermissionBanner("alarms (set timers)")  // onFixit = { viewModel.fixMissingAlarmsPermission() })
                HorizontalDivider()
            }

            if (! viewModel.hasNotifyPermission) {
                NeedPermissionBanner("notifications")
                HorizontalDivider()
            }

            StatusDisplayText(uiState.message, uiState.statusColor)
            HorizontalDivider()
            EnableDisableToggle(
                appIsUsable = appIsUsable,
                isEnabled = appIsUsable && uiState.whenEnabled != null,
                onChange = viewModel::updateEnabled
            )
            HorizontalDivider()
            CountdownSelectSlider(
                appIsUsable && uiState.whenEnabled == null,
                uiState.delayMins,
                viewModel::updateDelayMins
            )
            HorizontalDivider()
            CountdownDisplay(
                appIsUsable && uiState.whenEnabled != null,
                uiState.minsLeft,
                uiState.delayMins,
                uiState.countdownBarColor
            )
            HorizontalDivider()
            Spacer(Modifier.weight(1f))
            Button(
                enabled = appIsUsable && uiState.whenEnabled != null,
                onClick = viewModel::checkin,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(100.dp)
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    "Countdown"
                )
                Text(
                    "  Check-in",
                    fontSize = 24.sp
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
fun NeedPermissionBanner(type: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Need ${type} permission",
            color = Color.Red
        )
    }
}


@Composable
fun StatusDisplayText(message: String, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            message,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun EnableDisableToggle(appIsUsable: Boolean, isEnabled: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Enable", fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Switch(
            enabled = appIsUsable,
            checked=isEnabled,
            onCheckedChange=onChange
        )
    }
}


@Composable
fun CountdownSelectSlider(enabled: Boolean, minutes: Int, onChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(18.dp)) {
        Column {
            Text("Countdown Delay",fontWeight = FontWeight.Bold)
            Slider(
                enabled = enabled,
                value = minutes.toFloat(),
                onValueChange = { onChange(it.toInt()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 10,
                valueRange = 5f..60f
            )
            Text("$minutes minutes")
        }
    }
}



@Composable
fun CountdownDisplay(isEnabled: Boolean, minsLeft: Int, delayMins: Int, barColor: Color) {
    val percentLeft = if (isEnabled) minsLeft.toFloat() / delayMins.toFloat() else 0F
    val message     = if (isEnabled) "$minsLeft Minutes Until Next Check-In"  else ""

    Row(modifier = Modifier.padding(18.dp)) {
        Column {
            Text(message, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { percentLeft },
                color = barColor,
                modifier = Modifier.fillMaxWidth().height(20.dp)
            )
        }
    }
}
