package org.ralberth.areyouok.ui.mainscreen

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
import java.time.Clock
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val coordinator: Coordinator,
    alarms: RuokAlarms,
    notifier: RuokNotifier,
    private val ruokDatastore: RuokDatastore
): ViewModel() {
    // Initializers, attributes, and "init {}" blocks fire in the order they appear below
    // Order matters for the timer, etc.

    init {
        println("Create new MainViewModel")
    }

    private val _uiState = MutableStateFlow(ruokDatastore.hydrateMainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    // FIXME: these need new values when the user switches back to this app from another
    var hasAlarmPermission: Boolean = alarms.canSetAlarms()
    var hasNotifyPermission: Boolean = notifier.canSendNotifications()

//    private val timer = DelayCountdownTimer(
//        { updateMinsLeft(it) },
//        { timeRanOut() }
//    )

    init {
        // If this view model was rehydrated by android while the timers were running, then
        // we need to turn the timer back on.
//        if (uiState.value.countdownStop != null)
//            timer.start(uiState.value.countdownStop)
    }

    fun updateEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            println("ViewModel: UI countdown started")
            val now = Clock.systemUTC().instant()
            _uiState.update {
                it.copy(
                    countdownStart = now,
                    countdownStop = now.plus(_uiState.value.countdownLength.toLong(), ChronoUnit.MINUTES),
//                    minsLeft = _uiState.value.countdownLength
                )
            }
//            timer.start(_uiState.value.countdownStop)
            coordinator.enabled(_uiState.value.countdownLength)
        } else {
            println("ViewModel: UI timer stopped")
//            timer.cancel()
            coordinator.disabled()
            _uiState.update {
                it.copy(
                    countdownStart = null,
                    countdownStop = null,
//                    minsLeft = null
                )
            }
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }


    fun updateCountdownLength(newCountdownLength: Int) {
        _uiState.update {
            it.copy(countdownLength = newCountdownLength)
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }


//    fun updateMinsLeft(newMinsLeft: Int) {
//        println("ViewModel.updateMinsLeft($newMinsLeft)")
//        _uiState.update {
//            it.copy(
//                minsLeft = newMinsLeft
//            )
//        }
//        ruokDatastore.saveMainScreenState(_uiState.value)
//    }

    fun checkin() {
        println("ViewModel.checkin()")
//        timer.reset()
        coordinator.checkin()
        val now = Clock.systemUTC().instant()
        _uiState.update {
            it.copy(
                countdownStart = now,
                countdownStop = now.plus(_uiState.value.countdownLength.toLong(), ChronoUnit.MINUTES),
//                minsLeft = _uiState.value.countdownLength
            )
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }


//    fun timeRanOut() {
//        println("ViewModel.timeRanOut(): cancel UI timer")
//        timer.cancel()
//        _uiState.update {
//            it.copy(
//                minsLeft = 0
//            )
//        }
//        ruokDatastore.saveMainScreenState(_uiState.value)
//    }
}
