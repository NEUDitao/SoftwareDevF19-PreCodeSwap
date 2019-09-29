package com.tsuro.board;

import com.tsuro.tile.Location;
import com.tsuro.tile.Tile;
import java.awt.Dimension;
import java.util.Set;

public interface ReadOnlyBoard {

  /**
   * Gets the tile at the given x-y coordinate starting from the top-left of the board.
   *
   * @throws IndexOutOfBoundsException if x and y are off the board.
   */
  Tile getTileAt(int x, int y);

  /**
   * Gets the coordinates and {@link Location} of a player represented by the given token.
   *
   * @throws IllegalArgumentException if the token isn't on the board
   */
  BoardLocation getLocationOf(Token token);

  /**
   * Gets a set of all of the tokens alive on the board.
   */
  Set<Token> getAllTokens();

  /**
   * Gets the dimensions of the game board.
   */
  Dimension getSize();
}
