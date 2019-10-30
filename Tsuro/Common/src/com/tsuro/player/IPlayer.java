package com.tsuro.player;

import com.tsuro.action.IAction;
import com.tsuro.action.InitialAction;
import com.tsuro.action.IntermediateAction;
import com.tsuro.board.ColorString;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.observer.IObservable;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.List;

/**
 * Represents a player performing {@link IAction}s, which involve the placing of {@link ITile}s on a
 * {@link IBoard}.
 */
public interface IPlayer extends IObservable<PlayerState> {

  /**
   * Makes an {@link com.tsuro.action.InitialAction} on the given {@link IBoard}, allowing the
   * {@link IPlayer} to check their proposed move using the hand on the {@link IRuleChecker}
   * checker, using the given {@link Token}.
   */
  InitialAction makeInitMove(List<ITile> hand, Token avatar, IBoard board, IRuleChecker checker);

  /**
   * Makes an {@link com.tsuro.action.IntermediateAction} on the given {@link IBoard}, allowing the
   * {@link IPlayer} to check their proposed move using the hand on the {@link IRuleChecker}
   * checker, using the given {@link Token}.
   */
  IntermediateAction makeIntermediateMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker);

  /**
   * Tells the Player what color they're playing as. Default behaviour to do nothing.
   */
  default void playingAs(ColorString color) {
  }

  /**
   * Tells the Player what colors their opponents are. Default behaviour to do nothing.
   */
  default void otherPlayerAre(List<ColorString> otherColors) {
  }

  /**
   * Tells the Player if they won or not.
   */
  default void notifyResults(boolean won) {
  }

}
