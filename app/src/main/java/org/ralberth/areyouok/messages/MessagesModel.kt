package org.ralberth.areyouok.messages

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.DelayCountdownTimer
import org.ralberth.areyouok.SoundEffects
import org.ralberth.areyouok.ui.theme.ProgressDanger
import org.ralberth.areyouok.ui.theme.ProgressOK
import org.ralberth.areyouok.ui.theme.ProgressPaging
import org.ralberth.areyouok.ui.theme.ProgressWarning
import org.ralberth.areyouok.ui.theme.StatusDanger
import org.ralberth.areyouok.ui.theme.StatusIdle
import org.ralberth.areyouok.ui.theme.StatusOK
import org.ralberth.areyouok.ui.theme.StatusWarning
import java.util.Date
import javax.inject.Inject



data class LogMessage(
    val message: String = "",
    val color: Color = Color.Black,
    val logTime: Date = Date()
)


data class CountdownUiState(
    val messages: List<LogMessage> = ArrayList()
)


fun newListAddMessage(currList: List<LogMessage>, newLogMessage: LogMessage): List<LogMessage> {
    val newList: ArrayList<LogMessage> = ArrayList(currList)
    newList.add(newLogMessage)
    if (newList.size >= 50)
        newList.removeAt(0)
    return newList
}


@HiltViewModel
class MessagesViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()
}
