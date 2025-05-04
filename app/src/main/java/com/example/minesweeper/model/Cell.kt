package com.example.minesweeper.model

data class Cell (
    val x: Int,
    val y: Int,
    var hasMine: Boolean = false,
    var isRevealed: Boolean = false,
    var isFlagged: Boolean = false,
    var adjacentMines: Int = 0
) {
    fun reveal(): Cell {
        return this.copy(isRevealed = true) // Copiamos la celda con el nuevo estado revelado
    }
    fun toggleFlag(): Cell = if (!isRevealed) copy(isFlagged = !isFlagged) else this
}

typealias Board = List<List<Cell>>