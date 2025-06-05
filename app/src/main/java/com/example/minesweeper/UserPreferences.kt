package com.example.minesweeper

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

object PreferencesKeys {
    val DIFFICULTY = stringPreferencesKey("difficulty")
    val TIME_LIMIT = intPreferencesKey("time_limit")
    val ALIAS = stringPreferencesKey("alias")
}

class UserPreferences(private val context: Context) {

    val difficultyFlow: Flow<String> = context.dataStore.data
        .map { it[PreferencesKeys.DIFFICULTY] ?: "Normal" }

    val timeLimitFlow: Flow<Int> = context.dataStore.data
        .map { it[PreferencesKeys.TIME_LIMIT] ?: 2 }

    val aliasFlow: Flow<String> = context.dataStore.data
        .map { it[PreferencesKeys.ALIAS] ?: "" }

    suspend fun saveDifficulty(difficulty: String) {
        context.dataStore.edit { it[PreferencesKeys.DIFFICULTY] = difficulty }
    }

    suspend fun saveTimeLimit(time: Int) {
        context.dataStore.edit { it[PreferencesKeys.TIME_LIMIT] = time }
    }

    suspend fun saveAlias(alias: String) {
        context.dataStore.edit { it[PreferencesKeys.ALIAS] = alias }
    }
}
