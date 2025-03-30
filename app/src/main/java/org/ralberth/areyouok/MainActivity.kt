package org.ralberth.areyouok

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.ralberth.areyouok.navigation.RuokNavGraph
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
                    RuokNavGraph()
                }
            }
        }
    }
}


//@Composable
//fun MainApp() {
//    MainScreen(modifier = Modifier
//        .fillMaxSize()
//        .wrapContentSize(Alignment.Center)
//    )
//}


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