package com.example.minesweeper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.minesweeper.model.Cell
import com.example.minesweeper.viewModel.BoardViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class BoardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val boardSize = intent.getIntExtra("boardSize", 7)
        val numMines = intent.getIntExtra("numMines", 8)
        val maxTime = intent.getIntExtra("maxTime", 120)

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return BoardViewModel(boardSize, numMines, maxTime) as T
            }
        }
        val boardViewModel: BoardViewModel by viewModels { viewModelFactory }

        setContent {
            BoardScreen(viewModel = boardViewModel)
        }
    }
}

@Composable
fun BoardScreen(viewModel: BoardViewModel) {
    val backgroundColor = Color(0xFF69BAC9)

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF1778A4)

    val board = viewModel.board
    val hasLost = viewModel.hasLost
    val hasWin = viewModel.hasWin
    val elapsedTime = viewModel.elapsedTime

    val context = LocalContext.current

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hasLost) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("💥 ¡HAS PERDIDO! 💥") },
                    text = { Text(viewModel.lossReason) },
                    confirmButton = {
                        Button(
                            onClick = {
                                val intent = Intent(context, ResultActivity::class.java).apply {
                                    putExtra("elapsedTime", viewModel.elapsedTime)
                                    putExtra("hasWin", viewModel.hasWin)
                                    putExtra("lossReason", viewModel.lossReason)
                                    putExtra("revealedMines", viewModel.countRevealedMines())
                                    putExtra("unrevealedMines", viewModel.countUnrevealedMines())
                                }
                                viewModel.resetLossFlag()
                                context.startActivity(intent)
                                (context as Activity).finish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1778A4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Salir")
                        }
                    }
                )
            }

            if (hasWin) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("\uD83C\uDF89 ¡VICTORIA! \uD83C\uDF89") },
                    text = { Text("Todas las minas fueron desactivadas") },
                    confirmButton = {
                        Button(
                            onClick = {
                                val intent = Intent(context, ResultActivity::class.java).apply {
                                    putExtra("elapsedTime", viewModel.elapsedTime)
                                    putExtra("hasWin", viewModel.hasWin)
                                    putExtra("lossReason", viewModel.lossReason)
                                    putExtra("revealedMines", viewModel.countRevealedMines())
                                    putExtra("unrevealedMines", viewModel.countUnrevealedMines())
                                }
                                viewModel.resetWinFlag()
                                context.startActivity(intent)
                                (context as Activity).finish()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1778A4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Salir")
                        }
                    }
                )
            }

            Text(text = "Tiempo: $elapsedTime segundos")
            Column {
                board.forEach { row ->
                    Row {
                        row.forEach { cell ->
                            CellBox(
                                cell = cell,
                                onReveal = { viewModel.revealCell(cell) },
                                onToggleFlag = { viewModel.toggleFlag(cell) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CellBox(cell: Cell, onReveal: () -> Unit, onToggleFlag: () -> Unit) {
    val size = 40.dp
    Box(
        modifier = Modifier
            .size(size)
            .border(1.dp, Color.Black)
            .background(
                when {
                    cell.isFlagged -> Color.Yellow
                    cell.isRevealed -> Color.LightGray
                    else -> Color.DarkGray
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onReveal() },
                    onLongPress = { onToggleFlag() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when {
                cell.isFlagged -> "🚩"
                !cell.isRevealed -> ""
                cell.hasMine -> "💣"
                cell.adjacentMines > 0 -> "${cell.adjacentMines}"
                else -> ""
            },
            color = when {
                cell.isFlagged || !cell.isRevealed || cell.hasMine -> Color.Unspecified
                else -> getNumberColor(cell.adjacentMines)
            },
            fontSize = 18.sp
        )
    }
}


fun getNumberColor(adjacentMines: Int): Color {
    return when (adjacentMines) {
        1 -> Color(0xFF000080)
        2 -> Color(0xFFC50303)
        3 -> Color(0xFF248D1F)
        4 -> Color(0xFF6C330B)
        5 -> Color(0xFFC9B002)
        6 -> Color(0xFFD77300)
        7 -> Color(0xFF0A0404)
        8 -> Color(0xFF434346)
        else -> Color.Transparent
    }
}