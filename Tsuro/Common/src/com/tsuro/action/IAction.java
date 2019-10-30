package com.tsuro.action;

import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;

/**
 * Represents an action that can be taken on a {@link IBoard}.
 */
public interface IAction {

  /**
   * Determines if a move on the given board using the given deck of Tiles is valid according to the
   * given RuleChecker. If the move is valid, do the action and return the new {@link IBoard}.
   */
  Optional<IBoard> doActionIfValid(@NonNull IRuleChecker rules, @NonNull IBoard board,
      @NonNull Token player, @NonNull Collection<ITile> tiles);

}
