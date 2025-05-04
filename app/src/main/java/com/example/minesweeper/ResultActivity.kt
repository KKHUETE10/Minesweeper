package com.example.minesweeper

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minesweeper.viewModel.ResultViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class ResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val elapsedTime = intent.getLongExtra("elapsedTime", 0)
        val hasWin = intent.getBooleanExtra("hasWin", false)
        val lossReason = intent.getStringExtra("lossReason") ?: ""
        val revealedMines = intent.getIntExtra("revealedMines", 0)
        val unrevealedMines = intent.getIntExtra("unrevealedMines", 0)

        val resultViewModel: ResultViewModel by viewModels() // Solo para el email

        setContent {
            ResultScreen(
                elapsedTime,
                hasWin,
                lossReason,
                revealedMines,
                unrevealedMines,
                resultViewModel
            )
        }
    }
}

@Composable
fun ResultScreen(
    elapsedTime: Long,
    hasWin: Boolean,
    lossReason: String,
    revealedMines: Int,
    unrevealedMines: Int,
    viewModel: ResultViewModel
) {
    val backgroundColor = Color(0xFF69BAC9)

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF1778A4)

    val context = LocalContext.current
    val email = viewModel.email.value
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
        if (isLandscape) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Resultado", fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("⏱️ Tiempo: $elapsedTime s", fontSize = 18.sp)
                        if (!hasWin) {
                            Text("❌ Derrota: $lossReason", fontSize = 18.sp)
                            Text("🚩 Minas descubiertas: $revealedMines", fontSize = 18.sp)
                            Text("💣 Sin descubrir: $unrevealedMines", fontSize = 18.sp)
                        } else {
                            Text("✅ ¡Has ganado!", fontSize = 18.sp)
                            Text("🚩 Minas descubiertas: $revealedMines", fontSize = 18.sp)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = email,
                            onValueChange = { viewModel.setEmail(it) },
                            label = { Text("Tu email") },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { sendEmail(context, email, elapsedTime, hasWin, lossReason, revealedMines, unrevealedMines) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1778A4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Enviar resultados")
                        }
                    }
                }

                Button(
                    onClick = { (context as Activity).finish() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1778A4),
                        contentColor = Color.White
                    )
                ) {
                    Text("Volver al inicio")
                }
            }

        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Resultado de la partida", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("⏱️ Tiempo jugado: $elapsedTime segundos", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (!hasWin) {
                    Text("❌ Derrota: $lossReason", fontSize = 18.sp)
                    Text("🚩 Minas descubiertas: $revealedMines", fontSize = 18.sp)
                    Text("💣 Sin descubrir: $unrevealedMines", fontSize = 18.sp)
                } else {
                    Text("✅ ¡Has ganado!", fontSize = 18.sp)
                    Text("🚩 Minas descubiertas: $revealedMines", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = email,
                    onValueChange = { viewModel.setEmail(it) },
                    label = { Text("Tu email") },
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

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    enabled = email.isNotBlank(),
                    onClick = {sendEmail(context, email, elapsedTime, hasWin, lossReason, revealedMines, unrevealedMines)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1778A4),
                        contentColor = Color.White
                    )
                ) {
                    Text("Enviar resultados")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {(context as Activity).finish()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1778A4),
                        contentColor = Color.White
                    )
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}

fun sendEmail(
    context: Context,
    email: String,
    elapsedTime: Long,
    hasWin: Boolean,
    lossReason: String,
    revealedMines: Int,
    unrevealedMines: Int
){
    val subject = "Resultados de la partida de Buscaminas"
    val body = buildString {
        append("Tiempo jugado: $elapsedTime segundos\n")
        if (!hasWin) {
            append("Motivo de la derrota: $lossReason\n")
            append("Minas descubiertas: $revealedMines\n")
            append("Minas sin descubrir: $unrevealedMines")
        } else {
            append("¡Has ganado!\n")
            append("Minas descubiertas: $revealedMines")
        }
    }

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:${email}")
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No hay aplicación de correo instalada", Toast.LENGTH_SHORT).show()
    }
}