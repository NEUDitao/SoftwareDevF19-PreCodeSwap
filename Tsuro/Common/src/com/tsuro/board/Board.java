package com.tsuro.board;


import com.tsuro.tile.Location;
import com.tsuro.tile.Tile;

public interface Board extends ReadOnlyBoard {

  /**
   * Places the initial tiles of the game down for players. Since it's the initial tile, the player
   * can choose where to put their tile, as long as it's touching the edge, and not adjacent to
   * another player's. The player's token must not be on a location that would result in it being
   * off the board.
   *
   * @param tile  the tile to be placed
   * @param token the player's token
   * @param loc   the coordinates of the tile, and the player's {@link Location} on it
   * @throws IllegalArgumentException for placements that result in boards where tiles neighbor
   *                                  other tiles, tiles not placed on the periphery, and tiles
   *                                  where the avatar does not occupy a port that faces the board’s
   *                                  interior.
   */
  void placeFirstTile(Tile tile, Token token, BoardLocation loc);

  /**
   * Places non-initial tiles of the game down for players. After the initial round, tiles can only
   * be placed where players are facing.
   *
   * @param tile  the tile to be placed
   * @param token the player's token
   * @throws IllegalArgumentException if the placement cannot have resulted from a series of turns.
   */
  void placeTileOnBehalfOfPlayer(Tile tile, Token token);

  /**
   * Kicks the player from the game
   *
   * @param token The player to kick
   */
  void kickPlayer(Token token);
}
