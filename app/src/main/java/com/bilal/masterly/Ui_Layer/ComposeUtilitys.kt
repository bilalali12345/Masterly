package com.bilal.masterly.Ui_Layer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.util.Date
import java.util.Locale

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Previewer() {
    val sample = Skill(
        id = 1, name = "JavaScript Development", hoursCompleted = 1500, hoursTotal = 10000
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
//    AddSkillSheet(onDismiss = { }, onAddSkill = { })
    PaywallScreen()
}


@Composable
fun TimerControls(
    isRunning: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stop
        TimerControlButton(
            icon = R.drawable.stop_24px, onClick = onStop, size = 56.dp
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
            icon = R.drawable.restart_alt_24px, onClick = { /* reset */ }, size = 56.dp
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
    isPrimary: Boolean = false,
) {
    val color = MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(
            strokeWidth, color.copy(alpha = if (isPrimary) 1f else 0.6f)
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
            .height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerPopup(
    onDateSelected: (String) -> Unit, onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
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
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
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
            onClick = onAdd, shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Add Skill")
        }
    }
}


@Composable
fun DeadlineField(
    value: String, onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null, // remove ripple over text field
                interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            }) {
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
        value = value,
        onValueChange = { input ->
            if (input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        label = { Text("Skill Hours") },
        placeholder = { Text(text = "eg ., 1000") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock), contentDescription = null
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SkillNameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Skill Name") },
        placeholder = { Text(text = "eg ., Machine Learning") },
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.edit_24dp), contentDescription = null
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
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
            .clickable { onCardClick(skill) }) {
        Column(modifier = Modifier.padding(12.dp)) {
            // title row
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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

@Composable
fun PillBtn(
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 44.dp, // make height configurable
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.primary
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(height), // use the passed height
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = BorderStroke(width = 1.dp, color = borderColor),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = LocalContentColor.current,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = LocalContentColor.current,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Default.Timer,
    onClick: () -> Unit,
    gradient: Brush = Brush.horizontalGradient(listOf(Color(0xFF4C1D71), Color(0xFF7C3AED))),
    cornerRadius: Dp = 20.dp,
    height: Dp = 44.dp // make height configurable
) {
    val shape = RoundedCornerShape(cornerRadius)

    Button(
        onClick = onClick,
        modifier = modifier
            .height(height) // use the passed height
            .clip(shape)
            .background(brush = gradient, shape = shape)
            .shadow(elevation = 6.dp, shape = shape),
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
            }

            Text(text = text, style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}



@Composable
fun MonthCalendar(
    yearMonth: YearMonth,
    practicedDates: Set<LocalDate>,
    modifier: Modifier = Modifier,
    cellSize: Dp = 48.dp,
    onDateClick: (LocalDate) -> Unit = {}
) {
    val today = LocalDate.now()
    val firstOfMonth = yearMonth.atDay(1)

    // If you prefer Monday-first, change this logic:
    // val offset = (firstOfMonth.dayOfWeek.value + 6) % 7 // Monday-first
    val offset = firstOfMonth.dayOfWeek.value % 7 // Sunday-first: Sunday -> 0

    val daysInMonth = yearMonth.lengthOfMonth()
    val totalCells = 7 * 6 // 6 rows ensures consistent layout

    Column(modifier = modifier) {
        // Weekday header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("S","M","T","W","T","F","S").forEach { wd ->
                Box(
                    modifier = Modifier
                        .size(cellSize)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        wd,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid of day cells
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height((cellSize * 6) + 8.dp), // fixed height for 6 rows
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                items((0 until totalCells).toList()) { index ->
                    val dayNumber = index - offset + 1
                    if (dayNumber in 1..daysInMonth) {
                        val date = yearMonth.atDay(dayNumber)
                        DayCell(
                            date = date,
                            isToday = date == today,
                            practiced = practicedDates.contains(date),
                            size = cellSize,
                            onClick = { onDateClick(date) }
                        )
                    } else {
                        // empty placeholder
                        Box(modifier = Modifier.size(cellSize))
                    }
                }
            }
        )
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    isToday: Boolean,
    practiced: Boolean,
    size: Dp,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .size(size)
            .padding(4.dp)
            .clip(shape)
            .semantics { contentDescription = "Day ${date.dayOfMonth}" }
            .clickable { onClick() },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            // Day number with optional today highlight
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .then(
                        if (isToday) Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f))
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // small practiced indicator (dot)
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (practiced) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
                    )
            )
        }
    }
}
