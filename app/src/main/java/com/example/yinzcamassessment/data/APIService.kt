package com.example.yinzcamassessment.data

import com.example.yinzcamassessment.domain.TeamSchedule
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("/iOS/interviews/ScheduleExercise/schedule.json")
    suspend fun getSchedules(): TeamSchedule
}