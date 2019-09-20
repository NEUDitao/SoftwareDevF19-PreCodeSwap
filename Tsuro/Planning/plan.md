
In a game of Tsuro:

- A TsuroGameManager is the first entrypoint into the game.
    - An implementation should take a list of players, and start the game
    - it keeps track of the board and the current player

- The TsuroBoard is the board with all of the players on it:
    - It knows how to place Tiles, query if a position is occupied,
      and get the tile at a specific x y location.

- A Tile is an individual piece on the TsuroBoard:
    - It keeps track of the Ports inside of it.
    - The tile knows how to rotate itself, how to prevent itself from
      rotating in the future, how its Ports are connected internally,
      and who the Ports it has is connected to on other Tiles

- A Port is a connection on a Tile:
    - It knows who is connected to next. This is on the assumption that
      all ports face outwards.
    - For example, if there are two tiles, one at 1x1 and one at 1x2, the
      only ports that are connected to another port are the ones on the east
      of the 1x1 and the west of 1x2. The east north Port on the Tile at 1x1
      would be connected to the corresponding Port within 1x2 that is connected
      to 1x2's west north port.
