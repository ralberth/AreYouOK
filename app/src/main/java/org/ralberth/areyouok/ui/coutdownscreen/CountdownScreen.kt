package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.coutdownscreen.CountdownDisplay
import org.ralberth.areyouok.ui.coutdownscreen.StatusDisplayText
import org.ralberth.areyouok.ui.settings.TableBuilder
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.Ticker
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration


@Composable
fun CountdownScreen(
    navController: NavController,
    viewModel: RuokViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CountdownUI(
        navController,
        uiState.countdownStart,
        uiState.countdownStop,
        viewModel::checkin,
        { navController.navigate("callcontact") }
    )
}


@Composable
fun CountdownUI(
    navController: NavController?,
    countdownStart: Instant?,
    countdownStop: Instant?,
    onCheckin: () -> Unit,
    onCallContact: () -> Unit
) {
    RuokScaffold(
        navController,
        "countdown",
        "Countdown",
        showNavigateUp = false
    ) {
        StatusDisplayText(countdownStop)

        Ticker(countdownStop) {
            timeRemaining ->
                CountdownDisplay(
                    countdownStart,
                    countdownStop,
                    timeRemaining
                )

                Button(
                    enabled = countdownStart != null,
                    onClick = onCheckin,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(60.dp)
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        "Check-in"
                    )
                    Text(
                        " Check-in",
                        fontSize = 20.sp
                    )
                }

                FilledTonalButton(
                    onClick = onCallContact,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(60.dp)
                ) {
                    Icon(
                        Icons.Filled.Call,
                        "Check-in"
                    )
                    Text(
                        " Call Contact",
                        fontSize = 20.sp
                    )
                }


                if (timeRemaining != null) {
                    val minsLeft: Long = timeRemaining.toMinutes()

                    TableBuilder()
                        .columnWeights(1, 1, 5)
                        .row(
                            Icons.Filled.Done,
                            Icons.Filled.Face,
                            "Send \"starting\" text"
                        )
                        .row(
                            if (minsLeft <= 3) Icons.Filled.Done else "",
                            Icons.Filled.Notifications,
                            "T-3 minutes left"
                        )
                        .row(
                            if (minsLeft <= 2) Icons.Filled.Done else "",
                            Icons.Filled.Notifications,
                            "T-2 minutes left"
                        )
                        .row(
                            if (minsLeft <= 1) Icons.Filled.Done else "",
                            Icons.Filled.Notifications,
                            "T-1 minutes left"
                        )
                        .row(
                            if (minsLeft <= 0) Icons.Filled.Done else "",
                            Icons.Filled.Face,
                            "Sent \"Time ran out!\""
                        )
                        .build()
                }
        }
    }
}


@PreviewLightDark
@Composable
fun CountdownUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CountdownUI(
                null,
                Instant.now(),
                Instant.now().plusSeconds(20 * 60),
                {},
                {}
            )
        }
    }
}