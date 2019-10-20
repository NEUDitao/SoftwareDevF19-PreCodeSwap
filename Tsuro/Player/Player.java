import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.strategy.IStrategy;
import com.tsuro.tile.ITile;
import java.util.List;

class Player implements IPlayer {

  public Player(IStrategy s) {

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
