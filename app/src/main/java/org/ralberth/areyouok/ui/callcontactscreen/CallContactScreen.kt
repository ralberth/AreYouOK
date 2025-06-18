package org.ralberth.areyouok.ui.callcontactscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Break this into CallContactScreen and CallContactUI to make @Preview below simple
    val targetTime = Instant.now().plusSeconds(5)
    Ticker(targetTime, 1000) { timeRemaining ->
        if (timeRemaining != null) {
            CallContactUI(
                timeRemaining,
                uiState.phoneName,
                uiState.phoneNumber,
                { navController.navigateUp() }
            )

            if (timeRemaining.isZero) {
                viewModel.callContact()
                navController.navigateUp()
            }
        }
    }
}


@Composable
fun CallContactUI(
    timeRemaining: Duration,
    name: String,
    number: String,
    onCancel: () -> Unit
) {
    RuokScaffold(null, "callcontact", "Call Contact") {
        Text("Calling")
        Text(name)
        Text(number)
        Text("in")

        val timeRemainingFrac = timeRemaining.toMillis().toFloat() / 1000f
        val percentRemaining = timeRemainingFrac / 5f
        val wholeSecondsRemaining = ceil(timeRemainingFrac).toInt()

        Box(
            contentAlignment = Alignment.Center
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
    CallContactUI(
        Duration.ofSeconds(3),
        "John Smith",
        "(123) 456-7890",
        { }
    )
}
