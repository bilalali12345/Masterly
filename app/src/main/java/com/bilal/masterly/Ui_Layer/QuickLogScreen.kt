package com.bilal.masterly.Ui_Layer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bilal.masterly.Domain_Layer.Skill
import com.bilal.masterly.Domain_Layer.sampleSkills
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickLogScreen(skill: Skill?, onClose: () -> Unit) {
    var showSheet = (true)

    var selected by remember {
        mutableStateOf(skill ?: sampleSkills.first())
    }

    // Skip partial so show() => Expanded (full-screen)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scope = rememberCoroutineScope()

    // show immediately when composed
    LaunchedEffect(showSheet) {
        if (showSheet) sheetState.show()
    }

    // Intercept back while sheet is visible: hide the sheet (animate down), then remove it
    BackHandler(enabled = showSheet) {
        scope.launch {
            sheetState.hide()
            showSheet = false
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                // animate hide then remove from composition
                scope.launch {
                    sheetState.hide()
                    showSheet = false
                }
            },
            sheetMaxWidth = Dp.Unspecified
        ) {
            QuickLogSheetContent(
                selected = selected,
                onSelect = { selected = it },
                onClose = onClose
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickLogSheetContent(
    selected: Skill,
    onSelect: (Skill) -> Unit,
    onClose: () -> Unit
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    var isDropDownExpanded by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp) // force full-screen height for the sheet content
            .padding(horizontal = 0.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Bolt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "Quick Log",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                // optional close action
                TextButton(onClick = onClose) {
                    Text("Close")
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Log a practice session in seconds.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Rest of your full-screen sheet UI here...
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Select Skill",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = isDropDownExpanded,
                onExpandedChange = { isDropDownExpanded = !isDropDownExpanded },
                modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selected.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }

                }

                ExposedDropdownMenu(
                    expanded = isDropDownExpanded,
                    onDismissRequest = { isDropDownExpanded = false }
                ) {
                    sampleSkills.forEach { skill ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(skill.name, modifier = Modifier.weight(1f))
                                    if (skill == selected) {
                                        Icon(Icons.Default.Check, null)
                                    }
                                }
                            },
                        onClick = {
                            onSelect(skill)
                            isDropDownExpanded = false
                        })
                    }
                }
            }
        }
    }
}
