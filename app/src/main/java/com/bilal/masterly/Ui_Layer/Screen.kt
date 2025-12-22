package com.bilal.masterly.Ui_Layer

object Screen {
    const val SkillList = "SkillListScreen"
    const val TimerPattern = "TimerScreen/{skillId}"
    fun timer(skillId: Long) = "TimerScreen/$skillId"
    const val DetailPattern = "SkillDetailScreen/{skillId}"
    fun detail(skillId: Long) = "SkillDetailScreen/$skillId"
}
