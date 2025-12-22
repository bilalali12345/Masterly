package com.bilal.masterly.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.bilal.masterly.Domain_Layer.Skill
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SkillDetailViewModel(savedStateHandle : SavedStateHandle) : ViewModel() {
    private val skillId = savedStateHandle.get<Long>("skillId")!!
    val skillFlow = flow<Skill> {
        delay(100)
        Skill(id = skillId, name = "JavaScript Development", hoursCompleted = 1500, hoursTotal = 10000)
    }
}