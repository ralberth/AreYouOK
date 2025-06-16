package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.messaging.AlertSender


@Composable
fun LocationScreen(navController: NavController, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RuokScaffold(
        navController = navController,
        route = "location",
        title = "Set Your Location",
        description = "Where you physically are right now.  Sent to your POC so they know where you are."
    ) {
        TextField(
            value = uiState.location,
            onValueChange = { viewModel.updateLocation(it) },
            label = { Text("Location") }
        )

        Text("How messages sent to your contact will look:")
        Text(AlertSender.getTurnedOnMessage(uiState.countdownLength, uiState.location))
        Text(AlertSender.missedCheckinMessage(uiState.location))

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { navController.navigateUp() }) {
                Text("OK")
            }
        }
    }
}
