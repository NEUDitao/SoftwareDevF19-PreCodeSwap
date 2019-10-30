import static com.tsuro.TsuroTestHelper.generateNumPlayers;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tsuro.player.IPlayer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class AdministratorTest {

  @Test
  void runTournamentWithOneGame() {

    List<IPlayer> players = generateNumPlayers(5);

    Administrator a = new Administrator(players);

    assertEquals(new HashSet<>(Collections.singletonList(players.get(0))), a.runTournament());

    players = generateNumPlayers(3);
    a = new Administrator(players);

    assertEquals(new HashSet<>(Collections.singleton(players.get(0))), a.runTournament());

    players = generateNumPlayers(4);
    a = new Administrator(players);

    assertEquals(Collections.singleton(players.get(1)), a.runTournament());


  }

  @Test
  void runTournamentWithLessThan3Players() {

    List<IPlayer> players = generateNumPlayers(2);
    Administrator a = new Administrator(players);

    assertEquals(new HashSet<>(players), a.runTournament());

    players = generateNumPlayers(1);
    a = new Administrator(players);

    assertEquals(new HashSet<>(players), a.runTournament());

    players = generateNumPlayers(0);
    a = new Administrator(players);

    assertEquals(new HashSet<>(players), a.runTournament());
  }

  @Test
  void runTournamentWithMultipleGames() {

    List<IPlayer> players = generateNumPlayers(10);

    Administrator a = new Administrator(players);

    assertEquals(new HashSet<>(Arrays.asList(players.get(0), players.get(5))), a.runTournament());

    players = generateNumPlayers(9);
    a = new Administrator(players);

    assertEquals(new HashSet<>(Arrays.asList(players.get(0), players.get(6))), a.runTournament());

    players = generateNumPlayers(8);
    a = new Administrator(players);

    assertEquals(new HashSet<>(Arrays.asList(players.get(0), players.get(5))), a.runTournament());

    players = generateNumPlayers(7);
    a = new Administrator(players);

    assertEquals(new HashSet<>(Arrays.asList(players.get(1), players.get(4))), a.runTournament());

    players = generateNumPlayers(6);
    a = new Administrator(players);

    assertEquals(new HashSet<>(Arrays.asList(players.get(0), players.get(3))), a.runTournament());

    players = generateNumPlayers(1234);

    a = new Administrator(players);
    assertEquals(new HashSet<>(Arrays.asList(players.get(0), players.get(625))), a.runTournament());
  }
}