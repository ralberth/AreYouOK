package org.ralberth.areyouok.ui.callcontactscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.Ticker
import java.time.Instant
import java.time.Duration
import kotlin.math.ceil


@Composable
fun CallContactScreen(
    navController: NavController,
    viewModel: RuokViewModel
) {
    var alreadyCalled by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Break this into CallContactScreen and CallContactUI to make @Preview below simple
    val targetTime = Instant.now().plusSeconds(5)
    Ticker(targetTime, 1000) { timeRemaining ->
        if (timeRemaining != null) {
            CallContactUI(
                navController,
                timeRemaining,
                uiState.phoneName,
                uiState.phoneNumber,
                { navController.navigateUp() }
            )

            if (timeRemaining.isZero) {
                if (! alreadyCalled) {
                    alreadyCalled = true
                    viewModel.callContact()
                    navController.navigateUp()
                    // Navigating away means next time we're on this screen, the alreadyCalled
                    // remember above will be created fresh.
                }
            }
        }
    }
}


@Composable
fun CallContactUI(
    navController: NavController?,
    timeRemaining: Duration,
    name: String,
    number: String,
    onCancel: () -> Unit
) {
    RuokScaffold(navController, "callcontact", "Call Contact") {
        Text("Calling", modifier = Modifier.padding(top=30.dp))
        Text(name)
        Text(number)
        Text("in", modifier = Modifier.padding(14.dp))

        val timeRemainingFrac = timeRemaining.toMillis().toFloat() / 1000f
        val percentRemaining = timeRemainingFrac / 5f
        val wholeSecondsRemaining = ceil(timeRemainingFrac).toInt()

        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.padding(14.dp)
        ) {
            CircularProgressIndicator(
                progress = { percentRemaining },
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = wholeSecondsRemaining.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onCancel,
            modifier = Modifier.height(60.dp)
        ) {
            Icon(
                Icons.Filled.Close,
                "Cancel"
            )
            Text(
                " Cancel",
                fontSize = 20.sp
            )
        }
    }
}


@PreviewLightDark
@Composable
fun CallContactUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CallContactUI(
                null,
                Duration.ofSeconds(3),
                "John Smith",
                "(123) 456-7890",
                { }
            )
        }
    }
}
