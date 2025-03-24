package org.ralberth.areyouok;

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class MainUiState(
    val enabled: Boolean = false, // True means we're actively counting-down or alerting people
    val delayMins: Int = 20,      // Number of minutes to count down after every button press
    val minsLeft: Int = 4         // Minutes until the app starts alerting and texting people
)


@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    var timer: DelayCountdownTimer? = null


    fun updateEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(
                enabled = isEnabled,
                minsLeft = if (isEnabled) _uiState.value.delayMins else 0
            )
        }

        if (isEnabled) {
            timer = DelayCountdownTimer(
                _uiState.value.delayMins,
                { updateMinsLeft(it) },
                { println("Timer stopped") }
            )
            timer!!.start()
            println("Timer started")
        } else {
            timer?.cancel()   // so onComplete() isn't called
            timer = null
        }
    }

    fun updateDelayMins(newDelayMins: Int) {
        _uiState.update {
            it.copy(delayMins = newDelayMins)
        }
    }

    fun updateMinsLeft(newMinsLeft: Int) {
        println("New minsLeft from timer: $newMinsLeft")
        _uiState.update {
            it.copy(minsLeft = newMinsLeft)
        }
    }
}
