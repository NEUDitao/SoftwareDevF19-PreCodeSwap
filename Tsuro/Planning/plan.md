
All players are implementations of an interface. When it is a player's turn to act, it receives information
about the current state of the game, and the tiles available for that player to place.
It can affect the game by calling a given callback function with the information about
what tile to play, and how to play it.
Additionally, it receives updates when other players move, but cannot act on them.

The player knows the current state of the board, what actions are available to play, and if the move it posits
is valid or not. The GameManager knows who each of the players are, and has the canonical order of players and
game board. The GameManager, when calling on a Player to move, will provide a callback function. This callback
function accepts a move, and checks the validity of the move. If the move is valid and legal, the manager will
apply the move to the game board. Otherwise, it returns an error.

Additionally, there is a component of the software dedicated to simulating the Board. This component
allows placing tiles, querying the board for what tiles exist at various locations, and determining how
the pieces should move when a new tile is placed.

In a game of Tsuro:

- A TsuroGameManager is the first entrypoint into the game.
    - An implementation should take a list of players, create a board, and start the game
    - It keeps track of the board and the current player
    - It assigns players valid colors at random and keeps track of which players have which colors.

- The TsuroBoard is the board with all of the players on it:
    - It knows how to place Tiles, query if a position is occupied,
      and get the tile at a specific x y location.
    - An implementation should have a 10x10 array of tiles. In addition to the methods shown on the diagram,
      the board should support placing players on a given location in a tile. When a new tile is placed,
      the board updates the ports on its tiles and moves the players as appropriate.
    - When a Tile is placed a TsuroBoard, its rotation is Locked.

- A Tile is an individual piece on the TsuroBoard:
    - A tile has 8 Locations, TopLeft, TopRight, RightTop, RightBottom, BottomRight,
BottomLeft, LeftBottom, LeftTop. The first direction in this name denotes the side of the tile,
while the second direction refers to the part of the side that the
location refers to.
    - Each tile has four "Paths", which are pairs of two Locations. 
For further constraints about these paths, see the rules of Tsuro.
    - When a tile's rotation is locked, it gets 8 ports, one for each Location. The ports are initially unconnected.
    - If a tile's rotation is not locked, the rotate() method rotates the Paths accordingly.
    - The internalConnection method accepts a Location, and returns the Location that the given Location
shares a Path with.
    - A tile overrides equals and hashcode to account for the fact that rotated tiles can be equivalent if
      one of the tiles can be transformed with a rotation into the other tile.

- A Port is a connection on a Tile:
    - It knows who is connected to next. This is on the assumption that
      all ports face outwards.
    - For example, let's have a tile at 1x1. We place a tile at 1x2 whose left top port has a path to its left bottom port.
The right top port of the 1x1 tile is connected to the left bottom port of the tile at 1x2.
If the 1x2 tile instead had a path from the left top to the right top port, the right top port of the tile at 1x1
tile woutld be connected to the right top port of the tile at 1x2. Since there is not tile at 1x3, the port at the right
top of the tile at 1x2 is unconnected.
    - getNextPort returns the path that a given port is connected to, or null if it is unconnected.
    - If the port is at the edge of the board, edgeOfBoard for that port should return true.

- A Player does need need to maintain state about the game. When it is the player's turn, it is fed the relevant info
in the form of an instance of GameInfo created by the TsuroGameManager. As stated in Part 1, the TsuroGameManager
will provide a callback function for the player to act on.
   - This callback function will return a MoveResult, which provides information about whether the selected move
     is successful.

- A GameInfo is given to a Player when it is time to move, or to notify a Player when another Player has made a move.
   - The GameInfo allows the player to obtain a read-only copy of the current TsuroBoard.
   - Additionally, the GameInfo lets the Player obtain its token's location, along with the locations of the
other players' tokens.
