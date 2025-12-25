package com.bilal.masterly.Ui_Layer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bilal.masterly.viewModel.AppViewModel
import com.bilal.masterly.viewModel.SkillDetailViewModel
import com.bilal.masterly.viewModel.TimerViewModel


@Composable
fun AppNavHost(
    navController: NavHostController,
    appViewModel: AppViewModel,
    startDestination: String,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("AddFirstSkillScreen") {
            AddFirstSkillScreen( onAddSkillClick = { appViewModel.requestShowAddSkillSheet() })
        }

        composable("SkillListScreen") {
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
            val timerVm: TimerViewModel = viewModel(backStackEntry)
            val skill by timerVm.skillFlow.collectAsState(initial = null)


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
            val detailVm: SkillDetailViewModel = viewModel(backStackEntry)
            val skill by detailVm.skillFlow.collectAsState(initial = null)
            SkillDetailScreen(
                skill = skill,
                onBack = { navController.popBackStack() })
        }
    }
}