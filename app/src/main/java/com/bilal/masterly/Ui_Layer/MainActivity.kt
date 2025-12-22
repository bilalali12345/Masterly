package com.bilal.masterly.Ui_Layer

import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bilal.masterly.ui.theme.MasterlyTheme
import com.bilal.masterly.viewModel.AppViewModel
import com.bilal.masterly.viewModel.SkillDetailViewModel
import com.bilal.masterly.viewModel.TimerViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MasterlyTheme {

                val navController = rememberNavController()
                val appViewModel: AppViewModel = viewModel()

                val isSingleSkillAdded by appViewModel.isSingleSkillAdded.collectAsState()
                var showBottomSheet by rememberSaveable { mutableStateOf(false) }

                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        topBar = { TopBar() },
                        floatingActionButton = {
                            if (currentRoute == "SkillListScreen") {
                                FloatingActionButton(
                                    onClick = { showBottomSheet = true }
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add Skill"
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->

                        NavHost(
                            navController = navController,
                            startDestination = "AddFirstSkillScreen",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("AddFirstSkillScreen") {
                                AddFirstSkillScreen()
                            }

                            composable("SkillListScreen") {
                                val skills by appViewModel.skillList.collectAsState()
                                SkillListScreen(
                                    skills = skills,
                                    onNavigateToTimer = { id -> navController.navigate(Screen.timer(id)) },
                                    onNavigateToDetails = { id -> navController.navigate(Screen.detail(id)) },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            composable(
                                route = Screen.TimerPattern,
                                arguments = listOf(navArgument("skillId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val timerVm: TimerViewModel = viewModel(backStackEntry)
                                val skill by timerVm.skillFlow.collectAsState(initial = null)


                                TimerScreen(skill = skill, onBack = { navController.popBackStack() })
                            }

                            composable(
                                route = Screen.DetailPattern,
                                arguments = listOf(navArgument("skillId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val detailVm: SkillDetailViewModel = viewModel(backStackEntry)
                                val skill by detailVm.skillFlow.collectAsState(initial = null)
                                SkillDetailScreen(skill = skill , onBack = { navController.popBackStack() })
                            }
                        }
                    }

                    // Navigation side-effect (correct place)
                    LaunchedEffect(isSingleSkillAdded) {
                        if (isSingleSkillAdded) {
                            navController.navigate("SkillListScreen") {
                                popUpTo("AddFirstSkillScreen") { inclusive = true }
                            }
                        }
                    }

                    // Bottom sheet (single owner: App shell)
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false }
                        ) {
                            AddSkillSheet(
                                onDismiss = { showBottomSheet = false },
                                onAddSkill = { skill ->
                                    appViewModel.addSkill(skill)
                                    showBottomSheet = false
                                }
                            )
                        }
                    }
                }
            }
        }

        window.decorView.post {
            applyWindowInsets()
        }
    }

    private fun applyWindowInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}