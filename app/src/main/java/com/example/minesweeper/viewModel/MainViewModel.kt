package com.example.minesweeper.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minesweeper.GameDatabase
import com.example.minesweeper.GameResult
import com.example.minesweeper.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    val selectedDifficulty = userPreferences.difficultyFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "Normal")
    val selectedTime = userPreferences.timeLimitFlow.stateIn(viewModelScope, SharingStarted.Eagerly, 2)
    val alias = userPreferences.aliasFlow.stateIn(viewModelScope, SharingStarted.Eagerly, "")


    private val dao = GameDatabase.getDatabase(application).gameDao()

    val savedGames: Flow<List<GameResult>> = dao.getAllGames()

    fun setDifficulty(level: String) {
        viewModelScope.launch {
            userPreferences.saveDifficulty(level)
        }
    }

    fun setTime(time: Int) {
        viewModelScope.launch {
            userPreferences.saveTimeLimit(time)
        }
    }

    fun setAlias(newAlias: String) {
        viewModelScope.launch {
            userPreferences.saveAlias(newAlias)
        }
    }
}