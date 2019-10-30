import com.google.common.collect.Lists;
import com.tsuro.player.IPlayer;
import com.tsuro.referee.CycleThroughTiles;
import com.tsuro.referee.Referee;
import com.tsuro.rulechecker.TsuroRuleChecker;
import com.tsuro.tile.tiletypes.TileTypes;
import com.tsuro.utils.TimeoutUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * Represents a tournament manager/administrator for a game of Tsuro. Original specification can be
 * found https://ccs.neu.edu/home/matthias/4500-f19/7.html
 * <p>
 * Admins should only be used once.
 */
public class Administrator {

  private static final TileTypes allTiles = TileTypes.createTileTypes();
  private final int MAX_PLAYERS_IN_GAME = 5;
  private final int MIN_PLAYERS_IN_GAME = 3;
  private final List<IPlayer> players;
  private final List<IPlayer> currentPlayers;
  private final Set<IPlayer> cheaters = new HashSet<>();

  /**
   * Constructor for an Administrator that plays a game with the given players.
   *
   * @param players players to play the game in order of descending age.
   */
  public Administrator(@NonNull List<IPlayer> players) {
    this.players = new ArrayList<>(players);
    this.currentPlayers = new ArrayList<>(players);
  }

  /**
   * Runs a full tournament of Tsuro.
   *
   * @return A Set of players who won the game.
   */
  public Set<IPlayer> runTournament() {

    while (true) {
      List<List<IPlayer>> games = assignPlayersToGames(new ArrayList<>(currentPlayers));

      if (games.size() == 0) { // if no games, end
        return endTournament();
      }

      playGames(games);
    }
  }

  /**
   * Puts lists in games into tournaments, and removes losers from currentPlayers, adds cheaters to
   * cheaters.
   */
  private void playGames(List<List<IPlayer>> games) {

    List<IPlayer> survivingPlayers = new ArrayList<>();
    for (List<IPlayer> game : games) {

      List<Set<IPlayer>> resultsFromSingleGame = new Referee(new TsuroRuleChecker(), game,
          new CycleThroughTiles(allTiles.getAllTiles())).startGame();

      // gets cheaters
      game.stream()
          .filter(cheater -> resultsFromSingleGame.stream()
              .noneMatch(ordinal -> ordinal.contains(cheater)))
          .forEach(cheaters::add);

      // adds the top 2 sets of players to continue on. 加油！
      resultsFromSingleGame.stream().limit(2).forEach(survivingPlayers::addAll);

    }

    currentPlayers.retainAll(survivingPlayers);
  }

  /**
   * Informs players whether or not they won (excluding cheaters), and declares the winners of the
   * Tsuro tournament.
   */
  private Set<IPlayer> endTournament() {
    players.stream()
        .filter(player -> !cheaters.contains(player)) // get rid of cheaters
        .forEach(winner -> { // notify all players
          try {
            TimeoutUtils
                .doFunctionForTime(() -> winner.notifyResults(currentPlayers.contains(winner)));
          } catch (Exception e) {
            // they're no longer a winner
            currentPlayers.remove(winner);
            cheaters.add(winner);
          }
        });
    return new HashSet<>(currentPlayers);
  }

  /**
   * Assigns players to sublists of games that they will play rounds of Tsuro for.
   */
  private List<List<IPlayer>> assignPlayersToGames(List<IPlayer> players) {

    List<List<IPlayer>> retVal = new ArrayList<>();
    if (players.size() < MIN_PLAYERS_IN_GAME) {
      return retVal;
    }

    retVal = Lists.partition(players, MAX_PLAYERS_IN_GAME).stream().map(ArrayList::new).collect(
        Collectors.toList());
    List<IPlayer> endList = retVal.get(retVal.size() - 1);

    if (endList.size()
        < MIN_PLAYERS_IN_GAME) { // if the last game doesn't have enough players, reassign them.

      List<IPlayer> beforeEndLIst = retVal.get(retVal.size() - 2);
      while (endList.size() < MIN_PLAYERS_IN_GAME) {
        endList.add(0, beforeEndLIst.remove(beforeEndLIst.size() - 1));
      }
    }

    return retVal;

  }

}
