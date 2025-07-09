package org.ralberth.areyouok.ui.settings

import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.movement.MovementSource
import org.ralberth.areyouok.R
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.movement.MovementUiUpdater
import org.ralberth.areyouok.movement.RotationPosition
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.theme.AreYouOkTheme
import org.ralberth.areyouok.ui.utils.ErrorStripe


@Composable
fun MovementScreen(
    navController: NavController,
    movement: MovementSource,
    viewModel: RuokViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovementUiUpdater(movement) {
        MovementUI(
            navController,
            it
        )
    }
}


@Composable
fun MovementUI(
    navController: NavController?,
    position: RotationPosition
) {
    RuokScaffold(
        navController = navController,
        route = "movement",
        title = "Calibrate Movement",
        description = "Calibrate when the device should be considered not moving so the app " +
                "knows when to alert your contact."
    ) {
        Column(
            modifier = Modifier.padding(/*horizontal =*/ 18.dp)
        ) {
            Text("X = ${position.x}")
            Text("Y = ${position.y}")
            Text("Z = ${position.z}")
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
                RotationPosition(x = .5f, y = 1.2f, z = -9.8f)
            )
        }
    }
}
