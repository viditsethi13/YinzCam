package com.example.yinzcamassessment.data.remote

import com.example.yinzcamassessment.data.remote.dto.TeamSchedule
import retrofit2.http.GET

interface APIService {
    @GET("/iOS/interviews/ScheduleExercise/schedule.json")
    suspend fun getSchedules(): TeamSchedule
}