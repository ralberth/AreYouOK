package org.ralberth.areyouok.datamodel

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.ralberth.areyouok.SoundEffects
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
    private val ruokDatastore: RuokDatastore,
    private val soundEffects: SoundEffects
): ViewModel() {
    init {
        println("Create new MainViewModel")
    }

    private val _uiState = MutableStateFlow(ruokDatastore.hydrateMainScreenState())
    val uiState: StateFlow<RuokScreenState> = _uiState.asStateFlow()

    // FIXME: these need new values when the user switches back to this app from another
    var hasAlarmPermission: Boolean = alarms.canSetAlarms()
    var hasNotifyPermission: Boolean = notifier.canSendNotifications()


    fun updateEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            println("ViewModel: UI countdown started")
            coordinator.enabled(_uiState.value.countdownLength)
            val now = Clock.systemUTC().instant()
            _uiState.update {
                it.copy(
                    countdownStart = now,
                    countdownStop = now.plus(_uiState.value.countdownLength.toLong(), ChronoUnit.MINUTES)
                )
            }
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
        val oldLength = _uiState.value.countdownLength
        if (oldLength != newCountdownLength) {
            _uiState.update {
                it.copy(countdownLength = newCountdownLength)
            }
            ruokDatastore.saveMainScreenState(_uiState.value)
            if (_uiState.value.isCountingDown())
                coordinator.durationChanged(newCountdownLength)
        }
    }


    fun updatePhoneNumber(newPhoneName: String, newPhoneNumber: String) {
        val oldPhoneNumber = _uiState.value.phoneNumber
        if (oldPhoneNumber != newPhoneNumber) {
            _uiState.update {
                it.copy(
                    phoneName = newPhoneName,
                    phoneNumber = newPhoneNumber
                )
            }
            ruokDatastore.saveMainScreenState(_uiState.value)
            if (_uiState.value.isCountingDown())
                coordinator.updatePhone(
                    oldPhoneNumber, newPhoneName, // tell current person they're done
                    newPhoneNumber, _uiState.value.countdownLength, _uiState.value.location // tell new person
                )
        }
    }


    fun updateLocation(newLocation: String) {
        val oldLocation = _uiState.value.location
        if (oldLocation != newLocation) {
            // Make newLocation be at the front of recentLocations, and trim to 10 elements
            val newRecentLocations = listOf(newLocation) + _uiState.value.recentLocations.filterNot { it == newLocation }
            _uiState.update {
                it.copy(
                    location = newLocation,
                    recentLocations = newRecentLocations.take(6)
                )
            }
            ruokDatastore.saveMainScreenState(_uiState.value)
            if (_uiState.value.isCountingDown())
                coordinator.updateLocation(newLocation)
        }
    }


    fun updateVolumePercent(newVolumePercent: Float?) {
        val oldVolume = _uiState.value.volumePercent
        if (oldVolume != newVolumePercent) {
            _uiState.update {
                it.copy(
                    volumePercent = newVolumePercent
                )
            }
            ruokDatastore.saveMainScreenState(_uiState.value)
            soundEffects.newOverrideVolumePercent(newVolumePercent)
        }
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


    /*
     * Wrapper to same name in Coordinator.
     * Our iffy design is that the Coordinator is in charge of all interactions and can be
     * referenced from any entry-point, not just @Composables or ViewModels.  The Coordinator
     * also has an ApplicationContext for interacting with the phone.  ViewModel doesn't.
     */
    fun callContact() {
        println("ViewModel.callContact()")
        coordinator.callContact(uiState.value.phoneNumber)
    }
}
