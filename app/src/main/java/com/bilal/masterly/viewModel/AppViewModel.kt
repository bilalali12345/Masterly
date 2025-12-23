package com.bilal.masterly.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bilal.masterly.Domain_Layer.Skill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel : ViewModel() {

    private val _navigateToSkillList = MutableStateFlow(false)
    val navigateToSkillList: StateFlow<Boolean> = _navigateToSkillList.asStateFlow()

    private val _skillList = MutableStateFlow<List<Skill>>(emptyList())
    val skillList: StateFlow<List<Skill>> = _skillList.asStateFlow()

    init {

        viewModelScope.launch {
            _skillList.value = loadSkillsFromDb()
            onFirstSkillAdded()
        }

    }

    fun onFirstSkillAdded() {
        _navigateToSkillList.value = true
    }

    fun consumeNavigation() {
        _navigateToSkillList.value = false
    }

    // Simulated DB fetch - replace with repository/db call in real app
    private suspend fun loadSkillsFromDb(): List<Skill> = withContext(Dispatchers.Default) {
        delay(200) // simulate IO
        val list = listOf(
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
        return@withContext list
    }

    // Mutators - update list immutably
    fun addSkill(skill: Skill) {
        _skillList.value = _skillList.value + skill
        onFirstSkillAdded()
    }

    fun removeSkillById(id: Long) {
        _skillList.value = _skillList.value.filterNot { it.id == id }
    }


}