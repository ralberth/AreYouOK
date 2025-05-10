package org.ralberth.areyouok.ui.mainscreen

import android.app.Activity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import org.ralberth.areyouok.MainActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    activity: MainActivity,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    RuokMasthead()
                }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val animatedAlpha by animateFloatAsState(
            targetValue = if (uiState.countdownStop != null) 1.0f else 0f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            label = "alpha"
        )

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxHeight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val appIsUsable = viewModel.hasAlarmPermission && viewModel.hasNotifyPermission

            if (! viewModel.hasAlarmPermission) {
                NeedPermissionBanner("alarms (set timers)")
                HorizontalDivider()
            }

            if (! viewModel.hasNotifyPermission) {
                NeedPermissionBanner("notifications")
                HorizontalDivider()
            }

            CountdownSelectSlider(
                appIsUsable && uiState.countdownStart == null,
                uiState.countdownLength,
                viewModel::updateCountdownLength
            )

            PhoneNumber(
                appIsUsable && uiState.countdownStart == null,
                uiState.phoneName,
                uiState.phoneNumber,
                activity::askForContactPhoneNumber
            )

            EnableDisableToggle(
                appIsUsable = appIsUsable && uiState.phoneNumber.length > 0,
                isEnabled = appIsUsable && uiState.countdownStart != null,
                onChange = viewModel::updateEnabled
            )


            StatusDisplayText(
                uiState.countdownStop,
                modifier = Modifier.graphicsLayer { alpha = animatedAlpha }
            )

            CountdownDisplay(
                uiState.countdownStart,
                uiState.countdownStop,
                modifier = Modifier.graphicsLayer { alpha = animatedAlpha }
            )

            Spacer(Modifier.weight(1f).graphicsLayer { alpha = animatedAlpha })

            Button(
                enabled = appIsUsable && uiState.countdownStart != null,
                onClick = viewModel::checkin,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(100.dp).graphicsLayer { alpha = animatedAlpha }
            ) {
                Icon(
                    Icons.Filled.Refresh,
                    "Countdown"
                )
                Text(
                    " Check-in",
                    fontSize = 24.sp
                )
            }

            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
fun PhoneNumber(enabled: Boolean, name: String, number: String, onChangeButtonPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Phone number", fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))

        when {
            name.length == 0 && number.length == 0 -> Text("Pick a contact")
            name.length == 0 -> Text(number)   // number.length guaranteed > 0 at this point
            else -> Column { Text(name); Text(number) }
        }

        TextButton(
            enabled = enabled,
            onClick = onChangeButtonPressed
        ) {
            Icon(Icons.Filled.Edit, "Change")
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
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Countdown Delay:   ")
                    }
                    append("$minutes minutes")
                }
            )
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
        }
    }
}
