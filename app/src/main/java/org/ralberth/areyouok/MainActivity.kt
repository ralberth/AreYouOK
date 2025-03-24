package org.ralberth.areyouok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AreYouOkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}


@Composable
fun MainApp() {
    MainScreen(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
//    var running by remember { mutableStateOf(true) }
    var startTime by remember { mutableStateOf(20) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Are You OK?")
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
//            Text("Off (ready to arm)")
            EnableDisableToggle(
                isEnabled = uiState.enabled,
                onChange = viewModel::setEnabled)
            CountdownSelectSlider(startTime, onChange = { startTime = it })
//            if (uiState.enabled)
//                CountdownDisplay(20)
            Spacer(Modifier.weight(1f))
            Button(
                enabled = uiState.enabled,
                onClick = { /* TO DO */ },
            ) {
                Text("Check-in", fontSize = 24.sp)
            }
            Spacer(Modifier.weight(1f))
        }
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
fun CountdownSelectSlider(minutes: Int, onChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(24.dp)) {
        Column() {
            Text("Countdown Delay",fontWeight = FontWeight.Bold)
            Slider(
                value = minutes.toFloat(),
                onValueChange = { onChange(it.toInt()) },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 12,
                valueRange = 5f..60f
            )
            Text("$minutes minutes")
        }
    }
}



//@Composable
//fun CountdownDisplay(maxMins: Int) {
//    var timerValue = remember { mutableStateOf(0L) }
//    val percentLeft = minsLeft.toFloat() / maxMins.toFloat()
//    Row(modifier = Modifier.padding(24.dp)) {
//        Column() {
//            Text("$minsLeft Minutes Until Next Check-In", fontWeight = FontWeight.Bold)
//            LinearProgressIndicator(
//                progress = { percentLeft },
//                modifier = Modifier.fillMaxWidth().height(20.dp)
//            )
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AreYouOkTheme {
        MainScreen()
    }
}