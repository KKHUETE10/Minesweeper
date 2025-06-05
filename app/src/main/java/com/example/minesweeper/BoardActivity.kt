package com.example.minesweeper

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.minesweeper.model.Cell
import com.example.minesweeper.viewModel.BoardViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest


class BoardActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val boardSize = intent.getIntExtra("boardSize", 7)
        val numMines = intent.getIntExtra("numMines", 8)
        val maxTime = intent.getIntExtra("maxTime", 120)
        val alias = intent.getStringExtra("alias") ?: ""

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return BoardViewModel(boardSize, numMines, maxTime, alias) as T
            }
        }
        val boardViewModel: BoardViewModel by viewModels { viewModelFactory }

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            BoardScreen(viewModel = boardViewModel, windowSizeClass)
        }
    }
}

@Composable
fun BoardScreen(viewModel: BoardViewModel, windowSizeClass: WindowSizeClass) {
    val backgroundColor = Color(0xFF69BAC9)

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF1778A4)

    val board = viewModel.board
    val hasLost = viewModel.hasLost
    val hasWin = viewModel.hasWin
    val elapsedTime = viewModel.elapsedTime

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val isTablet = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium && windowSizeClass.heightSizeClass >= WindowHeightSizeClass.Medium

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    LaunchedEffect(true) {
        viewModel.toastMessage.collectLatest { message ->
            Log.d("DEBUG", "MOSTRANDO TOAST: $message")
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
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
                                viewModel.saveGameResult(context)
                                val intent = Intent(context, ResultActivity::class.java).apply {
                                    putExtra("elapsedTime", viewModel.elapsedTime)
                                    putExtra("hasWin", viewModel.hasWin)
                                    putExtra("lossReason", viewModel.lossReason)
                                    putExtra("revealedMines", viewModel.countRevealedMines())
                                    putExtra("unrevealedMines", viewModel.countUnrevealedMines())
                                    putExtra("alias", viewModel.alias)
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
                                viewModel.saveGameResult(context)
                                val intent = Intent(context, ResultActivity::class.java).apply {
                                    putExtra("elapsedTime", viewModel.elapsedTime)
                                    putExtra("hasWin", viewModel.hasWin)
                                    putExtra("lossReason", viewModel.lossReason)
                                    putExtra("revealedMines", viewModel.countRevealedMines())
                                    putExtra("unrevealedMines", viewModel.countUnrevealedMines())
                                    putExtra("alias", viewModel.alias)
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

            if (isTablet) {
                if(isLandscape){
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Panel izquierdo
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Tiempo: $elapsedTime segundos")
                            Spacer(modifier = Modifier.height(10.dp))
                            board.forEach { row ->
                                Row {
                                    row.forEach { cell ->
                                        CellBox(
                                            cell = cell,
                                            onReveal = { viewModel.revealCell(cell) },
                                            onToggleFlag = { viewModel.toggleFlag(cell) },
                                            onSelectCell = { viewModel.selectCell(cell) }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val cellCount = board.firstOrNull()?.size ?: 1
                                val boxWidth = 40.dp

                                Box(modifier = Modifier.width(boxWidth * (cellCount / 2))) {
                                    Text(
                                        text = "Minas: ${viewModel.countMines()}",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Box(modifier = Modifier.width(boxWidth * (cellCount - cellCount / 2))) {
                                    Text(
                                        text = "Sin revelar: ${viewModel.countUnrevealedCells()}",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }

                        // Panel derecho
                        val scrollState = rememberScrollState()

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            viewModel.selectedCells.reversed().forEach {
                                Text(
                                    text = "Casilla seleccionada: (${it.cell.x}, ${it.cell.y})",
                                    fontSize = 24.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Tiempo: ${it.time} segundos",
                                    fontSize = 20.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }else{
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Panel superior
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Tiempo: $elapsedTime segundos")
                            Spacer(modifier = Modifier.height(10.dp))
                            board.forEach { row ->
                                Row {
                                    row.forEach { cell ->
                                        CellBox(
                                            cell = cell,
                                            onReveal = { viewModel.revealCell(cell) },
                                            onToggleFlag = { viewModel.toggleFlag(cell) },
                                            onSelectCell = { viewModel.selectCell(cell) }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val cellCount = board.firstOrNull()?.size ?: 1
                                val boxWidth = 40.dp

                                Box(modifier = Modifier.width(boxWidth * (cellCount / 2))) {
                                    Text(
                                        text = "Minas: ${viewModel.countMines()}",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Box(modifier = Modifier.width(boxWidth * (cellCount - cellCount / 2))) {
                                    Text(
                                        text = "Sin revelar: ${viewModel.countUnrevealedCells()}",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }

                        // Panel inferior
                        val scrollState = rememberScrollState()

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            viewModel.selectedCells.reversed().forEach {
                                Text(
                                    text = "Casilla seleccionada: (${it.cell.x}, ${it.cell.y})",
                                    fontSize = 24.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Tiempo: ${it.time} segundos",
                                    fontSize = 20.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

            } else {
                Text(text = "Tiempo: $elapsedTime segundos")
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    board.forEach { row ->
                        Row {
                            row.forEach { cell ->
                                CellBox(
                                    cell = cell,
                                    onReveal = { viewModel.revealCell(cell) },
                                    onToggleFlag = { viewModel.toggleFlag(cell) },
                                    onSelectCell = { viewModel.selectCell(cell) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val cellCount = board.firstOrNull()?.size ?: 1
                        val boxWidth = 40.dp

                        Box(modifier = Modifier.width(boxWidth * (cellCount / 2))) {
                            Text(
                                text = "Minas: ${viewModel.countMines()}",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Box(modifier = Modifier.width(boxWidth * (cellCount - cellCount / 2))) {
                            Text(
                                text = "Sin revelar: ${viewModel.countUnrevealedCells()}",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CellBox(cell: Cell, onReveal: () -> Unit, onToggleFlag: () -> Unit, onSelectCell: () -> Unit) {
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
                    onTap = {
                        onReveal()
                        onSelectCell()
                    },
                    onLongPress = {
                        onToggleFlag()
                        onSelectCell()
                    }
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