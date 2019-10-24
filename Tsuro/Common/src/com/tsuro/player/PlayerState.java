package com.tsuro.player;

import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import java.util.Collection;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Represents a future Player's Action, the hand they were dealt, the {@link Token} that represents
 * them, and the {@link IBoard} they're acting on.
 */
@EqualsAndHashCode
@ToString
public class PlayerState {

  @NonNull
  public final IAction action;
  @NonNull
  public final Collection<ITile> hand;
  @NonNull
  public final Token token;
  @NonNull
  public final IBoard board;

  /**
   * Constructor for PlayerState that instantiates all parameters. An unmodifiable version of hand
   * is created for internal use.
   */
  public PlayerState(@NonNull IAction action, @NonNull Collection<ITile> hand, @NonNull Token token,
      @NonNull IBoard board) {
    this.action = action;
    this.hand = Collections.unmodifiableCollection(hand);
    this.board = board;
    this.token = token;
  }
}
