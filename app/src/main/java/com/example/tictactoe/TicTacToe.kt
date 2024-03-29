package com.example.tictactoe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import kotlin.math.min

private const val DRAW = "Draw"
const val X = "X"
const val O = "o"
const val EMPTY_CELL = "_"

@Composable
fun Cell(size: Dp, color: Color, onClick: () -> Unit, board: Array<Array<MutableLiveData<String>>>, x: Int, y: Int ) {
    Box(
        modifier = Modifier
            .size(size)
            .clickable(onClick = {
                onClick()
            })
            .shadow(
                elevation = 4.dp,
                shape = RectangleShape,
                clip = true
            )
    ) {
        Surface(
            modifier = Modifier.size(size),
            shape = RectangleShape,
            color = color
        ) {
            if (board[x][y].value == X) {
                AnimatedX()
            } else if (board[x][y].value == O) {
                AnimatedO()
            }
        }
    }
}

@Composable
fun GameBoard(navController: NavHostController, viewModel: MyViewModel) {

    val board = viewModel.board
    var isXturn = viewModel.currentPlayer == X
    var gameStatus = viewModel.gameStatus

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (gameStatus != "") {
            val text = if (gameStatus == DRAW) "Draw!" else "$gameStatus wins!"
            Text(
                text = text,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            val currentPlayer = if (isXturn) "Player X" else "Player O"
            Text(
                text = "Turn: $currentPlayer",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        for (x in 0 until 3) {
            Row {
                for (y in 0 until 3) {
                    Cell(
                        size = 100.dp,
                        color = Color.White,
                        onClick = {
                            if (board[x][y].value == EMPTY_CELL && gameStatus == "") {
                                viewModel.updateValue(x, y, if (isXturn) X else O)
                                isXturn = !isXturn
                                val winner = gameState(board)
                                if (winner != null) {
                                    gameStatus = winner
                                }
                            }
                        },
                        board = board,
                        x = x,
                        y = y,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
        Row {
            IconButton(
                onClick = {
                    viewModel.resetGame()
                }
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Clear Button",
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.width(64.dp))
            IconButton(onClick = { navController.navigate(Route.Home.route) }) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home Button",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

fun gameState(board: Array<Array<MutableLiveData<String>>>): String? {
    // Check rows
    for (row in board) {
        if (row.all { it.value == X }) return X
        if (row.all { it.value == O }) return O
    }

    // Check columns
    for (col in board.indices) {
        if (board.all { it[col].value == X }) return X
        if (board.all { it[col].value == O }) return O
    }

    // Check diagonals
    if (board[0][0].value == board[1][1].value && board[1][1].value == board[2][2].value) {
        if (board[0][0].value == X) return X
        if (board[0][0].value == O) return O
    }
    if (board[0][2].value == board[1][1].value && board[1][1].value == board[2][0].value) {
        if (board[0][2].value == X) return X
        if (board[0][2].value == O) return O
    }
    if (board.all { row -> row.all { it.value != EMPTY_CELL } }) {
        return DRAW
    }
    return null // No winner found
}



@Composable
fun AnimatedX() {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val lineLength = min(canvasWidth, canvasHeight) * animatedProgress.value - 100

        val startX = canvasWidth / 2 - lineLength / 2
        val startY = canvasHeight / 2 - lineLength / 2
        val endX = startX + lineLength
        val endY = startY + lineLength

        drawLine(
            color = Color.Black,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 4f
        )

        drawLine(
            color = Color.Black,
            start = Offset(endX, startY),
            end = Offset(startX, endY),
            strokeWidth = 4f
        )
    }
}

@Composable
fun AnimatedO() {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        val radius = min(canvasWidth, canvasHeight) / 4 * animatedProgress.value

        drawCircle(
            color = Color.Black,
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}