package com.example.yinzcamassessment.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yinzcamassessment.data.APIRepositoryImpl
import com.example.yinzcamassessment.domain.GameDisplay
import kotlinx.coroutines.launch

class ScheduleViewModel: ViewModel() {
    private val apiRepository = APIRepositoryImpl()

    private val _isLoading = mutableStateOf<Boolean>(false)
    val isLoading: State<Boolean> = _isLoading

    private val _gameDisplay = mutableStateOf<List<GameDisplay>>(emptyList())
    val gameDisplay: State<List<GameDisplay>> = _gameDisplay


    fun getSchedule() {
        if(_gameDisplay.value.isNotEmpty()){
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _gameDisplay.value = apiRepository.getSchedules()
            _isLoading.value = false
        }
    }
}