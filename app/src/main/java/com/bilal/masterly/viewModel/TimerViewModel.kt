package com.bilal.masterly.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.MyApplication
import com.bilal.masterly.Ui_Layer.UiEvent
import com.bilal.masterly.repository.TimerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(
    savedStateHandle: SavedStateHandle,
    private val repo: TimerRepository,
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return TimerViewModel(
                    savedStateHandle,
                    (application as MyApplication).timerRepository,
                ) as T
            }
        }
    }

    private val skillId = savedStateHandle.get<Long>("skillId")
    val skillFlow: StateFlow<Skill?> = repo.getSkillById(skillId ?: -1)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Mirror repository flows (no duplication)
    val elapsedSeconds: StateFlow<Long> = repo.elapsedSeconds
    val isRunning: StateFlow<Boolean> = repo.isRunning

    // UI-friendly timer text derived from elapsedSeconds
    val timerText: StateFlow<String> = elapsedSeconds
        .map { seconds ->
            val h = seconds / 3600
            val m = (seconds % 3600) / 60
            val s = seconds % 60
            "%02d:%02d:%02d".format(h, m, s)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "00:00:00")

    // one-shot UI events that the Activity/host collects
    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    // requests coming from UI → VM. VM decides whether to run locally or ask host to start Service.
    fun requestStart() {
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.RequestStartService)
        }
    }

    fun requestPause() {
        viewModelScope.launch { _uiEvents.emit(UiEvent.RequestPauseService) }
    }

    fun requestStop() {
        viewModelScope.launch { _uiEvents.emit(UiEvent.RequestStopService) }
    }

    // If you ever need to mutate skill from UI:
    fun addElapsedToSkillAndReset() {
        val seconds = elapsedSeconds.value
        skillFlow.value?.let {
            val updatedSkill = it.copy(
                hoursCompleted = it.hoursCompleted + (seconds / 3600).toInt()
            )
            viewModelScope.launch {
                repo.updateSkill(updatedSkill)
            }
        }

        // reset repo elapsed (if service ended)
        repo.updateElapsed(0L)
        repo.setRunning(false)
    }
}
