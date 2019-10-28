package com.tsuro.strategy;

import com.tsuro.action.InitialAction;
import com.tsuro.action.IntermediateAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.List;
import lombok.NonNull;

/**
 * Represents a strategy that a Player will use on any turn during a game of Tsuro.
 */
public interface IPlayerStrategy {

  /**
   * Creates an  {@link com.tsuro.action.InitialAction} based on this Strategy with the given hand,
   * playing as the {@link Token} on the {@link IBoard} with the {@link IRuleChecker}
   */
  InitialAction strategizeInitMove(@NonNull List<ITile> hand, @NonNull Token avatar,
      @NonNull IBoard board, @NonNull IRuleChecker checker);

  /**
   * Creates an  {@link com.tsuro.action.IntermediateAction} based on this Strategy with the given
   * hand, playing as the {@link Token} on the {@link IBoard} with the {@link IRuleChecker}
   */
  IntermediateAction strategizeIntermediateMove(@NonNull List<ITile> hand, @NonNull Token avatar,
      @NonNull IBoard board,
      @NonNull IRuleChecker checker);


}