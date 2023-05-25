package com.example.tictactoe

sealed class Route(val route: String) {
    object Home: Route("Home")
    object Board: Route("Board")
}