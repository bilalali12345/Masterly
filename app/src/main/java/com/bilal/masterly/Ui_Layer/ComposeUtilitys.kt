package com.bilal.masterly.Ui_Layer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.R
import com.bilal.masterly.viewModel.SkillDetailViewModel

@Preview(showBackground = true)
@Composable
private fun Previewer() {
    /* val sample = Skill(
         id = 1,
         name = "JavaScript Development",
         hoursCompleted = 1500,
         hoursTotal = 10000
     )

     MasterlyTheme {
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
}

@Composable
fun TimerScreen(skill: Skill?, onBack: () -> Boolean) {
     Text(text = "Timer Screen" , modifier = Modifier.fillMaxSize().background(Color.White))
}

@Composable
fun SkillDetailScreen(skill: Skill?, onBack: () -> Boolean) {
    Text(text = "SkillDetailScreen Screen" , modifier = Modifier.fillMaxSize().background(Color.White))

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
fun AddFirstSkillScreen() {

}

@Composable
fun AddSkillSheet(modifier: Modifier = Modifier, onDismiss: () -> Unit , onAddSkill : (Skill) -> Unit) {

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

