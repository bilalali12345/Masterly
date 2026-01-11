package com.bilal.masterly.Ui_Layer

object Screen {
    const val SkillList = "SkillListScreen"
    const val AddFirst = "AddFirstSkillScreen"
    const val Paywall = "PaywallScreen"
    const val Settings = "SettingsScreen"
    const val Analytics = "AnalyticsScreen"
    const val Timer = "TimerScreen"
    const val TimerPattern = "TimerScreen/{skillId}"
    fun timer(skillId: Long) = "TimerScreen/$skillId"
    const val DetailPattern = "SkillDetailScreen/{skillId}"
    fun detail(skillId: Long) = "SkillDetailScreen/$skillId"
}
