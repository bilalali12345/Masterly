package com.bilal.masterly.Domain_Layer

import androidx.annotation.DrawableRes
import com.bilal.masterly.R
import kotlin.math.roundToInt

data class Skill(
    val id: Long = 0L,
    val name: String,
    val description: String? = null,
    val hoursCompleted: Int = 0,
    val hoursTotal: Int = 0,
    @field:DrawableRes val iconRes: Int? = null,
    val colorArgb: Int? = null,
    val level: Int? = null,
    val isPinned: Boolean = false,
    val startedAt: Long? = null,
    val lastUpdated: Long = System.currentTimeMillis(),
) {
    /** Fractional progress 0.0..1.0 — use for LinearProgressIndicator(progress = progressFraction) */
    val progressFraction: Float
        get() = if (hoursTotal > 0) (hoursCompleted.toFloat() / hoursTotal).coerceIn(0f, 1f) else 0f

    /** Progress as an integer percent 0..100 */
    val progressPercentage: Int
        get() = (progressFraction * 100).roundToInt().coerceIn(0, 100)

    /** Human friendly progress string like "13%" */
    val progressDisplay: String
        get() = "${progressPercentage}%"

    /** Convenience: update completed hours (returns a copy) */
    fun withUpdatedHours(completed: Int, updatedAt: Long = System.currentTimeMillis()): Skill =
        copy(hoursCompleted = completed.coerceAtLeast(0), lastUpdated = updatedAt)

    /** Convenience: mark skill as completed */
    fun markComplete(): Skill =
        copy(hoursCompleted = hoursTotal, lastUpdated = System.currentTimeMillis())
}
