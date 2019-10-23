import static com.tsuro.TsuroTestHelper.WHITE_TOKEN;
import static com.tsuro.TsuroTestHelper.loopy;
import static com.tsuro.TsuroTestHelper.t1;
import static com.tsuro.TsuroTestHelper.t2;
import static com.tsuro.TsuroTestHelper.tsuroRuleChecker;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tsuro.action.IAction;
import com.tsuro.action.InitialAction;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.IBoard;
import com.tsuro.board.TsuroBoard;
import com.tsuro.tile.Location;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class FirstPlayerStrategyTest {

  FirstPlayerStrategy f = new FirstPlayerStrategy();

  @Test
  void strategizeInitMove() {
    IBoard theBoard = new TsuroBoard();
    IAction firstAction = new InitialAction(t1, new BoardLocation(Location.EASTNORTH, 1, 0));
    assertEquals(firstAction,
        f.strategizeInitMove(Arrays.asList(loopy, t2, t1), WHITE_TOKEN, theBoard,
            tsuroRuleChecker));

    theBoard = firstAction.doActionIfValid(tsuroRuleChecker, theBoard,
        WHITE_TOKEN, Arrays.asList(loopy, t2, t1))
        .get();


  }

  @Test
  void strategizeIntermediateMove() {
  }
}