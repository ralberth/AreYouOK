package com.example.helloworld;

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class MainUiState(
    val enabled: Boolean = false
)


@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun setEnabled(isEnabled: Boolean) {
        _uiState.update {
            it.copy(enabled = isEnabled)
        }
    }
}
