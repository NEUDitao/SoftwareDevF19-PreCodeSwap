package com.tsuro.action;

import com.tsuro.board.IBoard;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.Collection;

/**
 * An IAction is one of:
 *   - {@link InitialIAction}
 *   - {@link IntermediateIAction}
 * and represents an action that can be taken on a {@link IBoard}.
 */
public interface IAction {

  /**
   * Determines if a move on the given board using the given deck of Tiles is valid according to the
   * given RuleChecker.
   */
  boolean isValid(IRuleChecker rules, IBoard board, Collection<ITile> tiles);

  /**
   * Performs the action on the given Board.
   */
  IBoard doAction(IBoard board);

  /**
   * Returns true if this Action represents an initial move.
   */
  boolean isInitialMove();
}
