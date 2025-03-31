package org.ralberth.areyouok

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.countdown.CountdownScreen
import org.ralberth.areyouok.countdown.SettingsScreen
import org.ralberth.areyouok.messages.MessagesScreen
import org.ralberth.areyouok.navigation.RuokBottomBar
import org.ralberth.areyouok.navigation.RuokNavGraph
import org.ralberth.areyouok.navigation.RuokTopBar
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject
    lateinit var permHelper: PermissionsHelper

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
//                    RuokNavGraph()
                    MainApp()
                }
            }
        }
    }
}


@Composable
fun MainApp() {
    val visibleScreen = remember { mutableStateOf("countdown") }
    val delayMinutes = remember { mutableIntStateOf(20) }
    val messages = remember { mutableStateOf(listOf()) }

    Scaffold(
        topBar = { RuokTopBar("R U OK?") },
        bottomBar = { RuokBottomBar({ visibleScreen.value = it }) },
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (visibleScreen.value) {
                "countdown" -> CountdownScreen(
                    delayMinutes.intValue,
                    { messages.value.add(it) }
                )
                "settings" -> SettingsScreen(delayMinutes.intValue, { delayMinutes.intValue = it })
                "messages" -> MessagesScreen()
            }
        }
    }
}


/*
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
            StatusDisplayText(uiState.message, uiState.statusColor)
            HorizontalDivider()
            EnableDisableToggle(
                isEnabled = uiState.enabled,
                onChange = viewModel::updateEnabled
            )
            HorizontalDivider()
            CountdownSelectSlider(
                !uiState.enabled,
                uiState.delayMins,
                viewModel::updateDelayMins
            )
            HorizontalDivider()
            CountdownDisplay(
                uiState.enabled,
                uiState.minsLeft,
                uiState.delayMins,
                uiState.countdownBarColor
            )
            HorizontalDivider()
            Button(
                enabled = uiState.enabled,
                onClick = viewModel::checkin,
                modifier = Modifier.padding(24.dp)
            ) {
                Text("Check-in", fontSize = 24.sp)
            }
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxHeight().align(Alignment.Start).padding(5.dp)
            ) {
                items(uiState.messages) { message ->
                    val ts = logTimeFormatter.format(message.logTime)
                    Text(
                        "$ts: ${message.message}",
                        color = message.color,
                        fontFamily = FontFamily.Monospace,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun StatusDisplayText(message: String, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 36.dp),
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
fun EnableDisableToggle(isEnabled: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Enable", fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Switch(checked=isEnabled, onCheckedChange=onChange)
    }
}


@Composable
fun CountdownSelectSlider(enabled: Boolean, minutes: Int, onChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(24.dp)) {
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

    Row(modifier = Modifier.padding(24.dp)) {
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
*/

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    AreYouOkTheme {
//        MainScreen(Modifier, MainViewModel(SoundEffectsStub()))
//    }
//}