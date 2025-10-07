package org.jetbrains.kotlinx.tictactoe

/** Player type used by the CLI. */
enum class PlayerType { HUMAN, COMPUTER }

/** Ask for a player type (h/c), default is human. */
private fun readPlayerType(player: String): PlayerType {
    while (true) {
        print("Select type for $player ([h]uman/[c]omputer, default: human): ")
        val input = readlnOrNull()?.trim()?.lowercase()
        when (input) {
            "h", "human", "" -> return PlayerType.HUMAN
            "c", "computer" -> return PlayerType.COMPUTER
            else -> println("Invalid input. Please enter 'h' for human or 'c' for computer.")
        }
    }
}

/** Read a non-empty line or return [default] if provided. */
private fun readNonEmptyLine(prompt: String, default: String? = null): String {
    while (true) {
        print(prompt)
        val s = readlnOrNull()?.trim()
        if (!s.isNullOrEmpty()) return s
        if (default != null) return default
        println("Please enter a non-empty value.")
    }
}

/** Parse "row col" (0..2). Returns (row, col) or null if invalid. */
private fun parseMove(input: String?): Pair<Int, Int>? {
    if (input == null) return null
    val tokens = input.trim().split(Regex("""[,\s]+""")).filter { it.isNotEmpty() }
    if (tokens.size != 2) return null
    val r = tokens[0].toIntOrNull() ?: return null
    val c = tokens[1].toIntOrNull() ?: return null
    if (r !in 0..2 || c !in 0..2) return null
    return r to c
}

/** Render the board as a 3x3 grid with indices. */
private fun render(game: TicTacToe): String {
    fun cellChar(r: Int, c: Int): Char = when (game.board[r][c]) {
        Mark.X -> 'X'
        Mark.O -> 'O'
        null -> '.'
    }
    return buildString {
        appendLine("  0 1 2")
        for (r in 0..2) {
            append(r).append(' ')
            for (c in 0..2) {
                append(cellChar(r, c))
                if (c < 2) append(' ')
            }
            if (r < 2) appendLine()
        }
    }
}

/** CLI entry point. Set up players, play turns, print result. */
fun main() {
    println("Tic-Tac-Toe (CLI) 3x3 board, rows/cols in 0..2. Type q to quit.")
    val player1 = readNonEmptyLine("Enter name for Player 1 [default: Player 1]: ", default = "Player 1")
    val player1Type = readPlayerType(player1)
    val player2 = readNonEmptyLine("Enter name for Player 2 [default: Player 2]: ", default = "Player 2")
    val player2Type = readPlayerType(player2)

    val names: Map<Mark, String> = mapOf(Mark.X to player1, Mark.O to player2)
    val types: Map<Mark, PlayerType> = mapOf(Mark.X to player1Type, Mark.O to player2Type)

    val game = newGame()
    println("Initial board:")
    println(render(game))

    while (game.state is GameState.InProgress) {
        val current = game.current
        val playerName = names[current] ?: current.name
        val playerType = types[current] ?: PlayerType.HUMAN
        if (playerType == PlayerType.HUMAN) {
            print("$playerName ($current), enter your move as 'row col': ")
            val line = readlnOrNull()?.trim()
            if (line.equals("q", ignoreCase = true)) {
                println("Goodbye!")
                return
            }
            val mv = parseMove(line)
            if (mv == null) {
                println("Invalid input. Enter two numbers in 0..2, e.g., '1 2'.")
                continue
            }
            try {
                game.play(mv.first, mv.second)
            } catch (e: IllegalArgumentException) {
                println("${e.message} Try again.")
                continue
            } catch (e: IllegalStateException) {
                println(e.message)
                break
            }
        } else {
            println("$playerName ($current) [computer] is thinking...")
            Thread.sleep(300)
            val mv = AIPlayer.computeBestMove(game)
            if (mv == null) {
                println("No valid moves left.")
                break
            }
            println("$playerName ($current) [computer] plays: ${mv.first} ${mv.second}")
            game.play(mv.first, mv.second)
        }
        println()
        println(render(game))
    }

    when (val s = game.state) {
        is GameState.Won -> {
            val winnerName = names[s.winner] ?: s.winner.name
            println("\nGame over $winnerName (${s.winner}) wins!")
        }
        GameState.Draw -> println("\nGame over it's a draw.")
        GameState.InProgress -> {} // Should not happen
    }
}