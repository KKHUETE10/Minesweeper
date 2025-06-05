package com.example.minesweeper

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.wire.internal.Serializable

@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val duration: Long,
    val hasWon: Boolean,
    val revealedMines: Int,
    val unrevealedMines: Int,
    val lossReason: String?,
    val alias: String
) : Serializable
