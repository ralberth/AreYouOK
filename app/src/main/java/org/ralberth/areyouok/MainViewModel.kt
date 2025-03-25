package org.ralberth.areyouok;

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.ui.theme.ProgressDanger
import org.ralberth.areyouok.ui.theme.ProgressOK
import org.ralberth.areyouok.ui.theme.ProgressWarning
import javax.inject.Inject


data class MainUiState(
    val message: String = "Idle",
    val enabled: Boolean = false, // True means we're actively counting-down or alerting people
    val delayMins: Int = 20,      // Number of minutes to count down after every button press
    val minsLeft: Int = 4,        // Minutes until the app starts alerting and texting people
    val countdownBarColor: Color = ProgressOK
)


@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    val timer = DelayCountdownTimer(
        { updateMinsLeft(it) },
        { timeRanOut() }
    )

    fun updateEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(
                enabled = isEnabled,
                minsLeft = if (isEnabled) _uiState.value.delayMins else 0,
                message = if (isEnabled) "Running" else "Idle"
            )
        }

        if (isEnabled) {
            timer.start(_uiState.value.delayMins)
            println("Timer started")
        } else {
            timer.cancel()
        }
    }


    fun updateDelayMins(newDelayMins: Int) {
        _uiState.update {
            it.copy(delayMins = newDelayMins)
        }
    }


    fun updateMinsLeft(newMinsLeft: Int) {
        println("New minsLeft from timer: $newMinsLeft")

        var newBarColor: Color = ProgressOK
        if (newMinsLeft >= 2 && newMinsLeft <= 3)
            newBarColor = ProgressWarning
        if (newMinsLeft == 1)
            newBarColor = ProgressDanger

        _uiState.update {
            it.copy(
                minsLeft = newMinsLeft,
                countdownBarColor = newBarColor
            )
        }
    }

    fun checkin() {
        timer.reset()
        _uiState.update {
            it.copy(
                minsLeft = _uiState.value.delayMins,
                message = "Running"
            )
        }
    }


    fun timeRanOut() {
        timer.cancel()
        _uiState.update {
            it.copy(
                minsLeft = 0,
                message = "Notifying Contacts"
            )
        }
    }
}
