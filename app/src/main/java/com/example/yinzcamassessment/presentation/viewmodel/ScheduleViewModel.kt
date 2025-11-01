package com.example.yinzcamassessment.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yinzcamassessment.common.Resource
import com.example.yinzcamassessment.data.repository.APIRepositoryImpl
import com.example.yinzcamassessment.domain.model.GameDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ScheduleState(
    val games: List<GameDisplay> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ScheduleViewModel: ViewModel() {
    private val apiRepository = APIRepositoryImpl()

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    init{
        getScheduleData()
    }

    private fun getScheduleData() {
        viewModelScope.launch {
            apiRepository.getSchedules().onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = ScheduleState(isLoading = true)
                    }

                    is Resource.Success -> {

                        _state.value = ScheduleState(games = resource.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        _state.value = ScheduleState(
                            error = resource.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}