package org.jetbrains.kotlinx.tictactoe

// --- Public model ---
/** Player marks. */
enum class Mark { X, O }

/** Game state. */
sealed class GameState {
    data object InProgress : GameState()
    data class Won(val winner: Mark) : GameState()
    data object Draw : GameState()
}

// --- Public Interface ---
/**
 * Simple 3x3 Tic-Tac-Toe game engine.
 *
 * - Board: 3x3, rows/cols in 0..2
 * - X goes first
 * - play places the current mark; throws on invalid or finished games
 */
interface TicTacToe {
    val board: List<List<Mark?>>
    val current: Mark
    val state: GameState

    /** Place the current player's mark at [row], [col]. */
    fun play(row: Int, col: Int)
}

/** Create a new game. */
fun newGame(): TicTacToe = TicTacToeGame()

// --- Implementation (private) ---
private class TicTacToeGame : TicTacToe {
    // 3x3 board initialized with nulls
    private val cells: Array<Array<Mark?>> = Array(3) { arrayOfNulls<Mark?>(3) }

    override val board: List<List<Mark?>>
        get() = cells.map { it.toList() }

    private var turn: Mark = Mark.X
    override val current: Mark get() = turn

    private var _state: GameState = GameState.InProgress
    override val state: GameState get() = _state

    override fun play(row: Int, col: Int) {
        // Validate input and state
        require(row in 0..2 && col in 0..2) { "Row and column must be in 0..2" }
        check(_state is GameState.InProgress) { "Game is already over" }
        require(cells[row][col] == null) { "Cell ($row,$col) is already occupied" }

        cells[row][col] = turn

        // Update game state
        val winner = winnerOrNull()
        _state = when {
            winner != null -> GameState.Won(winner)
            isBoardFull() -> GameState.Draw
            else -> GameState.InProgress
        }

        // Switch turn only if game continues
        if (_state is GameState.InProgress) {
            turn = if (turn == Mark.X) Mark.O else Mark.X
        }
    }

    private fun winnerOrNull(): Mark? {
        // Rows
        for (r in 0..2) {
            val a = cells[r][0]
            if (a != null && a == cells[r][1] && a == cells[r][2]) return a
        }
        // Columns
        for (c in 0..2) {
            val a = cells[0][c]
            if (a != null && a == cells[1][c] && a == cells[2][c]) return a
        }
        // Diagonals
        val m = cells[1][1]
        if (m != null) {
            if (m == cells[0][0] && m == cells[2][2]) return m
            if (m == cells[0][2] && m == cells[2][0]) return m
        }
        return null
    }

    private fun isBoardFull(): Boolean = cells.all { row -> row.all { it != null } }
}
