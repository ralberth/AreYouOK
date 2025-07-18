package org.ralberth.areyouok.ui.settings.movement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.movement.MovementSource
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.BarChart
import org.ralberth.areyouok.ui.utils.CenteredButton


@Composable
fun MovementScreen(
    navController: NavController,
    movementSource: MovementSource,
    viewModel: RuokViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var cutoff = remember { uiState.movementThreshold }

    MovementUiUpdater(movementSource) {
        history ->
            MovementUI(
                navController,
                history,
                cutoff,
                { cutoff = it },
                { viewModel.updateMovementThreshold(cutoff); navController.navigateUp() }
            )
    }
}


@Composable
fun MovementUI(
    navController: NavController?,
    history: List<Float>,
    cutoff: Float,
    onCutoffChange: (Float) -> Unit,
    onDone: () -> Unit
) {
    RuokScaffold(
        navController = navController,
        route = "movement",
        title = "Calibrate Movement",
        description = "Calibrate when the device should be considered not moving so the app " +
                "knows when to alert your contact."
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            Text(
                text = "Move your phone gently like you're walking or running.  The graph below " +
                        "shows the cutoff when the phone thinks you aren't moving.  Adjust the " +
                        "slider to pick a cutoff you like.",
                modifier = Modifier.padding(horizontal = 18.dp)
            )
            BarChart(
                maxHeight = 120.dp,
                maxValue = 50f,
                cutoff = cutoff,
                values = history,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            Slider(
                value = cutoff,
                onValueChange = onCutoffChange,
                valueRange = 5f..40f
            )
            CenteredButton(
                onClick = onDone,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}


@PreviewLightDark
@Composable
fun MovementUIPreview() {
    AreYouOkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MovementUI(
                null,
                listOf(24f, 36f, 50f, 18f, 27f, 17f, 30f, 20f, 40f, 36f, 25f, 28f, 47f, 10f, 9f),
                20f,
                { },
                { }
            )
        }
    }
}
