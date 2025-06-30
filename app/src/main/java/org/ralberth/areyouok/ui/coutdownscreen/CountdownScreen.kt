package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.minutesBeforeEnd
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.coutdownscreen.CountdownDisplay
import org.ralberth.areyouok.ui.coutdownscreen.StatusDisplayText
import org.ralberth.areyouok.ui.settings.TableBuilder
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.ErrorStripe
import org.ralberth.areyouok.ui.utils.Ticker
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit


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
        uiState.phoneNumber.isNotBlank(),
        viewModel::checkin,
        { navController.navigate("callcontact") }
    )
}


@Composable
fun CountdownUI(
    navController: NavController?,
    countdownStart: Instant?,
    countdownStop: Instant?,
    callButtonEnabled: Boolean,
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FilledIconButton(
                            enabled = countdownStart != null,
                            onClick = onCheckin,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Check-in"
                            )
                        }
                        Text("Check-in")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FilledTonalIconButton(
                            enabled = callButtonEnabled,
                            onClick = onCallContact,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Call,
                                contentDescription = "Call"
                            )
                        }
                        Text("Call Contact")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                ErrorStripe(
                    shouldDisplay = !callButtonEnabled,
                    message = "Call Contact button is disabled: no contact selected on the main screen."
                )


                if (timeRemaining != null) {
                    val minsLeft = minutesBeforeEnd(timeRemaining)

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
                true,
                {},
                {}
            )
        }
    }
}