package org.ralberth.areyouok.messages

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import java.text.SimpleDateFormat
import java.util.Locale


val logTimeFormatter = SimpleDateFormat("hh:mm:ss aa", Locale.US)


@Composable
fun MessagesScreen(
//    navActions: RuokNavigationActions,
//    modifier: Modifier = Modifier,
//    viewModel: MessagesViewModel = viewModel()
    messages: List<LogMessage>
) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
//            .align(Alignment.Start)
            .padding(5.dp)
    ) {
        items(messages) { message ->
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


//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    AreYouOkTheme {
//        MainScreen(Modifier, MainViewModel(SoundEffectsStub()))
//    }
//}