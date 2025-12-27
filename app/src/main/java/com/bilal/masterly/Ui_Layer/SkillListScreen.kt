package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bilal.masterly.Domain_Layer.Skill

@Composable
fun SkillListScreen(
    skills: List<Skill>,
    onNavigateToTimer: (Long) -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(items = skills, key = { it.id }) { skill ->
            SkillCard(
                skill = skill,
                onCardClick = { clickedSkill -> onNavigateToTimer(clickedSkill.id) },
                onDetailClick = { clickedSkill -> onNavigateToDetails(clickedSkill.id) }
            )
        }
    }
}