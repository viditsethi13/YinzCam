package com.example.yinzcamassessment.domain.repository

import com.example.yinzcamassessment.common.Resource
import com.example.yinzcamassessment.domain.model.GameDisplay
import kotlinx.coroutines.flow.Flow

interface APIRepository {
    suspend fun getSchedules(): Flow<Resource<List<GameDisplay>>>
}