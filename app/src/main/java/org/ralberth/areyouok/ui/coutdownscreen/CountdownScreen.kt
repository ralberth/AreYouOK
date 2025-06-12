package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.coutdownscreen.CountdownDisplay
import org.ralberth.areyouok.ui.coutdownscreen.StatusDisplayText


@Composable
fun CountdownScreen(
    navController: NavController,
    viewModel: MainViewModel  // = viewModel()
) {
    RuokScaffold(
        navBackCallback = { navController.navigate("main") }
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        StatusDisplayText(uiState.countdownStop)

        CountdownDisplay(
            uiState.countdownStart,
            uiState.countdownStop
        )

//        Spacer(Modifier.weight(1f))  // .graphicsLayer { alpha = animatedAlpha })

        Button(
            enabled = uiState.countdownStart != null,
            onClick = viewModel::checkin,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.height(60.dp)   // .graphicsLayer { alpha = animatedAlpha }
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
    }
}
