package com.bilal.masterly.data

import com.bilal.masterly.Domain_Layer.Skill

// data/mappers.kt
fun SkillEntity.toDomain(): Skill = Skill(
    id = id,
    name = name,
    description = description,
    hoursCompleted = hoursCompleted,
    hoursTotal = hoursTotal,
    iconRes = iconRes,
    colorArgb = colorArgb,
    level = level,
    isPinned = isPinned,
    startedAt = startedAt,
    lastUpdated = lastUpdated
)

fun Skill.toEntity(): SkillEntity = SkillEntity(
    id = id,
    name = name,
    description = description,
    hoursCompleted = hoursCompleted,
    hoursTotal = hoursTotal,
    iconRes = iconRes,
    colorArgb = colorArgb,
    level = level,
    isPinned = isPinned,
    startedAt = startedAt,
    lastUpdated = lastUpdated
)
