package org.ralberth.areyouok.countdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ralberth.areyouok.navigation.RuokBottomBar
import org.ralberth.areyouok.navigation.RuokNavigationActions
import org.ralberth.areyouok.navigation.RuokTopBar


@Composable
fun CountdownScreen(
    delayMinutes: Int,
    navActions: RuokNavigationActions,
    modifier: Modifier = Modifier,
    viewModel: CountdownViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { RuokTopBar("R U OK?") },
        bottomBar = { RuokBottomBar(navActions) }
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
                onChange = { viewModel.updateEnabled(it, delayMinutes) }
            )
            HorizontalDivider()
            CountdownDisplay(
                uiState.enabled,
                uiState.minsLeft,
                delayMinutes,
                uiState.countdownBarColor
            )
            HorizontalDivider()
            Spacer(Modifier.weight(1f))
            Button(
                enabled = uiState.enabled,
                onClick = { viewModel.checkin(delayMinutes) },
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


//@Preview(showBackground = true)
//@Composable
//fun CountdownScreenPreview() {
//    AreYouOkTheme {
//        CountdownScreen(20, Modifier, CountdownViewModel(null, null))
//    }
//}
