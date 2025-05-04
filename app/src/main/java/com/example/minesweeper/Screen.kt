package com.example.minesweeper

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minesweeper.viewModel.MainViewModel

val backgroundColor = Color(0xFF69BAC9)


@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val difficulty = viewModel.selectedDifficulty.collectAsState().value
    val maxTime = viewModel.selectedTime.collectAsState().value * 60

    var boardSize = 0
    var numMines = 0


    when(difficulty){
        "Fácil" -> {
            boardSize = 6
            numMines = 5
        }
        "Normal" -> {
            boardSize = 7
            numMines = 8
        }
        "Difícil" -> {
            boardSize = 8
            numMines = 12
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_main),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    val intent = Intent(context, BoardActivity::class.java).apply {
                        putExtra("boardSize", boardSize)
                        putExtra("numMines", numMines)
                        putExtra("maxTime", maxTime)
                    }
                    context.startActivity(intent)
                },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1778A4),
            contentColor = Color.White
        )
            ) {
                Text(text = "Jugar")
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun HelpScreen() {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ayuda",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "¿Cómo se juega al Buscaminas?",
                            fontSize = 20.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "El objetivo del juego es encontrar todas las minas ocultas sin hacerlas explotar.",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }

                    Column(
                        modifier = Modifier.weight(2f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = """
                        ✅ Pulsa una casilla para revelar su contenido.
                        🚩 Mantén pulsado para marcarla como posible mina.
                        💥 Si revelas una mina, pierdes.
                        🧠 Usa los números para deducir cuántas minas hay alrededor.

                        ¡Revela todas las casillas seguras para ganar!
                    """.trimIndent(),
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }else{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ayuda",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿Cómo se juega al Buscaminas?",
                    fontSize = 24.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = """
                El objetivo del juego es encontrar todas las minas ocultas sin hacerlas explotar.

                ✅ Pulsa una casilla para revelar su contenido.
                🚩 Mantén pulsado para marcarla como posible mina.
                💥 Si revelas una mina, pierdes.
                🧠 Usa los números para deducir cuántas minas hay alrededor.

                ¡Revela todas las casillas seguras para ganar!
            """.trimIndent(),
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val difficulties = listOf("Fácil", "Normal", "Difícil")
    val selectedDifficulty by viewModel.selectedDifficulty.collectAsState()
    val times = listOf(1, 2, 3)
    val selectedTime by viewModel.selectedTime.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if(isLandscape){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Configuración",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dificultad",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        difficulties.forEach { difficulty ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { viewModel.setDifficulty(difficulty) }
                            ) {
                                RadioButton(
                                    selected = selectedDifficulty == difficulty,
                                    onClick = { viewModel.setDifficulty(difficulty) }
                                )
                                Text(text = difficulty)
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tiempo límite",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        times.forEach { time ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { viewModel.setTime(time) }
                            ) {
                                RadioButton(
                                    selected = selectedTime == time,
                                    onClick = { viewModel.setTime(time) }
                                )
                                Text(text = "$time minuto${if (time > 1) "s" else ""}")
                            }
                        }
                    }
                }
            }
        }
    }else{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Configuración",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Dificultad",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                difficulties.forEach { difficulty ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setDifficulty(difficulty) }
                    ) {
                        RadioButton(
                            selected = selectedDifficulty == difficulty,
                            onClick = { viewModel.setDifficulty(difficulty) }
                        )
                        Text(text = difficulty)
                    }
                }

                Text(
                    text = "Tiempo límite",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 24.dp)
                )

                times.forEach { time ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setTime(time) }
                    ) {
                        RadioButton(
                            selected = selectedTime == time,
                            onClick = { viewModel.setTime(time) }
                        )
                        Text(text = "$time minuto${if (time > 1) "s" else ""}")
                    }
                }
            }
        }
    }
}
