package com.bilal.masterly.repository

import com.bilal.masterly.Domain_Layer.Skill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

object TimerRepository {

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    var startTimestampMillis: Long? = null

    fun updateElapsed(sec: Long) { _elapsedSeconds.value = sec }
    fun setRunning(r: Boolean) { _isRunning.value = r }

    var currentSkillId: Long? = null

    // Simulated DB
    private val skills = mutableListOf(
        Skill(id = 1, name = "JavaScript Development", hoursCompleted = 1500, hoursTotal = 10000),
        Skill(id = 2, name = "Kotlin & Compose", hoursCompleted = 120, hoursTotal = 200),
        Skill(id = 3, name = "Android Architecture", hoursCompleted = 40, hoursTotal = 100),
        Skill(id = 4, name = "Data Structures", hoursCompleted = 30, hoursTotal = 60),
        Skill(id = 5, name = "UI/UX Basics", hoursCompleted = 10, hoursTotal = 20),
        Skill(id = 6, name = "Flutter Basics", hoursCompleted = 5, hoursTotal = 10),
        Skill(id = 7, name = "Java Basics", hoursCompleted = 3, hoursTotal = 6),
        Skill(id = 8, name = "Python Basics", hoursCompleted = 2, hoursTotal = 4),
        Skill(id = 9, name = "C++ Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 10, name = "Swift Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 11, name = "Go Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 12, name = "Rust Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 13, name = "PHP Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 14, name = "Ruby Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 15, name = "HTML Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 16, name = "CSS Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 17, name = "SQL Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 18, name = "NoSQL Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 19, name = "Git Basics", hoursCompleted = 1, hoursTotal = 2),
        Skill(id = 20, name = "Docker Basics", hoursCompleted = 1, hoursTotal = 2),
    )

    suspend fun getSkillById(id: Long): Skill? = withContext(Dispatchers.IO) {
        val currentSkill = skills.find { it.id == id }
        if (currentSkill != null) {
            currentSkillId = currentSkill.id
        }
        return@withContext currentSkill
    }

    suspend fun updateSkill(skill: Skill) = withContext(Dispatchers.IO) {
        val index = skills.indexOfFirst { it.id == skill.id }
        if (index != -1) {
            skills[index] = skill
        }
    }
}