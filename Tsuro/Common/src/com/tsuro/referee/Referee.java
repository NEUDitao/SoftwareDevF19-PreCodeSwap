package com.tsuro.referee;

import static com.tsuro.utils.TimeoutUtils.doFunctionForTime;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import com.tsuro.action.IAction;
import com.tsuro.board.ColorString;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroBoard;
import com.tsuro.board.TsuroStatus;
import com.tsuro.player.IPlayer;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import com.tsuro.utils.QuintFunc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 * A referee that plays a game of Tsuro and deals with playing rounds of a game. Abnormal local conditions
 * are handled as described in https://ccs.neu.edu/home/matthias/4500-f19/6.html. (as of 10/24/19)
 * <p>
 * An instance of a com.tsuro.referee.Referee can only be used once (which is a call to startGame()), since Referees
 * are mutable.
 * </p>
 * <p>
 * Referees also deal with any players that throw any sort of Exception by classifying them as a cheater.
 * They also handle Timeouts.
 * </p>
 *
 */
public class Referee {

  // rules to be used during game
  @NonNull
  private final IRuleChecker rules;

  // players of a game
  @NonNull
  private final List<IPlayer> players;

  // how the referee chooses tiles
  @NonNull
  private final Iterator<ITile> tileStrategy;

  // eliminated players grouped by rankings (in reverse ordinals)
  @NonNull
  private final List<Set<IPlayer>> eliminatedPlayers;

  // all cheaters
  @NonNull
  private final List<IPlayer> cheaters = new ArrayList<>();

  // Mapping of a player's token -> them
  @NonNull
  private BiMap<Token, IPlayer> tokenPlayerMap;

  /**
   * Creates a com.tsuro.referee.Referee that will adhere to the given rules, and plays a game with the given players,
   * using the given {@link Iterator<ITile>} to determine which tiles to hand out next
   *
   * @param rules        rulechecker to be used by the referee
   * @param players      players that will play a game (in order of age)
   *                     REQUIREMENT: must be 3-5 players
   * @param tileStrategy how the {@link Referee} will choose the next tile
   */
  public Referee(@NonNull IRuleChecker rules,
      @NonNull List<IPlayer> players,
      @NonNull Iterator<ITile> tileStrategy) {

    checkConstructorContracts(players);

    this.rules = rules;
    this.players = new LinkedList<>(players);
    this.tileStrategy = tileStrategy;
    this.eliminatedPlayers = new ArrayList<>();
  }

  /**
   * Checks the contracts for a referee. If more are required, add here.
   */
  private void checkConstructorContracts(@NonNull List<IPlayer> players) {
    if (players.size() > 5 || players.size() < 3) {
      throw new IllegalArgumentException("Number of players must be between 3 and 5");
    }
  }

  /**
   * Starts a game of Tsuro.
   *
   * @return a List<Set<com.tsuro.player.IPlayer>> representing the placements of players. Each {@link Set<IPlayer>}
   * represents an ordinal in placement, and the index in the list is the ordinal achieved. Cheaters
   * will never be returned.
   */
  public List<Set<IPlayer>> startGame() {

    IBoard board = new TsuroBoard();

    tokenPlayerMap = getTokenPlayerMap();

    notifyPlayersOfColors();

    board = makeInitialMoves(board);

    while (board.getAllTokens().size() > 1) {
      board = intermediateRound(board);
    }

    // If there is only one player remaining, they are the winner. In other cases, either players will
    // be eliminated together, and already be in eliminatedPlayers.
    if (board.getAllTokens().size() == 1) {
      eliminatedPlayers
          .add(board.getAllTokens().stream().map(tokenPlayerMap::get).collect(Collectors.toSet()));
    }

    // correct ordinals
    Collections.reverse(eliminatedPlayers);
    return eliminatedPlayers;

  }

  /**
   * Plays a round of Tsuro.
   *
   * @param board    the board for the play to be done on
   * @param numTiles the number of tiles to be handed out this round
   * @param createAction the {@link IAction} that will be performed this round.
   * @return the {@link IBoard} at the end of the round of Tsuro
   */
  private IBoard doRound(IBoard board, int numTiles,
      QuintFunc<IPlayer, List<ITile>, Token, IBoard, IRuleChecker, IAction> createAction) {

    Set<IPlayer> elimThisRound = new HashSet<>();

    for (IPlayer p : players) {
      if (!isEliminated(p) && !cheaters.contains(p) && !elimThisRound.contains(p)) {

        List<ITile> hand = getNTiles(numTiles);
        Token t = tokenPlayerMap.inverse().get(p);

        IBoard newMove = board;


        try {
          IBoard finalBoard = board;
          IAction actionPerformed = doFunctionForTime(() -> createAction.apply(p, hand, t,
              finalBoard, rules));
          newMove = validateMoveHandleAbnormal(p, board, t, actionPerformed, hand);
        } catch (Exception e) {
          // If the player does something shady and crashes, kick them
          newMove = newMove.kickPlayer(t);
          this.cheaters.add(p);
        }

        Set<IPlayer> elimPlayersThisTurn = getEliminatedPlayers(board, newMove);
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
   * Creates a Set<com.tsuro.player.IPlayer> that contains the players that are in oldBoard but not in newBoard.
   * (excluding cheaters)
   */
  private Set<IPlayer> getEliminatedPlayers(IBoard oldBoard, IBoard newBoard) {
    Set<Token> tokensPriorToTurn = oldBoard.getAllTokens();
    Set<Token> boardAllTokens = newBoard.getAllTokens();
    Set<Token> eliminatedThisTurn = Sets.difference(tokensPriorToTurn, boardAllTokens);

    return eliminatedThisTurn.stream()
        .map(tokenPlayerMap::get)
        .filter(pl -> !cheaters.contains(pl))
        .collect(Collectors.toSet());
  }

  /**
   * Does the first round of Tsuro, where players are placing down their first tiles.
   *
   * @param board the board for the game to be played on
   * @return the board after the round
   */
  private IBoard makeInitialMoves(IBoard board) {
    return doRound(board, 3, IPlayer::makeInitMove);
  }

  /**
   * Does any intermediate round of Tsuro, where players are working on eliminating each other.
   *
   * @param board The board for the round to be played on.
   * @return the board at the end of the round
   */
  private IBoard intermediateRound(IBoard board) {
    return doRound(board, 2, IPlayer::makeIntermediateMove);
  }

  /**
   * Validates the move that was performed by the player.
   *
   * @param player   player performing the move
   * @param oldBoard board before move done
   * @param t        token of the player
   * @param pAction  action player will take
   * @param hand     tiles player had when taking move
   * @return an Optional<IBoard> that contains an {@link IBoard} if the move passed this referee's
   * rules.
   */
  private IBoard validateMoveHandleAbnormal(IPlayer player, IBoard oldBoard, Token t,
      IAction pAction, Collection<ITile> hand) {
    Optional<IBoard> newMove = pAction.doActionIfValid(rules, oldBoard, t, hand);

    if (newMove.isPresent()) {
      IBoard newBoard = newMove.get();
      List<TsuroStatus> statii = newBoard.getStatuses();

      if (statii.contains(TsuroStatus.CONTAINS_LOOP)) {
        // This player's only option was to place a tile with a loop,
        // behaviour is to kick player and keep board same as before
        return oldBoard.kickPlayer(t);
      }

      return newBoard;

    } else {
      cheaters.add(player);
      return oldBoard.kickPlayer(t);
    }
  }

  /**
   * Gets the next i tiles for the referee's strategy as a List
   */
  private List<ITile> getNTiles(int i) {
    return IntStream.range(0, i).mapToObj((a) -> tileStrategy.next()).collect(Collectors.toList());
  }

  /**
   * Creates a BiMap (reversible map) of assigned Tokens -> the Players
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
   * Tells all players what colors they're playing as, and what colors their opponents are playing
   * as.
   */
  private void notifyPlayersOfColors() {
    tokenPlayerMap.inverse().forEach((key, value) -> {
      try {
        doFunctionForTime(() -> key.playingAs(value.color));
      } catch (Exception e) {
        cheaters.add(key);
      }
    });

    tokenPlayerMap.inverse().forEach((key, value) -> {
      try {
        doFunctionForTime(() -> key.otherPlayerAre(
            tokenPlayerMap.keySet().stream()
                .map(a -> a.color)
                .filter(a -> !a.equals(value.color))
                .collect(Collectors.toList())));
      } catch (Exception e) {
        cheaters.add(key);
      }
    });
  }

  /**
   * Determines if the given {@link IPlayer} has already been eliminated or not.
   */
  private boolean isEliminated(IPlayer p) {
    return eliminatedPlayers.stream().anyMatch(a -> a.contains(p));
  }


}
