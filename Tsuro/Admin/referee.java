import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import com.tsuro.action.IAction;
import com.tsuro.board.ColorString;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroBoard;
import com.tsuro.board.TsuroStatus;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import com.tsuro.utils.QuintFunc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.NonNull;

/**
 * A referee that plays a game of Tsuro and deals with playing rounds of a game and abnormal
 * conditions as described at https://ccs.neu.edu/home/matthias/4500-f19/6.html
 */
class TileStrategyTsuroReferee {

  @NonNull
  private final IRuleChecker rules;
  @NonNull
  private final List<IPlayer> players;
  @NonNull
  private final Iterator<ITile> tileStrategy;
  @NonNull
  private final List<Set<IPlayer>> eliminatedPlayers;
  @NonNull
  private final List<IPlayer> cheaters = new ArrayList<>();

  @NonNull
  private BiMap<Token, IPlayer> tokenPlayerMap;

  /**
   * Creates a Referee that will adhere to the given rules, and plays a game with the given players,
   * using the given {@link Iterator<ITile>} to determine which tiles to hand out next
   */
  public TileStrategyTsuroReferee(@NonNull IRuleChecker rules,
      @NonNull List<IPlayer> players,
      @NonNull Iterator<ITile> tileStrategy) {
    if (players.size() > 5 || players.size() < 3) {
      throw new IllegalArgumentException("Number of players must be between 3 and 5");
    }

    this.rules = rules;
    this.players = new LinkedList<>(players);
    this.tileStrategy = tileStrategy;
    this.eliminatedPlayers = new ArrayList<>();
  }

  /**
   * Starts a game of Tsuro.
   *
   * @return a List<Set<IPlayer>> representing the placements of players. Each {@link Set<IPlayer>}
   * represents an ordinal in placement, and the index in the list is the ordinal achieved. Cheaters
   * will never be returned.
   */
  List<Set<IPlayer>> startGame() {
    IBoard board = new TsuroBoard();

    tokenPlayerMap = getTokenPlayerMap();

    board = makeInitialMoves(board);

    while (board.getAllTokens().size() > 1) {
      board = intermediateRound(board);
    }

    // TODO: less fishy?
    if (board.getAllTokens().size() == 1) {
      eliminatedPlayers
          .add(Collections.singleton(tokenPlayerMap.get(board.getAllTokens().iterator().next())));
    }

    Collections.reverse(eliminatedPlayers);
    return eliminatedPlayers;

  }

  /**
   * Plays a round of Tsuro.
   *
   * @param board    the board for the play to be done on
   * @param numTiles the number of tiles to be handed out this round
   * @param doAction the board created from the action that a player will take
   * @return the {@link IBoard} at the end of the round of Tsuro
   */
  private IBoard doRound(IBoard board, int numTiles,
      QuintFunc<IPlayer, IRuleChecker, IBoard, Token, List<ITile>, IBoard> doAction) {

    Set<IPlayer> elimThisRound = new HashSet<>();

    for (IPlayer p : players) {
      if (!isEliminated(p) && !cheaters.contains(p) && !elimThisRound.contains(p)) {

        List<ITile> hand = getNTiles(numTiles);
        Token t = tokenPlayerMap.inverse().get(p);

        IBoard newMove = doAction.apply(p, rules, board, t, hand);

        Set<Token> tokensPriorToTurn = board.getAllTokens();
        Set<Token> boardAllTokens = newMove.getAllTokens();
        Set<Token> eliminatedThisTurn = Sets.difference(tokensPriorToTurn, boardAllTokens);

        // Count up all players that were eliminated this turn specifically, not in round, not
        // cheaters, not if eliminated before, and not if in play.
        Set<IPlayer> elimPlayersThisTurn = eliminatedThisTurn.stream()
            .map(tokenPlayerMap::get)
            .filter(pl -> !cheaters.contains(pl))
            .collect(Collectors.toSet());

        elimThisRound.addAll(elimPlayersThisTurn);

        board = newMove;

      }
    }

    if (elimThisRound.size() > 0) {
      eliminatedPlayers.add(elimThisRound);
    }

    return board;
  }

  /**
   * Does the first round of Tsuro, where players are placing down their first tiles.
   *
   * @param board the board for the game to be played on
   * @return the board after the round
   */
  private IBoard makeInitialMoves(IBoard board) {
    return doRound(board, 3, (player, rules, brd, t, hand) -> {
      IAction pAction = player.makeInitMove(hand, t, brd, rules);

      if (!pAction.isInitialMove()) {
        cheaters.add(player);
        brd = brd.kickPlayer(t);
        return brd;
      }

      Optional<IBoard> newMove = pAction.doActionIfValid(rules, brd, t, hand);

      if (newMove.isPresent()) {
        return newMove.get();
      } else {
        cheaters.add(player);
        return brd.kickPlayer(t);
      }
    });
  }

  /**
   * Does any intermediate round of Tsuro, where players are working on eliminating each other.
   *
   * @param board The board for the round to be played on.
   * @return the board at the end of the round
   */
  private IBoard intermediateRound(IBoard board) {
    return doRound(board, 2, (player, rules, brd, t, hand) -> {
      IAction pAction = player.makeIntermediateMove(hand, t, brd, rules);

      if (pAction.isInitialMove()) {
        cheaters.add(player);
        return brd.kickPlayer(t);
      }

      Optional<IBoard> newMove = pAction.doActionIfValid(rules, brd, t, hand);

      if (newMove.isPresent()) {
        IBoard newBoard = newMove.get();
        List<TsuroStatus> statii = newBoard.getStatuses();

        if (statii.contains(TsuroStatus.CONTAINS_LOOP)) {
          // This player's only option was to place a tile with a loop,
          // behaviour is to kick player and keep board same as before
          return brd.kickPlayer(t);
        }

        return newBoard;

      } else {
        cheaters.add(player);
        return brd.kickPlayer(t);
      }
    });
  }

  /**
   * Gets the next i tiles for the referee's strategy as a List
   */
  private List<ITile> getNTiles(int i) {
    return IntStream.range(0, i).mapToObj((a) -> tileStrategy.next()).collect(Collectors.toList());
  }

  /**
   * Creates a BiMap of assigned Tokens -> the Players
   */
  private BiMap<Token, IPlayer> getTokenPlayerMap() {
    List<Token> tokens = Arrays.stream(ColorString.values())
        .map(Token::new)
        .collect(Collectors.toList());

    return IntStream.range(0, players.size())
        .collect(HashBiMap::create, (bm, t) -> bm.put(tokens.get(t), players.get(t)),
            BiMap::putAll);
  }

  /**
   * Determines if the given {@link IPlayer} has already been eliminated or not.
   */
  private boolean isEliminated(IPlayer p) {
    return eliminatedPlayers.stream().anyMatch(a -> a.contains(p));
  }


}
