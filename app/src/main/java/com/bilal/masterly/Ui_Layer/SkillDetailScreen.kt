package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.R
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SkillDetailPreview(modifier: Modifier = Modifier) {
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
    SkillDetailScreen(
        skill = previewSkill,
        modifier = Modifier,
        onBack = { },
        onEdit = { },
        onDelete = { }
    )
}

val tabItems = listOf(
    "Overview",
    "Sessions",
    "Analytics",
    "Milestones",
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SkillDetailScreen(
    skill: Skill?,
    modifier: Modifier = Modifier,
    onCallTimerStart: () -> Unit = {},
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
) {


    val pagerState = rememberPagerState { tabItems.size }
    val scope = rememberCoroutineScope()

    // Use theme background
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            //Header with back button and edit/delete buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Back")
                }
                Row {
                    PillBtn(
                        icon = R.drawable.baseline_edit_note_24,
                        text = "Edit",
                        onClick = onEdit,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    PillBtn(
                        icon = R.drawable.baseline_delete_forever_24,
                        text = "Delete",
                        onClick = onDelete,
                        modifier = Modifier.padding(start = 10.dp),
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        borderColor = MaterialTheme.colorScheme.error
                    )
                }
            }

            //Skill Name
            Text(
                text = skill?.name.orEmpty(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            )

            // progress text and percentage
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress towards mastery",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = skill?.progressDisplay ?: "0 %",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            LinearProgressIndicator(
                progress = skill?.progressFraction ?: 0.0f,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(50))
            )

            //Total Hours text header
            Text(
                text = "Total Hours",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth()
            )

            //hours text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    text = "${skill?.hoursCompleted ?: 0}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "/ ${skill?.hoursTotal ?: 0} hrs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }

            //Start Timer and Quick Log btns
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientButton(
                    text = "Start Timer",
                    icon = Icons.Default.Timer,
                    onClick = onCallTimerStart,
                    modifier = Modifier.weight(1f),
                    height = 44.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                PillBtn(
                    icon = R.drawable.baseline_menu_book_24,
                    text = "Quick Log",
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    height = 44.dp
                )
            }


            //Tabs
            // Tabs: use pagerState.currentPage as selected index, and animate pager on click
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabItems.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index) // animate directly
                            }
                        },
                        text = {
                            Text(
                                title,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    )
                }
            }

            // Pager: bind to the same pagerState
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                Text(text = "Page ${tabItems[page]}")
            }
        }
    }
}

