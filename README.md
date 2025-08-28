# iChess Game Library

A pure Java chess game library providing comprehensive chess functionality including game state management, move validation, FEN (Forsyth-Edwards Notation) support, PGN (Portable Game Notation) parsing, and complete chess rules implementation.

## Features

- **Game State Management**: Complete chess game state tracking
- **Move Validation**: Full chess move validation with all piece rules
- **FEN Support**: Forsyth-Edwards Notation parsing and generation
- **PGN Support**: Portable Game Notation parsing and generation
- **Piece Movement**: All chess piece movement rules implemented
- **Game History**: Complete move history tracking
- **Check/Checkmate Detection**: Advanced game state analysis
- **Pure Java**: No external dependencies (except JUnit for testing)

## Usage

### Basic Game Creation and Moves

```java
import com.ichess.game.Game;
import com.ichess.game.Common;

// Create a new game
Game game = new Game();

// Play some moves
game.playMove("e2e4");  // White's first move
game.playMove("e7e5");  // Black's response
game.playMove("g1f3");  // White develops knight
game.playMove("b8c6");  // Black develops knight

// Get current position in FEN
System.out.println(game.getFEN());
// Output: r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 3 2

// Check if it's checkmate
if (game.isCheckmate()) {
    System.out.println("Checkmate!");
}
```

### FEN Operations

```java
import com.ichess.game.Game;

// Create game from FEN position
Game game = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

// Get current FEN
String fen = game.getFEN();

// Validate FEN string
boolean isValid = Game.isValidFEN(fen);
```

### PGN Operations

```java
import com.ichess.game.Game;

// Create game from PGN
String pgn = "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7";
Game game = new Game();
game.loadPGN(pgn);

// Export to PGN
String exportedPGN = game.getPGN();
```

### Move Validation

```java
import com.ichess.game.Game;
import com.ichess.game.Move;

Game game = new Game();

// Check if a move is legal
boolean isLegal = game.isLegalMove("e2e4");

// Get all legal moves for current position
List<Move> legalMoves = game.getLegalMoves();

// Validate specific move
Move move = new Move("e2", "e4");
if (game.isValidMove(move)) {
    game.playMove(move);
}
```

## Installation

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.ichess</groupId>
    <artifactId>ichess-game</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Manual Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/berenfeld/ichess-game.git
   ```

2. Build the library:
   ```bash
   cd ichess-game
   mvn clean install
   ```

3. Add the JAR to your project's classpath.

## API Reference

### Core Classes

- **`Game`**: Main game class for chess game management
- **`Move`**: Represents a chess move
- **`Piece`**: Base class for all chess pieces
- **`Board`**: Represents the chess board
- **`Common`**: Constants and utility methods
- **`Utils`**: Utility functions for chess operations

### Key Methods

#### Game Class
- `playMove(String move)`: Play a move in algebraic notation
- `getFEN()`: Get current position in FEN format
- `loadFEN(String fen)`: Load position from FEN
- `getPGN()`: Get game in PGN format
- `loadPGN(String pgn)`: Load game from PGN
- `isCheckmate()`: Check if current position is checkmate
- `isStalemate()`: Check if current position is stalemate
- `isCheck()`: Check if current position is check
- `getLegalMoves()`: Get all legal moves for current position

## Testing

Run the test suite:

```bash
mvn test
```

The library includes comprehensive unit tests covering:
- Move validation
- FEN parsing and generation
- PGN parsing and generation
- Game state transitions
- Check and checkmate detection
- Special moves (castling, en passant, pawn promotion)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

This library was extracted from the iChess project and represents years of chess engine development and testing.
