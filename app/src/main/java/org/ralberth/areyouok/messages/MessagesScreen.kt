package org.ralberth.areyouok.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import org.ralberth.areyouok.RuokTopBar
import org.ralberth.areyouok.RuokBottomBar
import java.text.SimpleDateFormat
import java.util.Locale


val logTimeFormatter = SimpleDateFormat("hh:mm:ss aa", Locale.US)


@Composable
fun MessagesScreen(
    modifier: Modifier = Modifier,
    viewModel: MessagesViewModel = viewModel()
) {
    Scaffold(
        topBar = { RuokTopBar("Log Messages") },
        bottomBar = { RuokBottomBar({}, {}, {}) }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxHeight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight().align(Alignment.Start).padding(5.dp)
            ) {
                items(uiState.messages) { message ->
                    val ts = logTimeFormatter.format(message.logTime)
                    Text(
                        "$ts: ${message.message}",
                        color = message.color,
                        fontFamily = FontFamily.Monospace,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    AreYouOkTheme {
//        MainScreen(Modifier, MainViewModel(SoundEffectsStub()))
//    }
//}