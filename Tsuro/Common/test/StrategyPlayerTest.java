import static com.tsuro.TsuroTestHelper.WHITE_TOKEN;
import static com.tsuro.TsuroTestHelper.loopy;
import static com.tsuro.TsuroTestHelper.t1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.observer.IObserver;
import com.tsuro.player.PlayerState;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.strategy.IPlayerStrategy;
import java.util.Arrays;
import org.junit.jupiter.api.Test;


class StrategyPlayerTest {

  @Test
  void makeInitMove() {
    IPlayerStrategy s1 = mock(IPlayerStrategy.class);
    IAction action = mock(IAction.class);
    IBoard board = mock(IBoard.class);
    IRuleChecker checker = mock(IRuleChecker.class);

    IObserver<PlayerState> playerStateIObserver = mock(IObserver.class);

    when(s1.strategizeInitMove(anyList(), eq(WHITE_TOKEN), eq(board), eq(checker)))
        .thenReturn(action);

    StrategyPlayer p1 = new StrategyPlayer(s1);
    p1.addObserver(playerStateIObserver);

    assertEquals(action,
        p1.makeInitMove(Arrays.asList(t1, t1, loopy), WHITE_TOKEN, board, checker));

    verify(s1).strategizeInitMove(Arrays.asList(t1, t1, loopy), WHITE_TOKEN, board, checker);
    verify(playerStateIObserver).update(any(PlayerState.class));

    // Make sure observer isn't called anymore
    p1.removeObserver(playerStateIObserver);
    p1.makeInitMove(Arrays.asList(t1, t1, loopy), WHITE_TOKEN, board, checker);

    verifyNoMoreInteractions(playerStateIObserver);

  }

  @Test
  void makeIntermediateMove() {
    IPlayerStrategy s1 = mock(IPlayerStrategy.class);
    IAction action = mock(IAction.class);
    IBoard board = mock(IBoard.class);
    IRuleChecker checker = mock(IRuleChecker.class);

    IObserver<PlayerState> playerStateIObserver = mock(IObserver.class);

    when(s1.strategizeIntermediateMove(anyList(), eq(WHITE_TOKEN), eq(board), eq(checker)))
        .thenReturn(action);

    StrategyPlayer p1 = new StrategyPlayer(s1);
    p1.addObserver(playerStateIObserver);

    assertEquals(action,
        p1.makeIntermediateMove(Arrays.asList(t1, t1, loopy), WHITE_TOKEN, board, checker));

    verify(s1)
        .strategizeIntermediateMove(Arrays.asList(t1, t1, loopy), WHITE_TOKEN, board, checker);

    verify(playerStateIObserver).update(any(PlayerState.class));

    // Make sure observer isn't called anymore
    p1.removeObserver(playerStateIObserver);
    p1.makeIntermediateMove(Arrays.asList(t1, t1, loopy), WHITE_TOKEN, board, checker);

    verifyNoMoreInteractions(playerStateIObserver);
  }

}