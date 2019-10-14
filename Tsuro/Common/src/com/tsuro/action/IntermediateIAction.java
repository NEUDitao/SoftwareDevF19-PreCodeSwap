package com.tsuro.action;

import com.tsuro.board.IBoard;
import com.tsuro.board.IToken;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.Collection;

/**
 * Represents an Action during the course of a game.
 */
public class IntermediateIAction implements IAction {

  private final ITile tile;
  private final IToken token;


  /**
   * The Tile to be placed during the move with the {@link IToken} representing a player.
   */
  public IntermediateIAction(ITile tile, IToken token) {
    this.tile = tile;
    this.token = token;
  }

  @Override
  public boolean isValid(IRuleChecker checker, IBoard board, Collection<ITile> coll) {
    return checker.isValidIntermediateMove(board, tile, token, coll);
  }

  @Override
  public IBoard doAction(IBoard board) {
    return board.placeTileOnBehalfOfPlayer(tile, token);
  }

  @Override
  public boolean isInitialMove() {
    return false;
  }


}
