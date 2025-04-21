package org.ralberth.areyouok.ui.mainscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.alarms.RuokAlarms
import org.ralberth.areyouok.coordinator.Coordinator
import org.ralberth.areyouok.datamodel.MainScreenState
import org.ralberth.areyouok.datamodel.RuokDatastore
import org.ralberth.areyouok.notifications.RuokNotifier
import org.ralberth.areyouok.ui.theme.ProgressDanger
import org.ralberth.areyouok.ui.theme.ProgressOK
import org.ralberth.areyouok.ui.theme.ProgressPaging
import org.ralberth.areyouok.ui.theme.ProgressWarning
import org.ralberth.areyouok.ui.theme.StatusDanger
import org.ralberth.areyouok.ui.theme.StatusIdle
import org.ralberth.areyouok.ui.theme.StatusOK
import org.ralberth.areyouok.ui.theme.StatusWarning
import java.time.Clock
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val coordinator: Coordinator,
    alarms: RuokAlarms,
    notifier: RuokNotifier,
    private val ruokDatastore: RuokDatastore
): ViewModel() {
    private val _uiState = MutableStateFlow(ruokDatastore.hydrateMainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    // FIXME: these need new values when the user switches back to this app from another
    var hasAlarmPermission: Boolean = true   // alarms.canSetAlarms()
    var hasNotifyPermission: Boolean = true   // notifier.canSendNotifications()

    init {
        println("Create new MainViewModel")
        // FIXME: if we rehydrate a ViewModel from preferences and we're running, the timer below needs to be started
    }

    private val timer = DelayCountdownTimer(
        { updateMinsLeft(it) },
        { timeRanOut() }
    )

    fun updateEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(
                whenEnabled = if (isEnabled) Clock.systemUTC().instant() else null,
                minsLeft = if (isEnabled) _uiState.value.delayMins else 0,
                message = if (isEnabled) "Running" else "Idle",
                statusColor = if (isEnabled) StatusOK else StatusIdle
            )
        }

        if (isEnabled) {
            println("Timer started")
            timer.start(_uiState.value.delayMins)
            coordinator.enabled(_uiState.value.delayMins)
        } else {
            println("Timer stopped")
            timer.cancel()
            coordinator.disabled()
        }

        ruokDatastore.saveMainScreenState(_uiState.value)
    }


    fun updateDelayMins(newDelayMins: Int) {
        _uiState.update {
            it.copy(delayMins = newDelayMins)
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }


    fun updateMinsLeft(newMinsLeft: Int) {
        println("New minsLeft from timer: $newMinsLeft")

        // TODO: put this into the progressbar function itself (hide the logic there)
        var newBarColor: Color = ProgressOK
        var newStatusColor: Color = StatusOK
        if (newMinsLeft in 2..3) {
            newBarColor = ProgressWarning
            newStatusColor = StatusWarning
        }
        if (newMinsLeft == 1) {
            newBarColor = ProgressDanger
            newStatusColor = StatusDanger
        }

        _uiState.update {
            it.copy(
                minsLeft = newMinsLeft,
                countdownBarColor = newBarColor,
                statusColor = newStatusColor
            )
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }

    fun checkin() {
        println("Reset timer")
        timer.reset()
        coordinator.checkin()
        _uiState.update {
            it.copy(
                minsLeft = _uiState.value.delayMins,
                message = "Running",
                countdownBarColor = ProgressOK
            )
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }


    fun timeRanOut() {
        println("Time ran out: cancel timer, notify contacts")
        timer.cancel()
        _uiState.update {
            it.copy(
                minsLeft = 0,
                message = "Notifying Contacts",
                statusColor = ProgressPaging
            )
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }
}
