package com.example.minesweeper.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minesweeper.GameDatabase
import com.example.minesweeper.GameRepository
import com.example.minesweeper.GameResult
import com.example.minesweeper.logic.generateBoard
import com.example.minesweeper.model.Board
import com.example.minesweeper.model.Cell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class BoardViewModel(
    private val boardSize: Int,
    private val numMines: Int,
    private val maxTime: Int,
    val alias: String
    ) : ViewModel() {

    var board by mutableStateOf(generateBoard(boardSize, numMines))
        private set

    private var startTime by mutableStateOf(System.currentTimeMillis())
    var elapsedTime by mutableStateOf(0L)
    private var timerJob: Job? = null

    var hasLost by mutableStateOf(false)
        private set

    var lossReason by mutableStateOf("")
        private set

    var hasWin by mutableStateOf(false)
        private set

    data class SelectedCellWithTime(
        val cell: Cell,
        val time: Long
    )

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _selectedCells = mutableStateListOf<SelectedCellWithTime>()
    val selectedCells: List<SelectedCellWithTime> get() = _selectedCells

    fun selectCell(cell: Cell) {
        if (_selectedCells.none { it.cell == cell }) {
            _selectedCells.add(SelectedCellWithTime(cell, elapsedTime))
        }
    }

    init {
        setBoard()
    }

    fun resetLossFlag() {
        hasLost = false
    }

    fun resetWinFlag(){
        hasWin = false
    }

    fun setBoard() {
        board = generateBoard(boardSize, numMines)
        startTimer()
    }

    fun revealCell(cell: Cell) {
        val currentCell = board[cell.y][cell.x]

        if (currentCell.isRevealed){
            viewModelScope.launch {
                _toastMessage.emit("¡La celda (${cell.x + 1} , ${cell.y + 1}) ya está revelada!")
            }
            return
        }

        if(currentCell.isFlagged){
            val updatedCell = currentCell.unToggleFlag()
            board = updateBoardWithNewCell(updatedCell)
            return
        }

        val updatedCell = currentCell.reveal() // Usamos el método `reveal()` para obtener una nueva celda revelada
        board = updateBoardWithNewCell(updatedCell)

        if (updatedCell.hasMine) {
            hasLost = true
            lossReason = "Has pisado una mina en la celda (${cell.x + 1} , ${cell.y + 1})"
            stopTimer()
            return
        }

        if (updatedCell.adjacentMines == 0) {
            revealAdjacentCells(updatedCell)
        }


        if (checkIfGameWon()) {
            stopTimer()
            hasWin = true
        }
    }

    private fun updateBoardWithNewCell(updatedCell: Cell): Board {
        return board.map { row ->
            row.map { cell ->
                if (cell.x == updatedCell.x && cell.y == updatedCell.y) updatedCell else cell
            }
        }
    }

    private fun revealAdjacentCells(cell: Cell) {
        val directions = listOf(
            -1 to -1, -1 to 0, -1 to 1,
            0 to -1,         0 to 1,
            1 to -1,  1 to 0, 1 to 1
        )

        val rowCount = board.size
        val colCount = board.firstOrNull()?.size ?: 0

        for ((dx, dy) in directions) {
            val newX = cell.x + dx
            val newY = cell.y + dy

            if (newY in 0 until rowCount && newX in 0 until colCount) {
                val neighbor = board[newY][newX]
                if (!neighbor.isRevealed && !neighbor.hasMine) {
                    revealCell(neighbor)
                }
            }
        }
    }

    fun toggleFlag(cell: Cell) {
        val currentCell = board[cell.y][cell.x]

        if (currentCell.isRevealed) return
        val updatedCell = currentCell.toggleFlag()
        board = updateBoardWithNewCell(updatedCell)

        if (checkIfGameWon()) {
            stopTimer()
            hasWin = true
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000

                if (elapsedTime >= maxTime) {
                    stopTimer()
                    lossReason = "Límite de $maxTime segundos alcanzado"
                    hasLost = true
                    break
                }
            }
        }
    }


    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun checkIfGameWon(): Boolean {
        var allRevealed = true
        var allMinesFlagged = true

        for (row in board) {
            for (cell in row) {
                if (!cell.hasMine && !cell.isRevealed) {
                    allRevealed = false
                }
                if (cell.hasMine && !cell.isFlagged) {
                    allMinesFlagged = false
                }
            }
        }

        return allRevealed || allMinesFlagged
    }

    fun countRevealedMines(): Int {
        return board.flatten().count { it.hasMine && it.isFlagged }
    }

    fun countUnrevealedMines(): Int {
        return board.flatten().count { it.hasMine && !it.isRevealed && !it.isFlagged }
    }

    fun countMines(): Int {
        return board.flatten().count { it.hasMine }
    }

    fun countUnrevealedCells(): Int {
        return board.flatten().count { !it.isRevealed }
    }

    fun saveGameResult(context: Context) {
        val db = GameDatabase.getDatabase(context)
        val repo = GameRepository(db.gameDao())

         val result = GameResult(
             timestamp = System.currentTimeMillis(),
             duration = elapsedTime,
             hasWon = hasWin,
             revealedMines = if (hasWin) countMines() else countRevealedMines(),
             unrevealedMines = if (hasWin) 0 else countUnrevealedMines(),
             lossReason = if (hasWin) null else lossReason,
             alias = alias
         )

        viewModelScope.launch {
            repo.saveResult(result)
        }
    }


}

