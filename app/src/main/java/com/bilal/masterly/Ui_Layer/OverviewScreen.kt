package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bilal.masterly.Domain_Layer.Skill
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OverviewPreview(modifier: Modifier = Modifier) {
    val previewSkill = Skill(
        id = 1L,
        name = "Kotlin & Jetpack Compose",
        description = "Building modern Android UIs with Compose",
        hoursCompleted = 120,
        hoursTotal = 200,
        iconRes = null, // replace with an existing drawable
        colorArgb = 0xFF6200EE.toInt(),   // Material purple
        level = 3,
        isPinned = true,
        startedAt = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000), // 30 days ago
        lastUpdated = System.currentTimeMillis()
    )
    OverviewScreen(
        skill = previewSkill,
    )
}

@Composable
fun OverviewScreen(skill: Skill?) {
    // Use theme background
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 6.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.borderModifier()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Weekly Summary",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp)
                    )

                    val stats = listOf(
                        WeekStat("Week 1", 12),
                        WeekStat("Week 2", 8),
                        WeekStat("Week 3", 15),
                        WeekStat("Week 4", 10)
                    )

                    WeeklyBarChart(
                        data = stats,
                        modifier = Modifier
                            .fillMaxWidth()
                    )


                }

            }

            Row(
                modifier = Modifier.borderModifier()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Monthly Summary",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Month Icon",
                            tint = LocalContentColor.current,
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .size(22.dp)
                        )
                    }

                    MonthCalendar(
                        yearMonth = YearMonth.now(), practicedDates = setOf(
                            LocalDate.of(2026, Month.JANUARY, 15)
                        )
                    )
                }
            }
        }

    }
}

@Composable
private fun Modifier.borderModifier(): Modifier {
    val shape = RoundedCornerShape(8.dp)
    return this
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .shadow(
            elevation = 8.dp,
            shape = shape,
            clip = false
        )
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = shape
        )
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = shape
        )
}

@Composable
fun WeeklyBarChart(
    data: List<WeekStat>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOf { it.hours }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach {
            BarItem(
                label = it.label,
                value = it.hours,
                maxValue = maxValue,
                maxBarHeight = 140.dp,
                barColor = Color(0xFF6F8F80)
            )
        }
    }
}

@Composable
fun BarItem(
    label: String,
    value: Int,
    maxValue: Int,
    maxBarHeight: Dp,
    barColor: Color
) {
    val heightRatio = value.toFloat() / maxValue

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.width(72.dp)
    ) {

        Box(
            modifier = Modifier
                .height(maxBarHeight * heightRatio)
                .fillMaxWidth()
                .background(
                    color = barColor,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                )
        )

        Spacer(Modifier.height(12.dp))

        Text(label, style = MaterialTheme.typography.bodySmall)
        Text("${value}h", style = MaterialTheme.typography.titleMedium)
    }
}
