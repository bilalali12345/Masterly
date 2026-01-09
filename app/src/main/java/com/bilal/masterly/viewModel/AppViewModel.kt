package com.bilal.masterly.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.MyApplication
import com.bilal.masterly.Ui_Layer.UiEvent
import com.bilal.masterly.repository.SkillRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val repository: SkillRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return AppViewModel(
                    (application as MyApplication).skillRepository,
                ) as T
            }
        }
    }

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    val skillList: StateFlow<List<Skill>> = repository.getAllSkills()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // explicit init flag the UI can observe
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllSkills().first()
            _isInitialized.value = true
        }
    }

    fun requestShowAddSkillSheet() {
        viewModelScope.launch { _uiEvents.emit(UiEvent.ShowAddSkillSheet) }
    }

    fun addSkill(skill: Skill) {
        viewModelScope.launch {
            repository.insert(skill)
        }
    }

    fun removeSkill(skill: Skill) {
        viewModelScope.launch {
            repository.delete(skill)
        }
    }
}
