package com.example.yinzcamassessment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.yinzcamassessment.presentation.schedule.ScheduleViewModel
import com.example.yinzcamassessment.ui.theme.YinzCamAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YinzCamAssessmentTheme {
                val navController = rememberNavController()
                val scheduleViewModel = hiltViewModel<ScheduleViewModel>()
                AppNavigation(navController = navController, scheduleViewModel = scheduleViewModel)
            }
        }
    }
}