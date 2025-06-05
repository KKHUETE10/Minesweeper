package com.example.minesweeper

class GameRepository(private val dao: GameDao) {
    suspend fun saveResult(result: GameResult) {
        dao.insertGame(result)
    }
}