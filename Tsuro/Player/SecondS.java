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
public class SecondS extends AbstractStrategy implements IPlayerStrategy {

  /**
   * Attempts to find a legal placement by searching for a legal option with the first tile of the
   * hand, trying all possible rotations starting from 0 degrees. If none of these possibilities
   * work out, it moves to the second one and repeats the process. If no possible action is legal,
   * it chooses the first tile at 0 degrees.
   */
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
