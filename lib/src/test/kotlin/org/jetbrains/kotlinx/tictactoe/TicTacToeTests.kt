package org.jetbrains.kotlinx.tictactoe

import kotlin.test.*

class TicTacToeTests {

    @Test
    fun `new game starts with empty board and X to move`() {
        val g = newGame()
        assertEquals(GameState.InProgress, g.state)
        assertEquals(Mark.X, g.current)
        assertTrue(g.board.all { row -> row.all { it == null } })
    }

    @Test
    fun `players alternate turns`() {
        val g = newGame()
        g.play(0, 0) // X
        assertEquals(Mark.O, g.current)
        g.play(1, 1) // O
        assertEquals(Mark.X, g.current)
    }

    @Test
    fun `row win is detected`() {
        val g = newGame()
        g.play(0, 0) // X
        g.play(1, 0) // O
        g.play(0, 1) // X
        g.play(1, 1) // O
        g.play(0, 2) // X wins
        assertEquals(GameState.Won(Mark.X), g.state)
    }

    @Test
    fun `diagonal win is detected`() {
        val g = newGame()
        g.play(0, 0) // X
        g.play(0, 1) // O
        g.play(1, 1) // X
        g.play(0, 2) // O
        g.play(2, 2) // X wins
        assertEquals(GameState.Won(Mark.X), g.state)
    }

    @Test
    fun `draw is detected`() {
        val g = newGame()
        // Sequence that fills the board without a winner
        g.play(0,0) // X
        g.play(0,1) // O
        g.play(0,2) // X
        g.play(1,1) // O
        g.play(1,0) // X
        g.play(1,2) // O
        g.play(2,1) // X
        g.play(2,0) // O
        g.play(2,2) // X -> Draw
        assertEquals(GameState.Draw, g.state)
    }

    @Test
    fun `cannot play out of bounds`() {
        val g = newGame()
        val ex = assertFailsWith<IllegalArgumentException> { g.play(3, 0) }
        assertTrue(ex.message!!.contains("0..2"))
    }

    @Test
    fun `cannot play on occupied cell`() {
        val g = newGame()
        g.play(0, 0)
        assertFailsWith<IllegalArgumentException> { g.play(0, 0) }
    }

    @Test
    fun `cannot play after game over`() {
        val g = newGame()
        g.play(0, 0)
        g.play(1, 0)
        g.play(0, 1)
        g.play(1, 1)
        g.play(0, 2) // X wins
        assertTrue(g.state is GameState.Won)
        assertFailsWith<IllegalStateException> { g.play(2, 2) }
    }
}