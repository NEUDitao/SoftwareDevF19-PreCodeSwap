package com.tsuro.action;

import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.awt.Point;
import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents an Action during the course of a game.
 */
@AllArgsConstructor
public class IntermediateAction implements IAction {

  @NonNull
  private final ITile tile;

  @Override
  public Optional<IBoard> doActionIfValid(IRuleChecker rules, IBoard board, Token player,
      Collection<ITile> tiles) {
    if (rules.isValidIntermediateMove(board, tile, player, tiles)) {
      return Optional.of(board.placeTileOnBehalfOfPlayer(tile, player));
    }
    return Optional.empty();
  }

  @Override
  public boolean isInitialMove() {
    return false;
  }

  @Override
  public Optional<Point> getLocationOnBoard() {
    return Optional.empty();
  }


}
