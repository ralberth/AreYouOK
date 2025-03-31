package org.ralberth.areyouok.countdown

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
import javax.inject.Inject


data class CountdownUiState(
    // Configuration and big state -- things that don't change while the countdown timer runs
    val enabled: Boolean = false,                 // True means we're actively counting-down or alerting people
    // Tactical stuff that changes while the timer is running
    val message: String = "Idle",
    val statusColor: Color = StatusIdle,
    val minsLeft: Int = 4,                        // Minutes until the app starts alerting and texting people
    val countdownBarColor: Color = ProgressOK,
)


ugh, if countdown ui elements are not drawn and we're looking at the messages screen, will countdownviewmodel things fire, will it play alerts, send messages, and text people?
We might have a problem where we need to keep all three as Activities or at least use NavHost in some way.
Could adopt background/service things: have the UI strictly do UI things.
have a service/background thing handle everything else, instead of making the ViewModels do it.
Service sends texts, plays musics, logs messages, everything.


@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val soundEffects: SoundEffects?,
    private val alertSender: AlertSender?
): ViewModel() {

    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()

    private val timer = DelayCountdownTimer(
        { updateMinsLeft(it) },
        { timeRanOut() }
    )

    fun updateEnabled(isEnabled: Boolean, delayMinutes: Int) {
        println("Button enabled: $isEnabled")
        soundEffects!!.toggle()
        println("add to newMessages")
//        val newMessages = newListAddMessage(
//            _uiState.value.messages,
//            LogMessage(if (isEnabled) "Enabled" else "Disabled")
//        )
        println("update _uiState")
        _uiState.update {
            it.copy(
                enabled = isEnabled,
                minsLeft = if (isEnabled) delayMinutes else 0,
                message = if (isEnabled) "Running" else "Idle",
                statusColor = if (isEnabled) StatusOK else StatusIdle
            )
        }

        if (isEnabled) {
            println("Startign the timer")
            timer.start(delayMinutes)
            alertSender!!.enabled(delayMinutes)
            println("Timer started")
        } else {
            timer.cancel()
            alertSender!!.disabled()
            println("Timer stopped")
        }
    }




    fun updateMinsLeft(newMinsLeft: Int) {
        println("New minsLeft from timer: $newMinsLeft")

        var newBarColor: Color = ProgressOK
        var newStatusColor: Color = StatusOK
        if (newMinsLeft in 2..3) {
            newBarColor = ProgressWarning
            newStatusColor = StatusWarning
            soundEffects!!.yellowWarning()
        }
        if (newMinsLeft == 1) {
            newBarColor = ProgressDanger
            newStatusColor = StatusDanger
            soundEffects!!.redWarning()
        }
        if (newMinsLeft == 0) {
            soundEffects!!.timesUp()
        }

        _uiState.update {
            it.copy(
                minsLeft = newMinsLeft,
                countdownBarColor = newBarColor,
                statusColor = newStatusColor
            )
        }
    }

    fun checkin(delayMinutes: Int) {
        println("Reset timer")
        timer.reset()
        soundEffects!!.stop()
        alertSender!!.checkIn(delayMinutes)
//        val newMessages = newListAddMessage(
//            _uiState.value.messages,
//            LogMessage("Check-in (${_uiState.value.minsLeft} min left)")
//        )
        _uiState.update {
            it.copy(
                minsLeft = delayMinutes,
                message = "Running",
                countdownBarColor = ProgressOK
            )
        }
    }


    fun timeRanOut() {
        println("Time ran out: cancel timer, notify contacts")
        timer.cancel()
        alertSender!!.unresponsive()
//        val newMessages = newListAddMessage(
//            _uiState.value.messages,
//            LogMessage("Time ran out, notify contacts", Color.Red)
//        )
        _uiState.update {
            it.copy(
                minsLeft = 0,
                message = "Notifying Contacts",
                statusColor = ProgressPaging
            )
        }
    }
}
