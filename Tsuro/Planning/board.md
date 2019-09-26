# Data Definitions

A Location is one of:
 - NORTHWEST
 - NORTHEAST
 - EASTNORTH
 - EASTSOUTH
 - SOUTHEAST
 - SOUTHWEST
 - WESTSOUTH
 - WESTNORTH

**Interpretation**: The location on a Tile. The first direction is which edge of the Tile the location represents, and the second direction is the half of the edge the location represents.

---

A Path is a class:
  - `new Path(Location, Location)`

**Interpretation:** represents the unordered, bidirectional connection between the two given locations on the same Tile.

---

A Tile is an interface, implemented by one of:
  - `new TsuroTile(java.util.Collection<Path>)`
  - `new EmptyTile()`

**Interpretation:** represents a singular tile on the game board. A TsuroTile contains the paths between the 8 locations. An EmptyTile doesn't contain any paths or locations, and is a placeholder for an empty space on the Board.

**INVARIANT:** As defined by the rules of Tsuro, all Tiles have 8 Locations on them, with 4 Paths going between the Locations. All Locations must be uniquely used in the Paths. Tiles are immutable.

### Methods on Tile
  - `Tile rotate()`
    - Returns a new `Tile` that's been rotated 90 degrees clockwise
  - `Location internalConnection(Location)`
    - Finds the `Location` that's connected to the given `Location`
  - `java.util.Set<Path> getPaths()`
    - Gets all of the `Path`s within a `Tile`

---

A Token is an interface, implemented by:
  - `new ColoredToken(java.awt.Color)`

**Interpretation:** A Token is the avatar representing where a player is on the Board. However, the Token does not know the player that is using it. 

---
A BoardLocation is a class:
  - `new BoardLocation(int, int, Location)`
  
**Interpretation**: Holds x, y for the two ints given, and a Location. This corresponds to the Location on the Tile at the x and y coordinates. Has getters for all fields of Constructor.

---


A Board is an interface, implemented by:
  - `new TsuroBoard(int, int)`
  - `new TsuroBoard(Board)`
  - `new ReadOnlyBoard(Board)`

**Interpretation:** A TsuroBoard is a rectangular board based on either the dimensions given, or the board given. It contains x by y Tiles within, where x and y are the dimensions of the Board's width and height. A TsuroBoard can be acted on. A ReadOnlyBoard can only be queried. A Referee should hold a TsuroBoard, while Players should only be given ReadOnlyBoards.

### Methods on Board

A Board has the following methods for players:
  - `Tile getTileAt(int, int)`
    - Gets the `Tile` at the given x and y coordinate
  - `(int, int, Location) getLocationOf(Token)`
    - Gets the x and y coordinates, as well as the `Location` of a `Token` on the board. If the `Token` does not exist, throw an `Exception`
  - `java.util.Set<Token> getAllTokens()`
    - Gets all of the `Tokens` still on the `Board`
  - `java.util.Dimension getSize()`
    - Gets the size of the `Board`

A Board has the following methods for referees, in addition to the methods for players. All methods will throw an exception if the move is invalid (as that means the referee has forgotten the rules):
  - `void placeTile(Tile,int,int)`
    - Places the given tile at the location given in the ints
  - `void placeToken(Token, int, int, Location)`
    - Places the given `Token` at the `Location` on the `Tile` at the given ints
  - `void kickPlayer(Token)`
    - Kicks a token from the game. The referee is in charge of kicking the player.
