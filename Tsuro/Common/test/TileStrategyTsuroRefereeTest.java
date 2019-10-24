import static com.tsuro.TsuroTestHelper.loopy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsuro.action.InitialAction;
import com.tsuro.action.IntermediateAction;
import com.tsuro.board.BoardLocation;
import com.tsuro.referee.CycleThroughTiles;
import com.tsuro.rulechecker.TsuroRuleChecker;
import com.tsuro.tile.Location;
import com.tsuro.tile.tiletypes.TileTypes;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TileStrategyTsuroRefereeTest {

  TileTypes tt = TileTypes.createTileTypes();

  StrategyPlayer p1 = new StrategyPlayer(new FirstPlayerStrategy());
  StrategyPlayer p2 = new StrategyPlayer(new FirstPlayerStrategy());
  StrategyPlayer p3 = new StrategyPlayer(new FirstPlayerStrategy());
  StrategyPlayer p4 = new StrategyPlayer(new FirstPlayerStrategy());
  StrategyPlayer p5 = new StrategyPlayer(new FirstPlayerStrategy());

  TsuroRuleChecker rc = new TsuroRuleChecker();

  @Test
  void startGame() {

    List<IPlayer> players = Arrays.asList(p1, p2, p3, p4, p5);

    // PlayerObserver pob = new PlayerObserver();
    // players.forEach(p->p.addObserver(pob));

    TileStrategyTsuroReferee ref = new TileStrategyTsuroReferee(rc, players,
        new CycleThroughTiles(tt.getAllTiles()));
    assertEquals(Collections.singletonList(Collections.singleton(p1)), ref.startGame());
  }

  @Test
  void testTooManyPlayers() {
    assertThrows(IllegalArgumentException.class,
        () -> new TileStrategyTsuroReferee(rc, new LinkedList<>(),
            new CycleThroughTiles(tt.getAllTiles())));
    assertThrows(IllegalArgumentException.class,
        () -> new TileStrategyTsuroReferee(rc, Arrays.asList(p1, p1, p1, p1, p1, p1),
            new CycleThroughTiles(tt.getAllTiles())));
  }

  @Test
  void testInitMoveCheat() {
    IPlayer cheatPlayer1 = mock(IPlayer.class);
    when(cheatPlayer1.makeInitMove(anyList(), any(), any(), any()))
        .thenReturn(new InitialAction(loopy, new BoardLocation(
            Location.SOUTHWEST, 6, 6)));

    IPlayer cheatPlayer2 = mock(IPlayer.class);
    when(cheatPlayer2.makeInitMove(anyList(), any(), any(), any()))
        .thenReturn(new IntermediateAction(loopy));

    TileStrategyTsuroReferee ref = new TileStrategyTsuroReferee(rc,
        Arrays.asList(cheatPlayer1, cheatPlayer2, p1),
        new CycleThroughTiles(tt.getAllTiles()));

    assertEquals(Collections.singletonList(Collections.singleton(p1)), ref.startGame());

  }

  @Test
  void testWrongMoveIntermediate() {
    IPlayer cheatPlayer = spy(p1);
    doReturn(new InitialAction(loopy, new BoardLocation(Location.SOUTHWEST, 6, 6)))
        .when(cheatPlayer).makeIntermediateMove(any(), any(), any(), any());

    TileStrategyTsuroReferee ref = new TileStrategyTsuroReferee(rc,
        Arrays.asList(cheatPlayer, p2, p3),
        new CycleThroughTiles(tt.getAllTiles()));

    assertEquals(Collections.singletonList(Collections.singleton(p2)), ref.startGame());


  }

  //TODO test Loop is only valid move
}