package com.tsuro.action;

import com.tsuro.board.BoardLocation;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents an {@link IAction} during the initial phase of the game where players are placing
 * {@link ITile} on a {@link IBoard} and their {@link Token}s.
 */
@AllArgsConstructor
public class InitialAction implements IAction {

  @NonNull
  private final ITile tile;
  @NonNull
  private final BoardLocation loc;


  @Override
  public Optional<IBoard> doActionIfValid(IRuleChecker rules, IBoard board, Token player,
      Collection<ITile> tiles) {
    if (rules.isValidInitialMove(board, tile, player, loc, tiles)) {
      return Optional.of(board.placeFirstTile(tile, player, loc));
    }
    return Optional.empty();
  }

  @Override
  public boolean isInitialMove() {
    return true;
  }

}
