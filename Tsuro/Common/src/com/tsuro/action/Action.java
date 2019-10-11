package com.tsuro.action;

import com.tsuro.board.Board;
import com.tsuro.rulechecker.RuleChecker;
import com.tsuro.tile.Tile;
import java.util.Collection;

/**
 * Represents an action that can be taken on a {@link Board}.
 */
public interface Action {

  /**
   * Determines if a move on the given board using the given deck of Tiles is valid according to the
   * given RuleChecker.
   */
  boolean isValid(RuleChecker rules, Board board, Collection<Tile> tiles);

  /**
   * Performs the action on the given Board.
   */
  Board doAction(Board board);

  /**
   * Returns true if this Action represents an initial move.
   */
  boolean isInitialMove();
}
