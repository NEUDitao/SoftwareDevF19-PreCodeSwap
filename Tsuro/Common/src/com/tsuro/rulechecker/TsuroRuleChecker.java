package com.tsuro.rulechecker;

import com.tsuro.board.Board;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroStatus;
import com.tsuro.tile.EmptyTile;
import com.tsuro.tile.Tile;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a set of rules for a game of Tsuro where players cannot commit suicide unless it's
 * their only option, players cannot enter infinite loops, and initial placements must be bordered
 * by {@link EmptyTile}s.
 */
public class TsuroRuleChecker implements RuleChecker {

  @Override
  public boolean isValidInitialMove(Board board, Tile tile, Token player, BoardLocation loc,
      Collection<Tile> playerTiles) {

    if (!playerTiles.contains(tile)) {
      return false;
    }

    Board newBoard;

    try {
      newBoard = board.placeFirstTile(tile, player, loc);
    } catch (Exception e) {
      return false;
    }
    List<TsuroStatus> statii = newBoard.getStatuses();

    boolean anyStatusBad = statii.contains(TsuroStatus.INIT_TILE_NOT_ON_EDGE_BOARD)
        || statii.contains(TsuroStatus.INIT_TILE_TOUCHING_ANY)
        || statii.contains(TsuroStatus.INIT_TOKEN_SUICIDE);

    return !anyStatusBad;
  }

  @Override
  public boolean isValidIntermediateMove(Board board, Tile tile, Token player,
      Collection<Tile> playerTiles) {

    if (!playerTiles.contains(tile)) {
      return false;
    }

    Board newBoard;
    try {
      newBoard = board.placeTileOnBehalfOfPlayer(tile, player);
    } catch (Exception e) {
      return false;
    }

    List<TsuroStatus> statii = newBoard.getStatuses();

    boolean allStatusGood = true;

    if (statii.contains(TsuroStatus.CONTAINS_LOOP) || statii
        .contains(TsuroStatus.INTERMEDIATE_TOKEN_SUICIDE)) {
      allStatusGood = allRoadsLeadToSuicide(board, player, playerTiles);
    }

    return allStatusGood;

  }

  /**
   * Determines if all of the player's moves result in suicide on a given {@link Board}.
   */
  private boolean allRoadsLeadToSuicide(Board board, Token player, Collection<Tile> playerTiles) {

    return possibleBoardFromTile(board, player, playerTiles)
        .allMatch(a -> a.getStatuses().contains(TsuroStatus.INTERMEDIATE_TOKEN_SUICIDE)
            || a.getStatuses().contains(TsuroStatus.CONTAINS_LOOP));

  }

  /**
   * Creates a {@link Stream} of Boards that gives all boards with placements from all rotations of
   * the given {@link Tile}s
   */
  private static Stream<Board> possibleBoardFromTile(Board board, Token token,
      Collection<Tile> tiles) {

    Stream.Builder<Board> builder = Stream.builder();
    for (Tile t : tiles) {
      Tile rotatedTile = t;
      for (int i = 0; i < 4; i++) {
        rotatedTile = rotatedTile.rotate();
        try {
          builder.add(board.placeTileOnBehalfOfPlayer(rotatedTile, token));
        } catch (Exception e) {
          // Tile is so invalid that it can't even be acted on :o
        }
      }
    }

    return builder.build();
  }
}
