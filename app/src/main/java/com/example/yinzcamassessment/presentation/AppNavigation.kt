package com.example.yinzcamassessment.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.yinzcamassessment.presentation.schedule.ScheduleScreen
import com.example.yinzcamassessment.presentation.schedule.ScheduleViewModel

object AppRoutes{
    const val SCHEDULE_SCREEN = "scheduleScreen"
}
@Composable
fun AppNavigation(navController: NavHostController, scheduleViewModel: ScheduleViewModel) {

    NavHost(navController = navController, startDestination = AppRoutes.SCHEDULE_SCREEN)
    {
        composable(AppRoutes.SCHEDULE_SCREEN) {
            ScheduleScreen(scheduleViewModel)
        }
    }
}