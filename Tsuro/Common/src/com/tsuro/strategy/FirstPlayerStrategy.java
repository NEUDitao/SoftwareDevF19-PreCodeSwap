package com.tsuro.strategy;

import com.tsuro.action.IntermediateAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.List;

/**
 * Strategy for a player that follows the behaviour as defined at https://ccs.neu.edu/home/matthias/4500-f19/6.html
 */
public class FirstPlayerStrategy extends AbstractStrategy implements IPlayerStrategy {


  /**
   * Chooses the first tile given, without rotating.
   */
  public IntermediateAction strategizeIntermediateMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker) {
    return new IntermediateAction(hand.get(0));
  }
}
