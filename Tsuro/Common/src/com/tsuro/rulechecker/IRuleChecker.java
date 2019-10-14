package com.tsuro.rulechecker;

import com.tsuro.board.IBoard;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.IToken;
import com.tsuro.tile.ITile;
import java.util.Collection;

/**
 * Defines an interface for RuleCheckers for a game.
 */
public interface IRuleChecker {

  /**
   * Determines if an initial move by the player with the given tile at the loc on the {@link IBoard}
   * is valid with the given player's hand being playerTiles.
   */
  boolean isValidInitialMove(IBoard board, ITile tile, IToken player, BoardLocation loc,
                             Collection<ITile> playerTiles);

  /**
   * Determines if an intermediate move by the player with the given tile is valid, with the given
   * player's hand being playerTiles.
   */
  boolean isValidIntermediateMove(IBoard board, ITile tile, IToken player,
                                  Collection<ITile> playerTiles);

}
