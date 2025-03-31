package org.ralberth.areyouok

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.ui.theme.ProgressDanger
import org.ralberth.areyouok.ui.theme.ProgressOK
import org.ralberth.areyouok.ui.theme.ProgressPaging
import org.ralberth.areyouok.ui.theme.ProgressWarning
import org.ralberth.areyouok.ui.theme.StatusDanger
import org.ralberth.areyouok.ui.theme.StatusIdle
import org.ralberth.areyouok.ui.theme.StatusOK
import org.ralberth.areyouok.ui.theme.StatusWarning
import javax.inject.Inject


data class MainUiState(
    // Configuration and big state -- things that don't change while the countdown timer runs
    val enabled: Boolean = false,                 // True means we're actively counting-down or alerting people
    val delayMins: Int = 20,                      // Number of minutes to count down after every button press
    // Tactical stuff that changes while the timer is running
    val message: String = "Idle",
    val statusColor: Color = StatusIdle,
    val minsLeft: Int = 4,                        // Minutes until the app starts alerting and texting people
    val countdownBarColor: Color = ProgressOK
)


@HiltViewModel
class MainViewModel @Inject constructor(
    private val soundEffects: SoundEffects,
    private val alertSender: AlertSender
): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val timer = DelayCountdownTimer(
        { updateMinsLeft(it) },
        { timeRanOut() }
    )

    fun updateEnabled(isEnabled: Boolean) {
        soundEffects.toggle()
        _uiState.update {
            it.copy(
                enabled = isEnabled,
                minsLeft = if (isEnabled) _uiState.value.delayMins else 0,
                message = if (isEnabled) "Running" else "Idle",
                statusColor = if (isEnabled) StatusOK else StatusIdle
            )
        }

        if (isEnabled) {
            timer.start(_uiState.value.delayMins)
            alertSender.enabled(_uiState.value.delayMins)
            println("Timer started")
        } else {
            timer.cancel()
            alertSender.disabled()
            soundEffects.stop()
            println("Timer stopped")
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
        var newStatusColor: Color = StatusOK
        if (newMinsLeft in 2..3) {
            newBarColor = ProgressWarning
            newStatusColor = StatusWarning
            soundEffects.yellowWarning()
        }
        if (newMinsLeft == 1) {
            newBarColor = ProgressDanger
            newStatusColor = StatusDanger
            soundEffects.redWarning()
        }
        if (newMinsLeft == 0) {
            soundEffects.timesUp()
        }

        _uiState.update {
            it.copy(
                minsLeft = newMinsLeft,
                countdownBarColor = newBarColor,
                statusColor = newStatusColor
            )
        }
    }

    fun checkin() {
        println("Reset timer")
        timer.reset()
        soundEffects.stop()
        alertSender.checkin(_uiState.value.delayMins)
        _uiState.update {
            it.copy(
                minsLeft = _uiState.value.delayMins,
                message = "Running",
                countdownBarColor = ProgressOK
            )
        }
    }


    fun timeRanOut() {
        println("Time ran out: cancel timer, notify contacts")
        timer.cancel()
        alertSender.unresponsive()
        _uiState.update {
            it.copy(
                minsLeft = 0,
                message = "Notifying Contacts",
                statusColor = ProgressPaging
            )
        }
    }
}
