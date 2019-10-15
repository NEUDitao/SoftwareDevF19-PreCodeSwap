package com.tsuro.rulechecker;

import com.tsuro.board.BoardLocation;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import java.util.Collection;
import lombok.NonNull;

/**
 * An IRuleChecker is one of: - {@link TsuroRuleChecker} - {@link HackerIdealRuleChecker} and
 * defines an interface for RuleCheckers for a game. A RuleChecker checks moves if they are valid
 * for a game of Tsuro.
 */
public interface IRuleChecker {

  /**
   * Determines if an initial move by the player with the given tile at the loc on the {@link
   * IBoard} is valid with the given player's hand being playerTiles.
   */
  boolean isValidInitialMove(@NonNull IBoard board, @NonNull ITile tile, @NonNull Token player,
      @NonNull BoardLocation loc,
      @NonNull Collection<ITile> playerTiles);

  /**
   * Determines if an intermediate move by the player with the given tile is valid, with the given
   * player's hand being playerTiles.
   */
  boolean isValidIntermediateMove(@NonNull IBoard board, @NonNull ITile tile,
      @NonNull Token player,
      @NonNull Collection<ITile> playerTiles);

}
