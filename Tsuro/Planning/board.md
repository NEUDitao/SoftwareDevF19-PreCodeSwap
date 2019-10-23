# Data Definitions

A `Location` is one of:
 - NORTHWEST
 - NORTHEAST
 - EASTNORTH
 - EASTSOUTH
 - SOUTHEAST
 - SOUTHWEST
 - WESTSOUTH
 - WESTNORTH

**Interpretation**: The location on a Tile. The first direction is which edge of the Tile the
 location represents, and the second direction is the half of the edge the location represents.

---

A `Path` is a class:
  - `new Path(Location, Location)`

**Interpretation:** represents the unordered, bidirectional connection between the two given
 locations on the same Tile.

---

An `ITile` is an interface, implemented by one of:
  - `new TsuroTile(java.util.Collection<Path>)`
  - `new EmptySquare()`

**Interpretation:** represents a singular tile on the game board. A `TsuroTile` contains the paths
 between the 8 locations. An `EmptySquare` doesn't contain any paths or locations, and is a placeholder 
 for an empty space on the `IBoard`.

**INVARIANT:** As defined by the rules of Tsuro, all Tiles have 8 Locations on them, with 4 Paths
 going between the Locations. All `Location`s must be uniquely used in the `Path`s. Tiles are immutable.

### Methods on ITile
  - `ITile rotate()`
    - Returns a new `ITile` that's been rotated 90 degrees clockwise
  - `Location internalConnection(Location)`
    - Finds the `Location` that's connected to the given `Location`
  - `java.util.Set<Path> getPaths()`
    - Gets all of the `Path`s within a `ITile`
  - `boolean strictEqual(ITile)`
	- Determines if `this` ITile is equal to the other ITile only if they're in the same orientation.
	 This is different from normal equals, which determines two ITiles are equal even if they're 
	 rotated at different angles.

---

A Token is an interface, implemented by:
  - `new ColoredToken(java.awt.Color)`

**Interpretation:** A Token is the avatar representing where a player is on the `IBoard`. However,
 the Token does not know the player that is using it. 

---
A `BoardLocation` is a class:
  - `new BoardLocation(int, int, Location)`
  
**Interpretation**: Holds x, y for the two ints given, and a Location. This corresponds to the 
Location on the Tile at the x and y coordinates. Has getters for all fields of Constructor. 
(0, 0) is the top left of a board. 

---

A `TsuroStatus` is an enum, which is one of:
  - `INIT_TILE_NOT_ON_EDGE_BOARD`,
  - `INIT_TILE_TOUCHING_ANY`,
  - `INIT_TOKEN_SUICIDE`,
  - `CONTAINS_LOOP`,
  - `INTERMEDIATE_TOKEN_SUICIDE`
  
**Interpretation**: Represents a property of the last move that was taken on a board.

--- 


An IBoard is an interface, implemented by:
  - `new TsuroBoard(int, int)`
  - `new TsuroBoard(Board)`
  - `new TsuroBoard()`

**Interpretation:** A `TsuroBoard` is a rectangular board based on either the dimensions given,
 or the board given. It contains x by y Tiles within, where x and y are the dimensions of the 
 Board's width and height. A `TsuroBoard` can be acted on.
 
**INVARIANT:** All Boards are immutable.

### Methods on Board

An IBoard has the following methods:
  - `ITile getTileAt(int, int)`
    - Gets the `ITile` at the given x and y coordinate
  - `BoardLocation getLocationOf(Token)`
    - Gets the x and y coordinates, as well as the `Location` of a `Token` on the board. 
      If the `Token` does not exist, throw an `Exception`
  - `java.util.Set<Token> getAllTokens()`
    - Gets all of the `Tokens` still on the `Board`
  - `java.util.Dimension getSize()`
    - Gets the size of the `Board`
  - `Board placeFirstTile(Tile, Token, BoardLocation)`
	- Places a Tile from an initial Tile placement on the board
  - `IBoard placeTileOnBehalfOfPlayer(Tile, Token)`
	- Places a Tile at the coordinates the Token's BoardLocation is facing
  - `IBoard kickPlayer(Token)`
	- Kicks the given Token
  - `List<TsuroStatus> getStatuses()`
    - Gets all of the statuses on a Board
  - `Set<Token> getLoopingTokens()`
    - Gets all of the Tokens that are in a loop.
	
  
Additionally, there are two static functions that produce Boards:
  - `IBoard fromInitialPlacements(Map<Point, ITile>, Map<Token, BoardLocation>, Dimension)`
	- Creates a board from a bunch of initial Tile and Token placements. Validates the placements are valid.
  - `IBoard fromIntermediatePlacements(Map<Point, ITile<, Map <Token, BoardLocation>`
	- Creates a board from a a bunch of intermediate placements of Tiles and Tokens. 
	
Both will throw exceptions if the boards given aren't valid. 

# IAction

An IAction represents an action to be taken on a Board, and is implemented by:
  - `new InitialAction(Tile, Token, BoardLocation)`
    - and represents an IAction during the initial phase of the game where players are
      placing a on a Board and their Tokens.
  - `new IntermediateAction(Tile, Token)`
    - and represents an IAction during the course of the game, after the initial placements are done
  
  and implements the following methods:
  
  - `Optional<IBoard> doActionIfValid(IRuleChecker rules, IBoard board, Token player, Collection<ITile> tiles)`
    - Performs the action described by the fields given 
  - `Optional<Point> getLocationOnboard()`
    - Gets the location of the move on a board if it was an initial move.
  - `boolean isInitialMove()`
    - Returns whether this is an initial move
