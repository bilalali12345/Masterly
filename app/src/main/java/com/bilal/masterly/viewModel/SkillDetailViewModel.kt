// SkillDetailViewModel.kt
package com.bilal.masterly.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.MyApplication
import com.bilal.masterly.repository.SkillRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SkillDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SkillRepository
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

                return SkillDetailViewModel(
                    savedStateHandle,
                    (application as MyApplication).skillRepository,
                ) as T
            }
        }
    }

    private val skillId: Long? = savedStateHandle.get<Long>("skillId")

    val skillFlow: StateFlow<Skill?> = repository
        .getSkillFlow(skillId ?: -1L)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun deleteCurrentSkill() {
        val s = skillFlow.value ?: return
        viewModelScope.launch { repository.delete(s) }
    }

    fun updateSkill(updated: Skill) {
        viewModelScope.launch { repository.update(updated) }
    }
}
