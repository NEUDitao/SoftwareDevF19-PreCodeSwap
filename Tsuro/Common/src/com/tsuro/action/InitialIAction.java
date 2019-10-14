package com.tsuro.action;

import com.tsuro.board.IBoard;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.IToken;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.Collection;

/**
 * Represents an {@link IAction} during the initial phase of the game where players are placing
 * {@link ITile} on a {@link IBoard} and their {@link IToken}s.
 */
public class InitialIAction implements IAction {

  private final ITile tile;
  private final IToken token;
  private final BoardLocation loc;

  /**
   * Creates an {@link InitialIAction} with a {@link ITile}, {@link IToken}, and {@link BoardLocation}
   */
  public InitialIAction(ITile tile, IToken token, BoardLocation loc) {
    this.tile = tile;
    this.token = token;
    this.loc = loc;
  }

  @Override
  public boolean isValid(IRuleChecker rules, IBoard board, Collection<ITile> tiles) {
    return rules.isValidInitialMove(board, tile, token, loc, tiles);
  }

  @Override
  public IBoard doAction(IBoard board) {
    return board.placeFirstTile(tile, token, loc);
  }

  @Override
  public boolean isInitialMove() {
    return true;
  }

}
