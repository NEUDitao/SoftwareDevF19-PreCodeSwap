import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.List;

interface IPlayer {

  IAction makeInitMove(List<ITile> hand, Token avatar, IBoard board, IRuleChecker checker);

  IAction makeIntermediateMove(List<ITile> hand, Token avatar, IBoard board, IRuleChecker checker);

}
