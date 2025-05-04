package com.example.minesweeper.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ResultViewModel (): ViewModel(){
    
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    fun setEmail(newEmail: String) {
        _email.value = newEmail
    }
}