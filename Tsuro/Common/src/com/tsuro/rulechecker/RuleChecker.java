package com.tsuro.rulechecker;

import com.tsuro.board.Board;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.Token;
import com.tsuro.tile.Tile;
import java.util.Collection;

/**
 * Defines an interface for RuleCheckers for a game.
 */
public interface RuleChecker {

  /**
   * Determines if an initial move by the player with the given tile at the loc on the {@link Board}
   * is valid with the given player's hand being playerTiles.
   */
  boolean isValidInitialMove(Board board, Tile tile, Token player, BoardLocation loc,
      Collection<Tile> playerTiles);

  /**
   * Determines if an intermediate move by the player with the given tile is valid, with the given
   * player's hand being playerTiles.
   */
  boolean isValidIntermediateMove(Board board, Tile tile, Token player,
      Collection<Tile> playerTiles);

}
