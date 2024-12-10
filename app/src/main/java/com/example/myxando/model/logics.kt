package com.example.myxando.model

const val DIM = 3

typealias Field = CharArray
fun String.toField(): Field = this.toCharArray()

enum class GameState {
    IN_PROGRESS, CROSS_WIN, NOUGHT_WIN, DRAW, ILLEGAL
}

enum class Player {
    CROSS, NOUGHT
}

var data: Field = "_________".toField()

fun ix(row: Int, col: Int) = row * DIM + col

fun printField(field: Field): Unit {
    repeat(DIM) { row ->
        repeat(DIM) { col ->
            print("${field[ix(row, col)]} ")
        }
        println()
    }
}

val Field.gameState: GameState
    get() {
        val lines = arrayOf(
            arrayOf(0, 1, 2),
            arrayOf(3, 4, 5),
            arrayOf(6, 7, 8),
            arrayOf(0, 3, 6),
            arrayOf(1, 4, 7),
            arrayOf(2, 5, 8),
            arrayOf(0, 4, 8),
            arrayOf(2, 4, 6),
        )
        val nX = this.count { it == 'X' }
        val n0 = this.count { it == '0' }
        if ((nX == n0 || nX == n0 + 1).not()) {
            return GameState.ILLEGAL
        }
        for (line in lines) {
            when {
                line.all { this[it] == 'X' } -> return GameState.CROSS_WIN
                line.all { this[it] == '0' } -> return GameState.NOUGHT_WIN
            }
        }
        return if (this.count { it == '_' } == 0) GameState.DRAW else GameState.IN_PROGRESS
    }

fun Field.makeMove(player: Player, row: Int, col: Int): Field {
    val index = ix(row, col)
    if (this[index] != '_') {
        throw IllegalArgumentException("Cell is already occupied")
    }
    this[index] = if (player == Player.CROSS) 'X' else '0'
    return this
}

fun Field.nextPlayer(): Player {
    val nX = this.count { it == 'X' }
    val n0 = this.count { it == '0' }
    return if (nX > n0) Player.NOUGHT else Player.CROSS
}

fun main() {
    var field = "_________".toField()
    var currentPlayer = Player.CROSS

    while (true) {
        printField(field)
        println("Current Player: $currentPlayer")
        println("Enter your move (row and col): ")
        val input = readLine()?.split(" ") ?: break
        val row = input[0].toInt()
        val col = input[1].toInt()

        try {
            field = field.makeMove(currentPlayer, row, col)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            continue
        }

        when (field.gameState) {
            GameState.CROSS_WIN -> {
                printField(field)
                println("CROSS wins!")
                break
            }
            GameState.NOUGHT_WIN -> {
                printField(field)
                println("NOUGHT wins!")
                break
            }
            GameState.DRAW -> {
                printField(field)
                println("It's a draw!")
                break
            }
            GameState.ILLEGAL -> {
                println("Illegal state!")
                break
            }
            GameState.IN_PROGRESS -> {
                currentPlayer = field.nextPlayer()
            }
        }
    }
}
