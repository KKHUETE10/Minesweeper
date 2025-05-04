package com.example.minesweeper.logic

import com.example.minesweeper.model.Cell
import com.example.minesweeper.model.Board

fun generateBoard(size: Int, numMines: Int): Board {

    // Crear la lista base de celdas
    val cells = List(size) { y ->
        List(size) { x ->
            Cell(x = x, y = y)
        }
    }

    // Colocar minas en posiciones aleatorias únicas
    val allPositions = (0 until size * size).shuffled().take(numMines)
    for (pos in allPositions) {
        val x = pos % size
        val y = pos / size
        cells[y][x].hasMine = true
    }

    // Calcular minas adyacentes para cada celda
    for (y in 0 until size) {
        for (x in 0 until size) {
            val cell = cells[y][x]
            if (!cell.hasMine) {
                val neighbors = getNeighbors(cells, x, y)
                cell.adjacentMines = neighbors.count { it.hasMine }
            }
        }
    }

    return cells
}

private fun getNeighbors(board: Board, x: Int, y: Int): List<Cell> {
    val neighbors = mutableListOf<Cell>()
    val size = board.size

    for (dy in -1..1) {
        for (dx in -1..1) {
            if (dx == 0 && dy == 0) continue // Saltar la celda actual

            val nx = x + dx
            val ny = y + dy

            if (nx in 0 until size && ny in 0 until size) {
                neighbors.add(board[ny][nx])
            }
        }
    }

    return neighbors
}
