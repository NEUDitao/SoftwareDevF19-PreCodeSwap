package com.tsuro.action;

import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Represents an Action during the course of a game.
 */
@AllArgsConstructor
@EqualsAndHashCode
public final class IntermediateAction implements IAction {

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


}
