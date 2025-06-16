package org.ralberth.areyouok.ui.mainscreen

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
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
import org.ralberth.areyouok.datamodel.RuokViewModel
import org.ralberth.areyouok.ui.RuokScaffold
import org.ralberth.areyouok.ui.utils.Ticker
import java.time.Instant


@Composable
fun CallContactScreen(
    navController: NavController,
    viewModel: RuokViewModel
) {
    RuokScaffold(navController, "callcontact", "Call Contact") {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Text("Calling")
        Text(uiState.phoneName)
        Text(uiState.phoneNumber)

        val targetTime = Instant.now().plusSeconds(4)
        println("TARGET TIME $targetTime")
        Ticker(targetTime) {
            timeRemaining ->
                val s = timeRemaining!!.seconds.toInt()
                println(s)
                when {
                    s > 3 -> Text("> 3")
                    s <= 3 -> Text("3")
                    s <= 2 -> Text("2")
                    s <= 1 -> Text("1")
                    else -> Text("calling")
                }
        }

        Button(
            onClick = { navController.navigateUp() },
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
