package org.ralberth.areyouok.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.movement.MovementSource
import org.ralberth.areyouok.movement.MovementUiUpdater
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.BarChart
import java.text.DecimalFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.DurationUnit


@Composable
fun MovementScreen(
    navController: NavController,
    movement: MovementSource,
    viewModel: RuokViewModel
) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var recentPositions = remember { mutableStateListOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f) }
    var nextUpdate = remember { Instant.now() }

    MovementUiUpdater(movement) {
        newPosition ->
            if (Instant.now().isAfter(nextUpdate)) {
                recentPositions.removeAt(0)
                recentPositions.add(newPosition)
                nextUpdate = Instant.now().plus(250, ChronoUnit.MILLIS)
            }
            MovementUI(
                navController,
                recentPositions
            )
    }
}


@Composable
fun MovementUI(
    navController: NavController?,
    recentPositions: List<Float>
) {
    RuokScaffold(
        navController = navController,
        route = "movement",
        title = "Calibrate Movement",
        description = "Calibrate when the device should be considered not moving so the app " +
                "knows when to alert your contact."
    ) {
        BarChart(
            maxHeight = 120.dp,
            maxValue = 50f,
            values = recentPositions,
            modifier = Modifier.padding(horizontal = 60.dp)
        )
    }
}


@PreviewLightDark
@Composable
fun MovementUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MovementUI(
                null,
                listOf(24f, 36f, 50f, 18f, 27f, 17f, 30f, 20f, 40f, 36f, 25f, 28f, 47f, 10f, 9f)
            )
        }
    }
}
