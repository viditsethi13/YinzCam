package com.example.yinzcamassessment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.yinzcamassessment.presentation.viewmodel.ScheduleViewModel
import com.example.yinzcamassessment.ui.theme.YinzCamAssessmentTheme

class MainActivity : ComponentActivity() {
    private val scheduleViewModel: ScheduleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YinzCamAssessmentTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController, scheduleViewModel = scheduleViewModel)
            }
        }
    }
}