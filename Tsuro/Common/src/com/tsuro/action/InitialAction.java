package com.tsuro.action;

import com.tsuro.board.Board;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.RuleChecker;
import com.tsuro.tile.Tile;
import java.util.Collection;

/**
 * Represents an {@link Action} during the initial phase of the game where players are placing
 * {@link Tile} on a {@link Board} and their {@link Token}s.
 */
public class InitialAction implements Action {

  private final Tile tile;
  private final Token token;
  private final BoardLocation loc;

  /**
   * Creates an {@link InitialAction} with a {@link Tile}, {@link Token}, and {@link BoardLocation}
   */
  public InitialAction(Tile tile, Token token, BoardLocation loc) {
    this.tile = tile;
    this.token = token;
    this.loc = loc;
  }

  @Override
  public boolean isValid(RuleChecker rules, Board board, Collection<Tile> tiles) {
    return rules.isValidInitialMove(board, tile, token, loc, tiles);
  }

  @Override
  public Board doAction(Board board) {
    return board.placeFirstTile(tile, token, loc);
  }

  @Override
  public boolean isInitialMove() {
    return true;
  }

}
