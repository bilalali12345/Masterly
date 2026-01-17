package com.bilal.masterly.Ui_Layer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bilal.masterly.Notification.TimerService
import com.bilal.masterly.ui.theme.MasterlyTheme
import com.bilal.masterly.viewModel.AppViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : ComponentActivity() {
    // holder for one pending action while permission prompt is inflight
    private var pendingNotificationPermissionAction: ((Context) -> Unit)? = null

    // register launcher once as a property (calls pending action when granted)
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val pending = pendingNotificationPermissionAction
            pendingNotificationPermissionAction = null

            if (granted) {
                // run pending action if any; prefer Activity context
                pending?.invoke(this)
            } else {
                // optional: inform user that permission is required for foreground notifications
                Toast.makeText(
                    this,
                    "Notification permission is required to show the timer notification.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun ensureNotificationPermissionThen(action: (Context) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                action(this)
            } else {
                // keep pending action and request permission
                pendingNotificationPermissionAction = action

                // Optional: show rationale before direct request
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // you can show a dialog explaining why you need it; here we simply request
                }

                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // pre-Android 13: no runtime notification permission required
            action(this)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MasterlyTheme {

                val navController = rememberNavController()
                val appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)

                var showBottomSheet by rememberSaveable { mutableStateOf(false) }
                val isInitialized by appViewModel.isInitialized.collectAsState()

                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                val onTimerEvent = remember {
                    { event: UiEvent ->
                        when (event) {
                            UiEvent.RequestStartService -> {
                                ensureNotificationPermissionThen { ctx ->
                                    ContextCompat.startForegroundService(
                                        ctx, Intent(
                                            ctx,
                                            TimerService::class.java
                                        ).apply {
                                            action = TimerService.ACTION_START
                                        })
                                }
                            }

                            UiEvent.RequestPauseService -> {
                                application.startService(
                                    Intent(
                                        application,
                                        TimerService::class.java
                                    ).apply { action = TimerService.ACTION_PAUSE })
                            }

                            UiEvent.RequestStopService -> {
                                application.startService(
                                    Intent(
                                        application,
                                        TimerService::class.java
                                    ).apply { action = TimerService.ACTION_STOP })
                            }

                            else -> {}
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isInitialized) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Scaffold(
                            containerColor = MaterialTheme.colorScheme.background,
                            topBar = {
                                TopBar(
                                    onAnalyticsClick = {
                                        navController.navigate(Screen.Analytics) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onSettingsClick = {
                                        navController.navigate(Screen.Settings) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onProClick = {
                                        navController.navigate(Screen.Paywall) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            },
                            floatingActionButton = {
                                if (currentRoute == Screen.SkillList) {
                                    FloatingActionButton(
                                        onClick = { appViewModel.requestShowAddSkillSheet() }
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Add Skill"
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->

                            // compute startDestination where you already have isInitialized and skills
                            val skills by appViewModel.skillList.collectAsState()
                            val startDestination =
                                if (skills.isEmpty()) Screen.AddFirst else Screen.SkillList

                            AppNavHost(
                                navController = navController,
                                appViewModel = appViewModel,
                                startDestination = startDestination,
                                modifier = Modifier.padding(innerPadding),
                                onTimerEvent = onTimerEvent as (UiEvent) -> Unit
                            )

                        }


                        LaunchedEffect(Unit) {
                            appViewModel.uiEvents
                                .onEach { event ->
                                    when (event) {
                                        UiEvent.ShowAddSkillSheet -> showBottomSheet = true
                                        UiEvent.NavigateToSkillList -> navController.navigate("SkillListScreen") {
                                            popUpTo("AddFirstSkillScreen") { inclusive = true }
                                        }

                                        else -> {}
                                    }
                                }
                                .launchIn(this)

                            // handle intent immediately (still in same effect)
                            intent?.let {
                                val destination = it.getStringExtra("navigate_to")
                                val skillId = it.getStringExtra("skill_id")?.toLongOrNull()
                                if (destination == Screen.Timer && skillId != null) {
                                    navController.navigate(Screen.timer(skillId))
                                }
                            }

                            // effect will keep the launched collector alive until disposed
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