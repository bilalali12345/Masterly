package com.bilal.masterly.repository

import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.data.SkillDao
import com.bilal.masterly.data.toDomain
import com.bilal.masterly.data.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerRepository(
    private val skillDao: SkillDao,
    private val repoScope: CoroutineScope,
) {

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    var startTimestampMillis: Long? = null

    fun updateElapsed(sec: Long) {
        _elapsedSeconds.value = sec
    }

    fun setRunning(r: Boolean) {
        _isRunning.value = r
    }

    // HOT observable current skill
    private val _currentSkill = MutableStateFlow<Skill?>(null)
    val currentSkill: StateFlow<Skill?> = _currentSkill.asStateFlow()

    private var currentSkillJob: Job? = null

    // Start watching DAO's Flow for this skill id (cancels previous collector)
    fun setCurrentSkillId(id: Long) {
        currentSkillJob?.cancel()
        currentSkillJob = repoScope.launch {
            skillDao.getSkill(id).collect { skill -> _currentSkill.value = skill?.toDomain() }
        }
    }

    // If you prefer a one-shot load (useful when Service starts and UI not running)
    suspend fun loadCurrentSkillOnce(id: Long) {
        val skill = skillDao.getSkillOnce(id)
        _currentSkill.value = skill?.toDomain()
    }

    fun getSkillById(id: Long): Flow<Skill?> {
        setCurrentSkillId(id)

        return skillDao
            .getSkill(id)
            .map { it?.toDomain() }
    }

    suspend fun updateSkill(skill: Skill) = withContext(Dispatchers.IO) {
        skillDao.update(skill.toEntity())
    }
}
