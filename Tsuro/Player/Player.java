import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.strategy.IPlayerStrategy;
import com.tsuro.tile.ITile;
import java.util.List;

/**
 * Represents a player for a game of Tsuro. Player strategies can differentiate through dependency injection.
 */
class Player implements IPlayer {

  /**
   * The {@link IPlayerStrategy} this {@link Player} will use.
   */
  public Player(IPlayerStrategy s) {

  }

  @Override
  public IAction makeInitMove(List<ITile> hand, Token avatar, IBoard board,
                              IRuleChecker checker) {
    return null;
  }

  @Override
  public IAction makeIntermediateMove(List<ITile> hand, Token avatar, IBoard board,
                                      IRuleChecker checker) {
    return null;
  }
}
