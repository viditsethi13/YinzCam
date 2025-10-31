package com.example.yinzcamassessment.data

import com.example.yinzcamassessment.domain.GameDisplay

interface APIRepository {
    suspend fun getSchedules():List<GameDisplay>
}