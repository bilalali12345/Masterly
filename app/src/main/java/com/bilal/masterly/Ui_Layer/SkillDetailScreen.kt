package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bilal.masterly.Domain_Layer.Skill

@Composable
fun SkillDetailScreen(skill: Skill?, onBack: () -> Boolean) {
    Text(
        text = "SkillDetailScreen Screen",
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )

}