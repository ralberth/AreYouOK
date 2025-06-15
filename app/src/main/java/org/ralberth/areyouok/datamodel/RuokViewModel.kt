package org.ralberth.areyouok.datamodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.alarms.RuokAlarms
import org.ralberth.areyouok.coordinator.Coordinator
import org.ralberth.areyouok.notifications.RuokNotifier
import java.time.Clock
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class RuokViewModel @Inject constructor(
    private val coordinator: Coordinator,
    alarms: RuokAlarms,
    notifier: RuokNotifier,
    private val ruokDatastore: RuokDatastore
): ViewModel() {
    init {
        println("Create new MainViewModel")
    }

    private val _uiState = MutableStateFlow(ruokDatastore.hydrateMainScreenState())
    val uiState: StateFlow<RuokScreenState> = _uiState.asStateFlow()

    // FIXME: these need new values when the user switches back to this app from another
    var hasAlarmPermission: Boolean = alarms.canSetAlarms()
    var hasNotifyPermission: Boolean = notifier.canSendNotifications()


    fun updatePhoneNumber(newPhoneName: String, newPhoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneName = newPhoneName,
                phoneNumber = newPhoneNumber
            )
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }

    fun updateEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            println("ViewModel: UI countdown started")
            val now = Clock.systemUTC().instant()
            _uiState.update {
                it.copy(
                    countdownStart = now,
                    countdownStop = now.plus(_uiState.value.countdownLength.toLong(), ChronoUnit.MINUTES)
                )
            }
            coordinator.enabled(_uiState.value.countdownLength)
        } else {
            println("ViewModel: countdown stopped")
            coordinator.disabled()
            _uiState.update {
                it.copy(
                    countdownStart = null,
                    countdownStop = null
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


    fun checkin() {
        println("ViewModel.checkin(${uiState.value.countdownLength})")
        coordinator.checkin(uiState.value.countdownLength)
        val now = Clock.systemUTC().instant()
        _uiState.update {
            it.copy(
                countdownStart = now,
                countdownStop = now.plus(_uiState.value.countdownLength.toLong(), ChronoUnit.MINUTES)
            )
        }
        ruokDatastore.saveMainScreenState(_uiState.value)
    }
}
