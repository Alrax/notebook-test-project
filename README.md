# Test Project for Notebook Team Interns

**Task Description:**

Your task is to implement a Tic-Tac-Toe library that is able to drive a
game of Tic-Tac-Toe, but leaves it up to users of the library to create the
UI. 

After you have implemented the library, you must use it to create a simple
command line game of Tic-Tac-Toe.

It should have the following features:

1. When starting the game, it should request the names of Player X and 
   Player O.

2. It should print the initial state of the board when players have been named.

3. Each player should then switch choosing a position on the board, placing
   their mark. 

4. The game should print the updated board after each player has placed a mark.

5. When the game is over, the game should print the result of the game.

**Bonus Features:**

6. Make the player type configurable, i.e., it should be possible to choose
   whether one or both of the players are a human or a computer. The computer
   can just place marks at random.

7. Make the computer opponent a real AI, making intelligent decisions.

8. Create a Kotlin Notebook that demonstrates how to use the library.


If you have any questions, please contact christian.melchior@jetbrains.com.


**Evaluation Criteria:**

1. Does the code work?
2. Is the code readable?
3. Is the code documented?
4. Are there tests and are they passing?
5. How easy is it to extend the code with new player types or display formats?

It is more important to make the code readable, documented and tested
than implementing new features.


**Prepare to discuss the following topics:**

- Why does your implementation work?

- Why did you choose your particular implementation? What alternatives did 
  you consider?

- What are the advantages and disadvantages of your particular implementation?

- Why did you choose the approach to testing you did? What are the advantages 
  and disadvantages of your particular approach?

It is perfectly fine to use Google, Stack Overflow, OpenAI, Cursor or any other
resource to solve the problem, but you should be prepared to explain your
approach to the interviewer.


---

## Implemented Features (Summary)

- `lib/`: pure Tic-Tac-Toe engine (no UI dependencies).
- `app/`: command-line interface (CLI) that uses the library.

The following requirements are implemented:

1) Request player names (X and O)
- At startup, the CLI prompts for Player X and Player O names. Defaults are accepted if left blank.

2) Print initial board
- After naming, the CLI prints the initial 3x3 board with row/column indices. Empty cells are shown as `.`.

3) Alternate moves with input validation
- Players alternate turns automatically (X starts). Human players enter moves as `row col` (0..2). Invalid inputs are rejected with an adapted error message.
- Occupied cells are prevented; the game asks for another move.

4) Print board after each move
- The board is re-rendered and printed after every successful move.

5) Print game result
- When a player wins, the winner’s name and mark are printed. If no moves remain without a winner, the game declares a draw.

Bonus features implemented:

6) Configurable player types (Human/Computer)
- For each player (X and O), you can choose Human or Computer at startup.

7) Real AI opponent (Minimax)
- The library includes `AIPlayer.computeBestMove(game)`, a Minimax-based strategy that picks optimal moves for 3x3 Tic-Tac-Toe (prefers faster wins and slower losses).

8) Kotlin Notebooks demonstrating the library
- `TikTakToeTutorial.ipynb`: step-by-step usage tutorial.
---

## TicTacToe Documentation

#### by Alex Ramallo

### 1. Project Overview
A small Kotlin project that implements a Tic-Tac-Toe game engine and a simple command-line app that uses it. The library knows the rules; the app handles input/output. There are also Kotlin Notebooks that show how to use the library step by step.

### 2. Architecture Overview
- Modules:
  - `lib`: pure game engine. Exposes `TicTacToe` (board/current/state/play), `Mark`, `GameState`, and `newGame()`.
  - `app`: CLI that depends on `lib`. Handles names, human/computer turns, and prints the board.
- Boundaries:
  - The engine has no UI code. The board is read-only from the outside. All rule checks happen inside `play()`.
- AI:
  - `AIPlayer.computeBestMove(game)` uses a simple Minimax search (fast for 3x3).
  - `AIPlayer.getRandomMove(game)` is available for a non-smart computer.

### 3. Implemented Testing
- Engine tests (`lib`):
  - Start state: empty board, X to move.
  - Turn order: X and O alternate correctly.
  - Wins: row, column and diagonal wins are detected.
  - Draws: full board with no winner is reported as draw.
  - Validation: out-of-bounds and occupied cells throw `IllegalArgumentException`.
  - Post-game: playing after the game is over throws `IllegalStateException`.
- AI tests:
  - Picks an immediate winning move.
  - Blocks the opponent’s immediate win.

### 4. Design Choices
- Use strong types: `Mark` (enum) and `GameState` (sealed) make the code safe.
- Expose the board as a read-only structure; only `play()` can change state.
- Provide two computer players: a simple random move and a Minimax-based one. Works for small 3x3 environment.
- Write focused tests for rules and AI behaviors to test core logic and functionality.
- Aim for readability and simplicity. Short functions, direct checks, and small helpers.

### 5. Extensibility Notes

- UI-agnostic core: The engine exposes read-only board state and enforces rules in `play(row, col)`. Any UI (CLI, desktop, web) can render `game.board` as needed.
- Pluggable players: The computer move can use `getRandomMove` or `computeBestMove`. New strategies can be added behind the same interface.
- Clear public interface: `TicTacToe` exposes `board`, `current`, `state`, and `play` with simple error modes (`IllegalArgumentException`, `IllegalStateException`).

### 6. Folders and Files

```
├── README.md                  # This documentation file
├── lib/                       # Tic-Tac-Toe library (engine)
│   ├── src/main/kotlin/
│   │   └── tiktaktoe/         # Main library code
│   │       ├── TicTacToe.kt   # Core game logic
│   │       └── AIPlayer.kt    # AI logic (Minimax and random)
│   └── src/test/kotlin/
│       └── tiktaktoe/         # Unit tests for the library
│           ├── TicTacToeTest.kt # Tests for game rules
│           └── AIPlayerTest.kt  # Tests for AI behavior
├── app/                       # Command-line application
│   └── src/main/kotlin/
│       └── Main.kt        # Main entry point and game loop
│       └── TikTakToeTutorial.ipynb # Step-by-step usage tutorial
```