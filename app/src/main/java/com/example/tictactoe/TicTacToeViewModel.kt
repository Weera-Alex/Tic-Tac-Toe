package com.example.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class MyViewModel : ViewModel() {
    private val _board: Array<Array<MutableLiveData<String>>> = Array(3) {
        Array(3) { MutableLiveData(EMPTY_CELL) }
    }

    val board: Array<Array<MutableLiveData<String>>>
        get() = _board

    var currentPlayer by mutableStateOf(X)
    var gameStatus by mutableStateOf("")

    // Update the value at a specific position in the 2D array
    fun updateValue(row: Int, column: Int, newValue: String) {
        _board[row][column].value = newValue
        currentPlayer = if (currentPlayer == X) O else X
        val winner = gameState(_board)
        if (winner != null) {
            gameStatus = winner
        }
    }

    // Reset the game
    fun resetGame() {
        _board.forEach { row ->
            row.forEach { cell ->
                cell.value = EMPTY_CELL
            }
        }
        currentPlayer = X
        gameStatus = ""
    }
}