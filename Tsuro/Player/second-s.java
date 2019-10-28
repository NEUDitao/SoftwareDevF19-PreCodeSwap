import com.tsuro.action.IntermediateAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.strategy.AbstractStrategy;
import com.tsuro.strategy.IPlayerStrategy;
import com.tsuro.tile.ITile;
import java.util.List;

/**
 * Strategy for a player that follows the behaviour as defined at https://ccs.neu.edu/home/matthias/4500-f19/7.html
 */
class SecondPlayerStrategy extends AbstractStrategy implements IPlayerStrategy {

  @Override
  public IntermediateAction strategizeIntermediateMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker) {

    for (ITile tile : hand) {
      for (int i = 0; i < 4; i++) {
        if (checker.isValidIntermediateMove(board, tile, avatar, hand)) {
          return new IntermediateAction(tile);
        }
        tile = tile.rotate();
      }
    }

    return new IntermediateAction(hand.get(0));
  }
}
