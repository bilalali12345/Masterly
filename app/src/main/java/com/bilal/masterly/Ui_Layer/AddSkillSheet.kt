package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bilal.masterly.Domain_Layer.Skill

@Composable
fun AddSkillSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onAddSkill: (Skill) -> Unit
) {

    var skillNameText by rememberSaveable {
        mutableStateOf("")
    }
    var skillHoursText by rememberSaveable {
        mutableStateOf("")
    }
    var skillDeadlineDate by rememberSaveable {
        mutableStateOf("")
    }
    var showDatePicker by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Add New Skill",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        SkillNameField(
            value = skillNameText,
            onValueChange = { skillNameText = it }
        )

        TargetHoursField(
            value = skillHoursText,
            onValueChange = { skillHoursText = it }
        )

        DeadlineField(
            value = skillDeadlineDate,
            onClick = {
                showDatePicker = true
            }
        )

        ActionRow(
            onCancel = onDismiss,
            onAdd = {
                onAddSkill(
                    Skill(
                        id = System.currentTimeMillis(),
                        name = skillNameText,
                        hoursCompleted = 0,
                        hoursTotal = skillHoursText.toIntOrNull() ?: 0
                    )
                )
            }
        )
    }

    if (showDatePicker) {
        DatePickerPopup(
            onDateSelected = { date ->
                skillDeadlineDate = date
            },
            onDismiss = { showDatePicker = false }
        )
    }
}