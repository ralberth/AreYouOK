package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.messaging.AlertSender
import org.ralberth.areyouok.ui.theme.AreYouOkTheme


@Composable
fun LocationScreen(navController: NavController, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LocationUI(
        navController,
        uiState.location,
        uiState.countdownLength,
        { viewModel.updateLocation(it) },
        { navController.navigateUp() }  // change this to call controller.  If we're running, text the contact.
    )
}


@Composable
fun LocationUI(
    navController: NavController?,
    location: String,
    countdownLength: Int,
    onUpdateLocation: (String) -> Unit,
    onDone: () -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "location",
        title = "Set Your Location",
        description = "Where you physically are right now.  Sent to your POC so they know where you are."
    ) {
        TextField(
            value = location,
            onValueChange = onUpdateLocation,
            label = { Text("Location") }
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onDone) {
                Text("OK")
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 18.dp, end = 24.dp)
        ) {
            Text("How messages sent to your contact will look:")
            ChatItemBubble(
                AlertSender.getTurnedOnMessage(
                    countdownLength,
                    location
                )
            )
            ChatItemBubble(AlertSender.getLocationChangedMessage(location))
            ChatItemBubble(AlertSender.missedCheckinMessage(location))
        }
    }
}


@Composable
fun ChatItemBubble(message: String) {
    Column(modifier = Modifier.padding(top = 6.dp) ) {
        Surface(
            color = Color(red = 135, green = 206, blue = 235),   // MaterialTheme.colorScheme.primary,   // or MaterialTheme.colorScheme.surfaceVariant
            shape = RoundedCornerShape(4.dp, 20.dp, 4.dp, 20.dp)
        ) {
            Text(
                message,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            )
        }
    }
}


@PreviewLightDark
@Composable
fun LocationUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LocationUI(
                null,
                "Home by the sea",
                40,
                {},
                {}
            )
        }
    }
}
