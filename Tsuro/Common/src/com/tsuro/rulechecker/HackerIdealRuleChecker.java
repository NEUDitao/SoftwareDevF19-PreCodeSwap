package com.tsuro.rulechecker;

import com.tsuro.board.BoardLocation;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import java.util.Collection;

/**
 * A {@link IRuleChecker} that allows any and all moves.
 */
public class HackerIdealRuleChecker implements IRuleChecker {

  @Override
  public boolean isValidInitialMove(IBoard board, ITile tile, Token player, BoardLocation loc,
      Collection<ITile> playerTiles) {
    return true;
  }

  @Override
  public boolean isValidIntermediateMove(IBoard board, ITile tile, Token player,
      Collection<ITile> playerTiles) {
    return true;
  }
}
