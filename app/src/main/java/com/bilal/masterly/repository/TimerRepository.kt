package com.bilal.masterly.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TimerRepository {

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    var startTimestampMillis: Long? = null

    fun updateElapsed(sec: Long) { _elapsedSeconds.value = sec }
    fun setRunning(r: Boolean) { _isRunning.value = r }
}