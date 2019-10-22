import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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
  private BiMap<Token, IPlayer> tokenPlayerMap;
  //TODO

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

  private IBoard doRound(IBoard board, int numTiles,
      QuintFunc<IPlayer, IRuleChecker, IBoard, Token, List<ITile>, IBoard> createAction) {

    Set<IPlayer> elimThisRound = new HashSet<>();

    for (IPlayer p : players) {
      if (!isEliminated(p)) {

        List<ITile> hand = getNTiles(numTiles);
        Token t = tokenPlayerMap.inverse().get(p);

        IBoard newMove = createAction.apply(p, rules, board, t, hand);

        Set<Token> boardAllTokens = board.getAllTokens();
        Set<IPlayer> elimThisTurn = new HashSet<>(players);
        elimThisTurn.removeIf(a -> boardAllTokens.contains(tokenPlayerMap.inverse().get(a)));
        elimThisTurn.removeIf(elimThisRound::contains);
        elimThisTurn.removeIf(this::isEliminated);

        elimThisRound.addAll(elimThisTurn);

      }
    }

    if (elimThisRound.size() > 0) {
      eliminatedPlayers.add(elimThisRound);
    }

    return board;
  }

  private IBoard makeInitialMoves(IBoard board) {
    return doRound(board, 3, (player, rules, brd, t, hand) -> {
      IAction pAction = player.makeInitMove(hand, t, brd, rules);

      if (!pAction.isInitialMove()) {
        brd = brd.kickPlayer(t);
        return brd;
      }

      Optional<IBoard> newMove = pAction.doActionIfValid(rules, brd, t, hand);

      if (newMove.isPresent()) {
        return newMove.get();
      } else {
        return brd.kickPlayer(t);
      }
    });
  }

  private IBoard intermediateRound(IBoard board) {
    return doRound(board, 2, (player, rules, brd, t, hand) -> {
      IAction pAction = player.makeIntermediateMove(hand, t, brd, rules);

      if (pAction.isInitialMove()) {
        brd = brd.kickPlayer(t);
        return brd;
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
        return brd.kickPlayer(t);
      }
    });
  }

  private List<ITile> getNTiles(int i) {
    return IntStream.range(0, i).mapToObj((a) -> tileStrategy.next()).collect(Collectors.toList());
  }

  private BiMap<Token, IPlayer> getTokenPlayerMap() {
    List<Token> tokens = Arrays.stream(ColorString.values())
        .map(Token::new)
        .collect(Collectors.toList());

    return IntStream.range(0, players.size())
        .collect(HashBiMap::create, (bm, t) -> bm.put(tokens.get(t), players.get(t)),
            BiMap::putAll);
  }

  private boolean isEliminated(IPlayer p) {
    return eliminatedPlayers.stream().anyMatch(a -> a.contains(p));
  }


}
