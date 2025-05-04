package com.example.minesweeper.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _selectedDifficulty = MutableStateFlow("Normal")
    val selectedDifficulty: StateFlow<String> = _selectedDifficulty

    private val _selectedTime = MutableStateFlow(2) // tiempo en minutos
    val selectedTime: StateFlow<Int> = _selectedTime

    fun setDifficulty(level: String) {
        _selectedDifficulty.value = level
    }

    fun setTime(time: Int) {
        _selectedTime.value = time
    }
}