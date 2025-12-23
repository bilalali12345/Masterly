package com.bilal.masterly.viewModel

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.TimerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val skillId = savedStateHandle.get<Long>("skillId")!!
    private val _skill = MutableStateFlow(
        Skill(
            id = skillId,
            name = "JavaScript Development",
            hoursCompleted = 1500,
            hoursTotal = 10000
        )
    )
    val skillFlow: StateFlow<Skill> = _skill.asStateFlow()


    private var timerJob: Job? = null

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    val timerText: StateFlow<String> =
        elapsedSeconds
            .map { seconds ->
                val h = seconds / 3600
                val m = (seconds % 3600) / 60
                val s = seconds % 60
                "%02d:%02d:%02d".format(h, m, s)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = "00:00:00"
            )

    fun startTimer() {
        if (_isRunning.value) return

        _isRunning.value = true

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _elapsedSeconds.value++
            }
        }
    }

    fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun stopTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        applyElapsedTimeToSkill(elapsedSeconds.value)
        _elapsedSeconds.value = 0L
    }

    private fun applyElapsedTimeToSkill(seconds: Long) {
        val current = _skill.value

        val updatedSkill = current.copy(
            hoursCompleted = current.hoursCompleted + ( seconds / 60 / 60 ).toInt()
        )

        _skill.value = updatedSkill
    }



}