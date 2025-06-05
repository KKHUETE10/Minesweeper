package com.example.minesweeper

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


val backgroundColor = Color(0xFF69BAC9)


@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val difficulty = viewModel.selectedDifficulty.collectAsState().value
    val maxTime = viewModel.selectedTime.collectAsState().value * 60
    val alias = viewModel.alias.collectAsState().value

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
                        putExtra("alias", alias)
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
    val aliasFromVM by viewModel.alias.collectAsState(initial = "")
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var aliasText by remember { mutableStateOf("") }

    LaunchedEffect(aliasFromVM) {
        if (aliasFromVM != aliasText) {
            aliasText = aliasFromVM
        }
    }

    if (isLandscape) {
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

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    // Columna Alias
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Alias",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = aliasText,
                            onValueChange = { newValue ->
                                aliasText = newValue
                                viewModel.setAlias(newValue)
                            },
                            label = { Text("Alias") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1778A4),
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color(0xFF1778A4),
                                unfocusedLabelColor = Color.Gray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    // Columna Dificultad
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
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

                    // Columna Tiempo límite
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
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
    } else {
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
                    text = "Alias",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = aliasText,
                    onValueChange = { newValue ->
                        aliasText = newValue
                        viewModel.setAlias(newValue)
                    },
                    label = { Text("Alias") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1778A4),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFF1778A4),
                        unfocusedLabelColor = Color.Gray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: MainViewModel, windowSizeClass: WindowSizeClass) {
    val gameList by viewModel.savedGames.collectAsState(initial = emptyList())
    val context = LocalContext.current
    val selectedGame = rememberSaveable { mutableStateOf<GameResult?>(null) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val isTablet = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium && windowSizeClass.heightSizeClass >= WindowHeightSizeClass.Medium

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Historial de partidas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1778A4),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        if (gameList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = padding.calculateTopPadding(),
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 0.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay partidas guardadas.")
            }
        } else {
            if (isTablet) {
                if(isLandscape){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            items(gameList.size) { index ->
                                val game = gameList[index]
                                GameItem(game = game) {
                                    selectedGame.value = game
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            selectedGame.value?.let { game ->
                                GameDetails(game)
                            } ?: Text("Selecciona una partida para ver detalles")
                        }
                    }
                }else{
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            items(gameList.size) { index ->
                                val game = gameList[index]
                                GameItem(game = game) {
                                    selectedGame.value = game
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            selectedGame.value?.let { game ->
                                GameDetails(game)
                            } ?: Text("Selecciona una partida para ver detalles")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 0.dp
                        )
                ) {
                    items(gameList.size) { index ->
                        val game = gameList[index]
                        GameItem(game = game) {
                            val intent = Intent(context, ResultDetailActivity::class.java).apply {
                                putExtra("game", game)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameDetails(game: GameResult) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(game.timestamp))

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
}

@Composable
fun GameItem(game: GameResult, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(game.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {onClick()},
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDAEFF7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Fecha: $formattedDate", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Resultado: ${if (game.hasWon) "Ganada" else "Perdida"}")
        }
    }
}