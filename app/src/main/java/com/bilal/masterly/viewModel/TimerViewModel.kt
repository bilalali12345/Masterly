package com.bilal.masterly.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.repository.TimerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(
    savedStateHandle: SavedStateHandle,
    private val repo: TimerRepository = TimerRepository,
) : ViewModel() {

    // skill unchanged
    private val skillId = savedStateHandle.get<Long>("skillId")!!
    private val _skill = MutableStateFlow(
        Skill(
            id = skillId,
            name = "JavaScript Development",
            hoursCompleted = 1500,
            hoursTotal = 10000
        )
    )
    val skillFlow = _skill.asStateFlow()

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
    private val _uiEvents = MutableSharedFlow<TimerUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    // requests coming from UI → VM. VM decides whether to run locally or ask host to start Service.
    fun requestStart() {
        viewModelScope.launch {
            // if you want service-backed always:
            _uiEvents.emit(TimerUiEvent.RequestStartService)
            // If you also want local fallback you could call repo.setRunning(true) etc.
        }
    }

    fun requestPause() {
        viewModelScope.launch { _uiEvents.emit(TimerUiEvent.RequestPauseService) }
    }

    fun requestStop() {
        viewModelScope.launch { _uiEvents.emit(TimerUiEvent.RequestStopService) }
    }

    // If you ever need to mutate skill from UI:
    fun addElapsedToSkillAndReset() {
        val seconds = elapsedSeconds.value
        val updatedSkill = _skill.value.copy(
            hoursCompleted = _skill.value.hoursCompleted + (seconds / 3600).toInt()
        )
        _skill.value = updatedSkill
        // reset repo elapsed (if service ended)
        repo.updateElapsed(0L)
        repo.setRunning(false)
    }
}

sealed class TimerUiEvent {
    object RequestStartService : TimerUiEvent()
    object RequestPauseService : TimerUiEvent()
    object RequestStopService : TimerUiEvent()
}
