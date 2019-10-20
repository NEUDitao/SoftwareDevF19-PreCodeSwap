import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.tsuro.board.ColorString;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroBoard;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
  }

  List<Set<IPlayer>> startGame() {
    IBoard board = new TsuroBoard();

    BiMap<Token, IPlayer> tokenPlayerMap = getTokenPlayerMap();
    BiMap<IPlayer, Token> playerTokenMap = tokenPlayerMap.inverse();

    List<Set<IPlayer>> eliminatedPlayers = new ArrayList<>();


  }

  private IBoard makeInitialMoves(BiMap<Token, IPlayer> tokenPlayerMap,
      List<Set<IPlayer>> eliminatedPlayers, IBoard board) {

  }

  private BiMap<Token, IPlayer> getTokenPlayerMap() {
    List<Token> tokens = Arrays.stream(ColorString.values())
        .map(Token::new)
        .collect(Collectors.toList());

    return IntStream.range(0, players.size())
        .collect(HashBiMap::create, (bm, t) -> bm.put(tokens.get(t), players.get(t)),
            BiMap::putAll);
  }

}
