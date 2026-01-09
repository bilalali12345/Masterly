package com.bilal.masterly.data

// data/SkillEntity.kt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skills")
data class SkillEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val description: String? = null,
    @ColumnInfo(name = "hours_completed") val hoursCompleted: Int = 0,
    @ColumnInfo(name = "hours_total") val hoursTotal: Int = 0,
    @ColumnInfo(name = "icon_res") val iconRes: Int? = null,
    @ColumnInfo(name = "color_argb") val colorArgb: Int? = null,
    val level: Int? = null,
    @ColumnInfo(name = "is_pinned") val isPinned: Boolean = false,
    @ColumnInfo(name = "started_at") val startedAt: Long? = null,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = System.currentTimeMillis()
)
