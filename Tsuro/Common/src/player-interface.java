import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.observer.IObserver;
import com.tsuro.player.PlayerState;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.List;

/**
 * Represents a player performing {@link IAction}s, which involve the placing of {@link ITile}s on a
 * {@link IBoard}.
 */
interface IPlayer {

  /**
   * Makes an {@link com.tsuro.action.InitialAction} on the given {@link IBoard}, allowing the
   * {@link IPlayer} to check their proposed move using the hand on the {@link IRuleChecker}
   * checker, using the given {@link Token}.
   */
  IAction makeInitMove(List<ITile> hand, Token avatar, IBoard board, IRuleChecker checker);

  /**
   * Makes an {@link com.tsuro.action.IntermediateAction} on the given {@link IBoard}, allowing the
   * {@link IPlayer} to check their proposed move using the hand on the {@link IRuleChecker}
   * checker, using the given {@link Token}.
   */
  IAction makeIntermediateMove(List<ITile> hand, Token avatar, IBoard board, IRuleChecker checker);

  /**
   * Adds the given Observer to this Player.
   */
  void addObserver(IObserver<PlayerState> obs);

  /**
   * Removes the given Observer from thsi Player. Does not do anything if observer is not on the
   * Player.
   */
  void removeObserver(IObserver<PlayerState> obs);

}
