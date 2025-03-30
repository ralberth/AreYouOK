package org.ralberth.areyouok

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ralberth.areyouok.countdown.DurationSelectSlider
import org.ralberth.areyouok.countdown.SettingsScreen
import org.ralberth.areyouok.settings.SettingsViewModel
import org.ralberth.areyouok.ui.theme.AreYouOkTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuokTopBar(title: String) {
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(title)
        }
    )
}


@Composable
fun RuokBottomBar(
    onCountdown: () -> Unit,
    onSettings: () -> Unit,
    onMessages: () -> Unit
) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(18.dp, 0.dp)
                ) {
                    IconButton(onClick = onCountdown) {
                        Icon(Icons.Filled.Refresh, "Countdown")
                    }
                    Text(
                        "Countdown",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(18.dp, 0.dp)
                ) {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Filled.Settings, "Settings")
                    }
                    Text(
                        "Settings",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(18.dp, 0.dp)
                ) {
                    IconButton(onClick = onMessages) {
                        Icon(Icons.AutoMirrored.Filled.List, "Messages")
                    }
                    Text(
                        "Messages",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun NavBarsPreview() {
    AreYouOkTheme {
        Scaffold(
            topBar = { RuokTopBar("Preview") },
            bottomBar = { RuokBottomBar({}, {}, {}) }
        ) { innerPadding ->
            Text(
                "Hithere",
                modifier = Modifier.padding(innerPadding))
        }
    }
}
