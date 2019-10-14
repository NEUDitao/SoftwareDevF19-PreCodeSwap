package com.tsuro.board;

import com.tsuro.tile.Location;
import com.tsuro.tile.ITile;
import java.awt.Dimension;
import java.util.List;
import java.util.Set;

/**
 * An IBoard is one of:
 *   - {@link TsuroBoard}
 * and represents an interface for Boards for games. All implementations must be immutable.
 */
public interface IBoard {

  /**
   * Places the initial tiles of the game down for players. Since it's the initial tile, the player
   * can choose where to put their tile, as long as it's touching the edge, and not adjacent to
   * another player's. The player's token must not be on a location that would result in it being
   * off the board.
   * @param tile  the tile to be placed
   * @param token the player's token
   * @param loc   the coordinates of the tile, and the player's {@link Location} on it
   * @throws IllegalArgumentException for placements that violate basic physical constraints, such
   * as placing a {@link ITile} on top of an existing {@link ITile}
   */
  IBoard placeFirstTile(ITile tile, IToken token, BoardLocation loc);

  /**
   * Places non-initial tiles of the game down for players. After the initial round, tiles can only
   * be placed where players are facing.
   * @param tile  the tile to be placed
   * @param token the player's token
   * @throws IllegalArgumentException if the placement of a Tile cannot happen (such as the given
   * token not existing)
   */
  IBoard placeTileOnBehalfOfPlayer(ITile tile, IToken token);

  /**
   * Kicks the player from the game
   * @param token The player to kick
   */
  IBoard kickPlayer(IToken token);

  /**
   * Gets the tile at the given x-y coordinate starting from the top-left of the board.
   * @throws IndexOutOfBoundsException if x and y are off the board.
   */
  ITile getTileAt(int x, int y);

  /**
   * Gets the coordinates and {@link Location} of a player represented by the given token.
   * @throws IllegalArgumentException if the token isn't on the board
   */
  BoardLocation getLocationOf(IToken token);

  /**
   * Gets a set of all of the tokens alive on the board.
   */
  Set<IToken> getAllTokens();

  /**
   * Gets the dimensions of the game board.
   */
  Dimension getSize();

  /**
   * Gets all tokens that are currently stuck in a loop.
   *
   * @return A set of tokens that are stuck in a loop, or an empty set if no tokens are in a loop.
   */
  Set<IToken> getLoopingTokens();

  /**
   * Gets the list of statuses that the last move caused
   */
  List<TsuroStatus> getStatuses();


}

