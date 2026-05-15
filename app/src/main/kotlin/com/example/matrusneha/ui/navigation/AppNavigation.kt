package com.example.matrusneha.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.matrusneha.ui.screens.danger.DangerSignsScreen
import com.example.matrusneha.ui.screens.danger.EmergencyContactsScreen
import com.example.matrusneha.ui.screens.growth.BabyGrowthScreen
import com.example.matrusneha.ui.screens.health.HealthScreen
import com.example.matrusneha.ui.screens.calendar.CalendarScreen
import com.example.matrusneha.ui.screens.home.HomeScreen
import com.example.matrusneha.ui.screens.kickcounter.KickCounterScreen
import com.example.matrusneha.ui.screens.nutrition.NutritionScreen
import com.example.matrusneha.ui.screens.onboarding.OnboardingScreen
import com.example.matrusneha.ui.screens.profile.ProfileScreen
import com.example.matrusneha.ui.screens.profile.RegistrationScreen
import com.example.matrusneha.ui.screens.splash.SplashScreen
import com.example.matrusneha.ui.viewmodel.MainViewModel

sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Splash : Screen("splash", "Splash", { })
    object Onboarding : Screen("onboarding", "Onboarding", { })
    object Registration : Screen("registration", "Registration", { })
    
    // Bottom Nav Tabs
    object Home : Screen("home", "Home", { Icon(Icons.Default.Home, contentDescription = "Home") })
    object Health : Screen("health", "Health", { Icon(Icons.Default.Favorite, contentDescription = "Health") })
    object Calendar : Screen("calendar", "Calendar", { Icon(Icons.Default.DateRange, contentDescription = "Calendar") })
    object Growth : Screen("growth", "Growth", { Icon(Icons.Default.Face, contentDescription = "Baby Growth") })
    object Profile : Screen("profile", "Profile", { Icon(Icons.Default.Person, contentDescription = "Profile") })
    
    // Sub-screens
    object KickCounter : Screen("kickcounter", "Kick Count", { })
    object Nutrition : Screen("nutrition", "Nutrition", { })
    object DangerSigns : Screen("dangersigns", "Danger", { })
    object EmergencyContacts : Screen("emergencycontacts", "Emergency", { })
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    
    val profile by viewModel.profile.collectAsState()
    val todayKickCount by viewModel.todayKickCount.collectAsState()
    val nutritionLogs by viewModel.todayNutritionLogs.collectAsState()
    val aiSuggestion by viewModel.aiSuggestion.collectAsState()
    val kickHistory by viewModel.last7DaysKicks.collectAsState()
    val reminders by viewModel.reminders.collectAsState()

    val bottomBarItems = listOf(
        Screen.Home,
        Screen.Health,
        Screen.Calendar,
        Screen.Growth,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            val showBottomBar = bottomBarItems.any { it.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    bottomBarItems.forEach { screen ->
                        NavigationBarItem(
                            icon = screen.icon,
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = androidx.compose.ui.graphics.Color.White,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onLanguageSelected = { 
                    navController.navigate(Screen.Onboarding.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
                })
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(onFinish = {
                    if (profile == null) {
                        navController.navigate(Screen.Registration.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
                    } else {
                        navController.navigate(Screen.Home.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
                    }
                })
            }
            composable(Screen.Registration.route) {
                RegistrationScreen(onRegistrationComplete = { name, age, village, month, phone, bg ->
                    viewModel.saveProfile(name, age, village, month, phone, bg)
                    navController.navigate(Screen.Home.route) { popUpTo(Screen.Registration.route) { inclusive = true } }
                })
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    profile = profile,
                    kickCount = todayKickCount,
                    nutritionLogs = nutritionLogs,
                    aiSuggestion = aiSuggestion,
                    onNavigateToKickCounter = { navController.navigate(Screen.KickCounter.route) },
                    onNavigateToNutrition = { navController.navigate(Screen.Nutrition.route) },
                    onNavigateToDangerSigns = { navController.navigate(Screen.DangerSigns.route) },
                    onNavigateToGrowth = { navController.navigate(Screen.Growth.route) },
                    onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) }
                )
            }
            composable(Screen.Health.route) {
                HealthScreen(
                    onNavigateToKickCounter = { navController.navigate(Screen.KickCounter.route) },
                    onNavigateToNutrition = { navController.navigate(Screen.Nutrition.route) }
                )
            }
            composable(Screen.Calendar.route) { 
                CalendarScreen(
                    reminders = reminders,
                    onSaveReminder = { title, date -> viewModel.saveReminder(title, date) }
                ) 
            }
            composable(Screen.Growth.route) { 
                val currentWeek = profile?.pregnancyMonth?.times(4) ?: 1
                BabyGrowthScreen(currentWeek = currentWeek) 
            }
            composable(Screen.Profile.route) { 
                ProfileScreen(
                    profile = profile,
                    onLogout = {
                        viewModel.logout()
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                ) 
            }
            
            // Sub-screens
            composable(Screen.KickCounter.route) { 
                KickCounterScreen(
                    todayKickCount = todayKickCount,
                    history = kickHistory,
                    onRecordKick = { viewModel.recordKick() }
                ) 
            }
            composable(Screen.Nutrition.route) { 
                NutritionScreen(nutritionLogs = nutritionLogs, onToggleLog = { log, checked -> viewModel.toggleNutritionLog(log, checked) }) 
            }
            composable(Screen.DangerSigns.route) { DangerSignsScreen(onNavigateToContacts = { navController.navigate(Screen.EmergencyContacts.route) }) }
            composable(Screen.EmergencyContacts.route) { 
                EmergencyContactsScreen(
                    profile = profile,
                    onSave = { doctor, asha, family -> viewModel.saveEmergencyContacts(doctor, asha, family) }
                ) 
            }
        }
    }
}
