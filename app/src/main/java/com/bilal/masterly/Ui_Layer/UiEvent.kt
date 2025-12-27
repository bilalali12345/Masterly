package com.bilal.masterly.Ui_Layer

sealed interface UiEvent {
    data object ShowAddSkillSheet : UiEvent
    data object NavigateToSkillList : UiEvent
    data object RequestStartService : UiEvent
    data object RequestPauseService : UiEvent
    data object RequestStopService : UiEvent

}