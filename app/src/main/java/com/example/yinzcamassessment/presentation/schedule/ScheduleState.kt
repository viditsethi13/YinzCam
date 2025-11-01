package com.example.yinzcamassessment.presentation.schedule

import com.example.yinzcamassessment.domain.model.GameDisplay

data class ScheduleState(
    val games: List<GameDisplay> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
