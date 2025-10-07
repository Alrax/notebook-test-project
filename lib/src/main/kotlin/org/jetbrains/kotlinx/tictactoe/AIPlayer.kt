package org.jetbrains.kotlinx.tictactoe

import kotlin.random.Random

// --- Public API ---
/** AI for TicTacToe: Minimax (computeBestMove) and random (getRandomMove). */
object AIPlayer {
    /** Best (row, col) for the current player, or null if no moves. */
    fun computeBestMove(game: TicTacToe): Pair<Int, Int>? {
        val board = Array(3) { r -> Array<Mark?>(3) { c -> game.board[r][c] } }
        val current = game.current
        val (_, best) = minimax(board, current, current, depth = 0)
        return best ?: getRandomMove(game)
    }

    /** Random empty cell (row, col), or null if none. */
    fun getRandomMove(game: TicTacToe): Pair<Int, Int>? {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0..2) {
            for (c in 0..2) {
                if (game.board[r][c] == null) emptyCells.add(r to c)
            }
        }
        if (emptyCells.isEmpty()) return null
        return emptyCells[Random.nextInt(emptyCells.size)]
    }

    // --- Minimax search ---
    private fun minimax(
        board: Array<Array<Mark?>>, // mutable copy for simulation
        turn: Mark,                 // turn of current node
        maximizingFor: Mark,        // the player we optimize for (AI player)
        depth: Int
    ): Pair<Int, Pair<Int, Int>?> {
        val winner = winnerOf(board)
        if (winner != null) {
            return when (winner) {
                maximizingFor -> 10 - depth to null // prefer faster wins
                else -> depth - 10 to null          // prefer slower losses
            }
        }
        if (isFull(board)) return 0 to null

        // Generate all legal moves
        val moves = availableMoves(board)

        var bestMove: Pair<Int, Int>? = null
        if (turn == maximizingFor) {
            // Maximize score for Player
            var bestScore = Int.MIN_VALUE
            for ((r, c) in moves) {
                board[r][c] = turn
                val nextTurn = nextPlayer(turn)
                val (score, _) = minimax(board, nextTurn, maximizingFor, depth + 1)
                board[r][c] = null
                if (score > bestScore) {
                    bestScore = score
                    bestMove = r to c
                }
            }
            return bestScore to bestMove
        } else {
            // Minimize opponent's best score
            var bestScore = Int.MAX_VALUE
            for ((r, c) in moves) {
                board[r][c] = turn
                val nextTurn = nextPlayer(turn)
                val (score, _) = minimax(board, nextTurn, maximizingFor, depth + 1)
                board[r][c] = null
                if (score < bestScore) {
                    bestScore = score
                    bestMove = r to c
                }
            }
            return bestScore to bestMove
        }
    }

    // --- Utilities ---
    private fun availableMoves(board: Array<Array<Mark?>>): List<Pair<Int, Int>> {
        val res = ArrayList<Pair<Int, Int>>(9)
        for (r in 0..2) for (c in 0..2) if (board[r][c] == null) res += r to c
        return res
    }

    private fun isFull(board: Array<Array<Mark?>>): Boolean = board.all { row -> row.all { it != null } }

    private fun nextPlayer(mark: Mark): Mark = if (mark == Mark.X) Mark.O else Mark.X

    private fun winnerOf(board: Array<Array<Mark?>>): Mark? {
        // Rows
        for (r in 0..2) {
            val a = board[r][0]
            if (a != null && a == board[r][1] && a == board[r][2]) return a
        }
        // Columns
        for (c in 0..2) {
            val a = board[0][c]
            if (a != null && a == board[1][c] && a == board[2][c]) return a
        }
        // Diagonals
        val m = board[1][1]
        if (m != null) {
            if (m == board[0][0] && m == board[2][2]) return m
            if (m == board[0][2] && m == board[2][0]) return m
        }
        return null
    }
}