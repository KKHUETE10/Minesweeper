package com.example.minesweeper

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: GameResult)

    @Query("SELECT * FROM game_results ORDER BY timestamp DESC")
    fun getAllGames(): Flow<List<GameResult>>

}
