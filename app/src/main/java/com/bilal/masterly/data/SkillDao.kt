package com.bilal.masterly.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao {
    @Query("SELECT * FROM skills ORDER BY last_updated DESC")
    fun getAllSkills(): Flow<List<SkillEntity>>

    @Query("SELECT * FROM skills WHERE id = :id")
    fun getSkill(id: Long): Flow<SkillEntity?>

    @Query("SELECT * FROM skills WHERE id = :id")
    suspend fun getSkillOnce(id: Long): SkillEntity?

    @Insert
    suspend fun insert(skill: SkillEntity): Long

    @Update
    suspend fun update(skill: SkillEntity)

    @Delete
    suspend fun delete(skill: SkillEntity)
}
