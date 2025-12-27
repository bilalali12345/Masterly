package com.bilal.masterly.Ui_Layer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.R
import com.bilal.masterly.viewModel.TimerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Previewer() {
    val sample = Skill(
        id = 1,
        name = "JavaScript Development",
        hoursCompleted = 1500,
        hoursTotal = 10000
    )

    /*MasterlyTheme {
       // background from theme
       Surface(
           modifier = Modifier.fillMaxSize(),
           color = MaterialTheme.colorScheme.background
       ) {
           // center preview
           Column(modifier = Modifier.padding(16.dp)) {
               SkillCard(skill = sample)
           }
       }
   }*/

//    TopBar {  }
//    TimerScreen(sample, { true })
    AddSkillSheet(
        onDismiss = { },
        onAddSkill = { }
    )
}


@Composable
fun TimerControls(
    isRunning: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stop
        TimerControlButton(
            icon = R.drawable.stop_24px,
            onClick = onStop,
            size = 56.dp
        )

        // Play / Pause (primary)
        TimerControlButton(
            icon = if (isRunning) R.drawable.pause_24px else R.drawable.play_arrow_24px,
            onClick = if (isRunning) onPause else onPlay,
            size = 72.dp,
            isPrimary = true
        )

        // Optional: reset / lap / skip
        TimerControlButton(
            icon = R.drawable.restart_alt_24px,
            onClick = { /* reset */ },
            size = 56.dp
        )
    }
}


@Composable
fun TimerControlButton(
    icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    strokeWidth: Dp = 1.5.dp,
    isPrimary: Boolean = false
) {
    val color = MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(
            strokeWidth,
            color.copy(alpha = if (isPrimary) 1f else 0.6f)
        ),
        tonalElevation = 0.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(if (isPrimary) 28.dp else 24.dp)
            )
        }
    }
}




@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    // optional callbacks for actions
    onAnalyticsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProClick: () -> Unit = {},
) {
    // Use a Surface so the bar uses the theme's surface color and provides proper content color
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Masterly",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Track your journey to mastery",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }

            // action icons + labels
            DifferentScreenOption(
                text = "Analytics",
                id = R.drawable.ic_analytics,
                onClick = onAnalyticsClick
            )

            DifferentScreenOption(
                text = "Settings",
                id = R.drawable.ic_settings,
                onClick = onSettingsClick
            )

            DifferentScreenOption(
                text = "Pro",
                id = R.drawable.ic_pro,
                highlight = true, // shows primary color for pro
                onClick = onProClick
            )
        }
    }
}

@Composable
fun DifferentScreenOption(
    text: String,
    @DrawableRes id: Int,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(start = 8.dp)
            .clickable(onClick = onClick)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id),
            contentDescription = text,
            tint = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(28.dp)
                .padding(horizontal = 4.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 1.dp)
        )
    }
}


@Composable
fun AddFirstSkillScreen(
    onAddSkillClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Icon / visual anchor
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Add your first skill",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start tracking your progress and build mastery one session at a time.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onAddSkillClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Skill")
            }
        }
    }
}


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
    var selectedDate by rememberSaveable { mutableStateOf("") }



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
                selectedDate = date
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerPopup(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val millis = datePickerState.selectedDateMillis
                if (millis != null) {
                    val formattedDate = formatDate(millis)
                    onDateSelected(formattedDate)
                }
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun formatDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


@Composable
fun ActionRow(onCancel: () -> Unit, onAdd: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onCancel) {
            Text(text = "Cancel")
        }

        Spacer(modifier = Modifier.width(6.dp))

        Button(
            onClick = onAdd,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Add Skill")
        }
    }
}


@Composable
fun DeadlineField(
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null, // remove ripple over text field
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Deadline to Complete") },
            trailingIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.event_24dp),
                        contentDescription = "Pick date"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

    }
}


@Composable
fun TargetHoursField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text("Skill Hours") },
        placeholder = { Text(text = "eg ., 1000")},
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SkillNameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text("Skill Name") },
        placeholder = { Text(text = "eg ., Machine Learning")},
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.edit_24dp), contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

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

@Composable
fun SkillCard(
    skill: Skill,
    onCardClick: (Skill) -> Unit,
    onDetailClick: (Skill) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onCardClick(skill) }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // title row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = skill.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { onDetailClick(skill) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // hours row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${skill.hoursCompleted} / ${skill.hoursTotal} hrs",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // progress row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = skill.progressDisplay,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            LinearProgressIndicator(
                progress = skill.progressFraction,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(50))
            )
        }
    }
}

