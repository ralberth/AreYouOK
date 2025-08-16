package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.CenteredButton
import kotlin.math.roundToInt


@Composable
fun DurationSelectScreen(navController: NavController, viewModel: RuokViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var nextDuration by remember { mutableIntStateOf(uiState.countdownLength) }

    DurationSelectUI(
        navController,
        nextDuration,
        { nextDuration = it },
        { viewModel.updateCountdownLength(nextDuration) }
    )
}


@Composable
fun DurationSelectUI(
    navController: NavController?,
    countdownLength: Int,
    onLengthChange: (Int) -> Unit,
    onDone: () -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "durationselect",
        title = "Select Duration",
        description = "Total time after you click to enable the countdown before your " +
                "contact is texted saying you might be in trouble.",
        onNavigateUp = onDone
    ) {
        Row(modifier = Modifier.padding(18.dp)) {
            Column {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Countdown Duration:   ")
                        }
                        append("${countdownLength} minutes")
                    }
                )

                Slider(
                    value = countdownLength.toFloat(),
                    onValueChange = { onLengthChange(it.roundToInt()) },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    steps = 10,
                    valueRange = 5f..60f
                )

                CenteredButton(
                    onClick = onDone,
                    modifier = Modifier.padding(top = 20.dp)
                )

                TableBuilder()
                    .columnWeights(2, 1, 1)
                    .headerRow("", "Time", "Example")
                    .row("Start",  "T-${countdownLength}", "12:00")
                    .row(
                        "1st notification",
                        "T-3",
                        "12:${String.format("%02d", countdownLength - 3)}"
                    )
                    .row(
                        "2nd notification",
                        "T-2",
                        "12:${String.format("%02d", countdownLength - 2)}"
                    )
                    .row(
                        "Alert",
                        "T-1",
                        "12:${String.format("%02d", countdownLength - 1)}"
                    )
                    .row(
                        "Alarm",
                        "T-0",
                        "12:${String.format("%02d", countdownLength)}"
                    )
                    .build()
            }
        }
    }
}


@PreviewLightDark
@Composable
fun DurationSelectUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DurationSelectUI(null, 30, {}, {})
        }
    }
}
