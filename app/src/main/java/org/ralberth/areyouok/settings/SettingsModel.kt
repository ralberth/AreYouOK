package org.ralberth.areyouok.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.AlertSender
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


data class SettingsUiState(
    val delayMins: Int = 20
)


@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()


    fun updateDelayMins(newDelayMins: Int) {
        _uiState.update {
            it.copy(delayMins = newDelayMins)
        }
    }
}
