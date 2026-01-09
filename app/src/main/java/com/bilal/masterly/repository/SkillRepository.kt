package com.bilal.masterly.repository

import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.data.SkillDao
import com.bilal.masterly.data.toEntity
import com.bilal.masterly.data.toDomain
import kotlinx.coroutines.flow.map

class SkillRepository(private val dao: SkillDao) {
    // flow of domain models
    fun getAllSkills() = dao.getAllSkills().map { list -> list.map { it.toDomain() } }

    fun getSkillFlow(id: Long) = dao.getSkill(id).map { it?.toDomain() }

    suspend fun getSkillOnce(id: Long) = dao.getSkillOnce(id)?.toDomain()

    suspend fun insert(skill: Skill): Long = dao.insert(skill.toEntity())
    suspend fun update(skill: Skill) = dao.update(skill.toEntity())
    suspend fun delete(skill: Skill) = dao.delete(skill.toEntity())
}

