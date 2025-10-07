package org.jetbrains.kotlinx.tictactoe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AIPlayerTests {
    @Test
    fun `ai picks immediate winning move for X`() {
        val game = newGame()
        // X (0,0), O (1,1), X (0,1), O (2,2); X to move should play (0,2)
        game.play(0, 0)
        game.play(1, 1)
        game.play(0, 1)
        game.play(2, 2)
        assertEquals(Mark.X, game.current)
        val move = AIPlayer.computeBestMove(game)
        assertNotNull(move)
        assertEquals(0 to 2, move)
    }

    @Test
    fun `ai blocks opponents winning move for O`() {
        val game = newGame()
        // X (0,0), O (1,1), X (0,1), O (2,2), X (2,0); O should block at (0,2)
        game.play(0, 0)
        game.play(1, 1)
        game.play(0, 1)
        game.play(2, 2)
        game.play(2, 0)
        assertEquals(Mark.O, game.current)
        val move = AIPlayer.computeBestMove(game)
        assertNotNull(move)
        assertEquals(0 to 2, move)
    }
}