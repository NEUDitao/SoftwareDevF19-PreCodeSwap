package com.tsuro.action;

import com.tsuro.board.Board;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.RuleChecker;
import com.tsuro.tile.Tile;
import java.util.Collection;

/**
 * Represents an Action during the course of a game.
 */
public class IntermediateAction implements Action {

  private final Tile tile;
  private final Token token;


  /**
   * The Tile to be placed during the move with the {@link Token} representing a player.
   */
  public IntermediateAction(Tile tile, Token token) {
    this.tile = tile;
    this.token = token;
  }

  @Override
  public boolean isValid(RuleChecker checker, Board board, Collection<Tile> coll) {
    return checker.isValidIntermediateMove(board, tile, token, coll);
  }

  @Override
  public Board doAction(Board board) {
    return board.placeTileOnBehalfOfPlayer(tile, token);
  }

  @Override
  public boolean isInitialMove() {
    return false;
  }


}
