package com.example.tictactoe

sealed class Player(val symbol: String) {
    object X : Player("X")
    object O : Player("O")
}

