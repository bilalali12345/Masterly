package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bilal.masterly.MyApplication
import com.bilal.masterly.viewModel.AppViewModel
import com.bilal.masterly.viewModel.SkillDetailViewModel
import com.bilal.masterly.viewModel.TimerViewModel


@Composable
fun AppNavHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    startDestination: String,
    modifier: Modifier,
    onTimerEvent: (UiEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.AddFirst) {
            AddFirstSkillScreen( onAddSkillClick = { appViewModel.requestShowAddSkillSheet() })
        }

        composable(Screen.SkillList) {
            val skills by appViewModel.skillList.collectAsState()
            SkillListScreen(
                skills = skills,
                onNavigateToTimer = { id ->
                    navController.navigate(
                        Screen.timer(
                            id
                        )
                    )
                },
                onNavigateToDetails = { id ->
                    navController.navigate(
                        Screen.detail(
                            id
                        )
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable(
            route = Screen.TimerPattern,
            arguments = listOf(navArgument("skillId") {
                type = NavType.LongType
                nullable = false
            })
        ) { backStackEntry ->

            val timerVm: TimerViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = TimerViewModel.Factory
            )

            val skill by timerVm.skillFlow.collectAsState(initial = null)

            LaunchedEffect(key1 = timerVm) {
                timerVm.uiEvents.collect{
                    onTimerEvent(it)
                }
            }


            TimerScreen(
                skill = skill,
                timerVm = timerVm)
        }

        composable(
            route = Screen.DetailPattern,
            arguments = listOf(navArgument("skillId") {
                type = NavType.LongType
                nullable = false
            })
        ) { backStackEntry ->

            val detailVm: SkillDetailViewModel = viewModel(viewModelStoreOwner = backStackEntry ,factory = SkillDetailViewModel.Factory)
            val skill by detailVm.skillFlow.collectAsState(initial = null)
            SkillDetailScreen(
                skill = skill, onBack = { navController.popBackStack() },
                modifier = Modifier.fillMaxSize(),
                onCallTimerStart = { id ->
                    navController.navigate(
                        Screen.timer(
                            id
                        )
                    )
                },
                onEdit = {},
                onDelete = { detailVm.deleteCurrentSkill() }
            )
        }

        composable(Screen.Paywall) {
            PaywallScreen()
        }

        composable(Screen.Settings) {
            SettingsScreen()
        }

        composable(Screen.Analytics) {
            AnalyticsScreen()
        }
    }
}
