import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.observer.IObserver;
import com.tsuro.player.PlayerState;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.strategy.IPlayerStrategy;
import com.tsuro.tile.ITile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.NonNull;

/**
 * Represents a player for a game of Tsuro. Player strategies can differentiate through dependency
 * injection.
 */
class Player implements IPlayer {

  @NonNull
  private final IPlayerStrategy strat;
  @NonNull
  private final List<IObserver<PlayerState>> observers = new ArrayList<>();

  /**
   * The {@link IPlayerStrategy} this {@link Player} will use.
   */
  public Player(IPlayerStrategy s) {
    this.strat = s;
  }

  @Override
  public IAction makeInitMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker) {
    IAction moveToBeMade = strat.strategizeInitMove(hand, avatar, board, checker);
    notifyObservers(new PlayerState(moveToBeMade, new LinkedList<>(hand), avatar, board));
    return moveToBeMade;
  }

  @Override
  public IAction makeIntermediateMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker) {

    IAction moveToBeMade = strat.strategizeIntermediateMove(hand, avatar, board, checker);
    notifyObservers(new PlayerState(moveToBeMade, new LinkedList<>(hand), avatar, board));
    return moveToBeMade;
  }

  @Override
  public void addObserver(IObserver<PlayerState> obs) {
    this.observers.add(obs);
  }

  @Override
  public void removeObserver(IObserver<PlayerState> obs) {
    this.observers.remove(obs);
  }

  /**
   * Notifies all observer on this Player with the given State.
   */
  private void notifyObservers(PlayerState state) {
    for (IObserver<PlayerState> obs : this.observers) {
      obs.update(state);
    }
  }


}
