package com.example.minesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val game = intent.getSerializableExtra("game") as? GameResult

        setContent {
            if (game != null) {
                ResultDetailScreen(game){
                    finish()
                }
            } else {
                Text("No se han encontrado datos.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultDetailScreen(game: GameResult, onBack: () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF1778A4)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(game.timestamp))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle de Partida") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1778A4),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF69BAC9)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("📅 Fecha: $formattedDate")
                Spacer(modifier = Modifier.height(8.dp))
                Text("\uD83D\uDC64 Alias: ${game.alias}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("⏱️ Duración: ${game.duration} segundos")
                Spacer(modifier = Modifier.height(8.dp))
                Text("🏁 Resultado: ${if (game.hasWon) "Victoria" else "Derrota"}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("\uD83D\uDEA9 Minas descubiertas: ${game.revealedMines}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("\uD83D\uDCA3 Minas no descubiertas: ${game.unrevealedMines}")
                if (!game.hasWon) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("❌ Motivo de la derrota: ${game.lossReason ?: "N/A"}")
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1778A4),
                    contentColor = Color.White
                )
            ) {
                Text("Volver")
            }
        }
    }
}
