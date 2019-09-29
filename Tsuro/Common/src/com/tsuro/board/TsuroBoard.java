package com.tsuro.board;

import com.tsuro.tile.EmptyTile;
import com.tsuro.tile.Location;
import com.tsuro.tile.Tile;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TsuroBoard implements Board {

  private final Tile[][] board;
  private final Map<Token, BoardLocation> tokenLocations;

  public TsuroBoard(int x, int y) {
    board = new Tile[x][y];

    for (Tile[] t : board) {
      Arrays.fill(t, new EmptyTile());
    }

    tokenLocations = new HashMap<>();
  }

  public TsuroBoard() {
    this(10, 10);
  }

  public static Board fromInitialPlacements(Map<Point, Tile> tiles,
      Map<Token, BoardLocation> locs) {

    if (tiles.size() != locs.size()) {
      throw new IllegalArgumentException("Every tile requires an initial Token/BoardLoc pairing");
    }

    // board only used for edge matching.
    TsuroBoard newBoard = new TsuroBoard();

    if (!tiles.keySet().stream().allMatch(newBoard::isOnEdgeOfBoard)) {
      throw new IllegalArgumentException("Initial tiles are not on edge");
    }

    if (anyTouchingAny(tiles.keySet())) {
      throw new IllegalArgumentException("Initial tiles are touching");
    }

    if (anySameXYBoardLoc(locs.values())) {
      throw new IllegalArgumentException("Tokens cannot start on same Locations");
    }

    return fromIntermediatePlacements(tiles, locs);

  }


  public static Board fromIntermediatePlacements(Map<Point, Tile> tiles,
      Map<Token, BoardLocation> locs) {

    if (!locs.values().stream().allMatch(a -> tiles.containsKey(new Point(a.x, a.y)))) {
      throw new IllegalArgumentException("Not all tokens are on a tile");
    }

    TsuroBoard newBoard = new TsuroBoard();

    if (!locs.values().stream().map(TsuroBoard::nextPointFromLoc).allMatch(newBoard::isOnBoard)) {
      throw new IllegalArgumentException("One of the tokens is dead");
    }

    if (!locs.values().stream().map(TsuroBoard::nextPointFromLoc)
        .allMatch(tiles.keySet()::contains)) {
      throw new IllegalArgumentException("One of the tokens is facing another Tile");
    }

    if (!tiles.keySet().stream()
        .allMatch(a -> newBoard.isOnEdgeOfBoard(a) || touchingAny(a, tiles.keySet()))) {
      throw new IllegalArgumentException(
          "Tile is not on the edge of the board and not touching another tile");
    }

    for (Entry<Point, Tile> e : tiles.entrySet()) {
      newBoard.board[e.getKey().x][e.getKey().y] = e.getValue();
    }

    newBoard.tokenLocations.putAll(locs);

    return newBoard;

  }

  /**
   * Determines if the given point is in the bounds of the board.
   */
  private boolean isOnBoard(Point point) {
    return point.x >= 0 && point.y >= 0 && point.x < board.length
        && point.y < board[point.x].length;
  }


  @Override
  public void placeFirstTile(Tile tile, Token token, BoardLocation loc) {
    final Point point = new Point(loc.x, loc.y);
    if (!isOnEdgeOfBoard(point)) {
      throw new IllegalArgumentException("Given tile is not on edge of board");
    }

    if (!isEmpty(point)) {
      throw new IllegalArgumentException("Given location is already occupied");
    }

    if (touchingAny(point, nonEmptyPoints())) {
      throw new IllegalArgumentException("Given tile touches another tile already on the board");
    }

    if (tokenLocations.containsKey(token)) {
      throw new IllegalArgumentException("Given player is already on the board");
    }

    if (!isOnBoard(nextPointFromLoc(loc))) {
      throw new IllegalArgumentException("Player token is facing edge of board");
    }

    board[point.x][point.y] = tile;
  }

  @Override
  public void placeTileOnBehalfOfPlayer(Tile tile, Token token) {
    if (!tokenLocations.containsKey(token)) {
      throw new IllegalArgumentException("Placing tile for nonexistent player");
    }

    final Point point = nextPointFromToken(token);

    // This should never happen because the tokens always face an empty spot on the board
    if (!isOnBoard(point) || !board[point.x][point.y].isEmpty()) {
      throw new IllegalStateException("Inconsistent state of game");
    }

    board[point.x][point.y] = tile;

    tokenLocations.entrySet().stream()
        .filter(a -> nextPointFromLoc(a.getValue()).equals(point))
        .map(Entry::getKey)
        .forEach(this::moveTokenForward);
  }

  private void moveTokenForward(Token token) {
    while (tokenLocations.containsKey(token)
        && isOnBoard(nextPointFromToken(token))
        && !isEmpty(nextPointFromToken(token))) {

      Point nextPoint = nextPointFromToken(token);
      Tile tile = getTileAt(nextPoint.x, nextPoint.y);
      Location entryPort = tokenLocations.get(token).location.getPaired();
      Location newExitPort = tile.internalConnection(entryPort);

      tokenLocations.put(token, new BoardLocation(newExitPort, nextPoint.x, nextPoint.y));
    }

    if (!isOnBoard(nextPointFromToken(token))) {
      kickPlayer(token); // The token is on the edge of the board
    }
  }

  private Point nextPointFromToken(Token token) {
    return nextPointFromLoc(tokenLocations.get(token));
  }

  @Override
  public void kickPlayer(Token token) {
    tokenLocations.remove(token);
  }

  @Override
  public Tile getTileAt(int x, int y) {
    if (isOnBoard(new Point(x, y))) {
      return board[x][y];
    }

    throw new IndexOutOfBoundsException("The given index was not on the board");
  }

  @Override
  public BoardLocation getLocationOf(Token token) {
    // ok since BoardLocations are immutable
    return tokenLocations.get(token);
  }

  @Override
  public Set<Token> getAllTokens() {
    // favour immutability
    return Collections.unmodifiableSet(tokenLocations.keySet());
  }

  @Override
  public Dimension getSize() {
    return new Dimension(board.length, board[0].length);
  }


  private boolean isEmpty(Point p) {
    return board[p.x][p.y].isEmpty();
  }

  private Set<Point> nonEmptyPoints() {
    Set<Point> points = new HashSet<>();
    for (int x = 0; x < board.length; x++) {
      for (int y = 0; y < board[x].length; y++) {
        if (!board[x][y].isEmpty()) {
          points.add(new Point(x, y));
        }
      }
    }

    return points;
  }

  /**
   * Determines if the given point is on the edge of the board
   *
   * @param point the point to be queried
   * @return is the poitn is on the dge
   */
  private boolean isOnEdgeOfBoard(Point point) {
    return point.x == 0 || point.y == 0 || point.x == board.length - 1
        || point.y == board[point.x].length - 1;
  }

  /**
   * Determines if any of the points in set are touching.
   */
  private static boolean anyTouchingAny(Set<Point> set) {
    return set.stream().anyMatch(a -> touchingAny(a, set));
  }

  /**
   * Determines if any of the points in set are touching point.
   */
  private static boolean touchingAny(Point point, Set<Point> set) {
    return set.stream().anyMatch(a -> touchingPoints(point, a));
  }

  /**
   * Determines if the two given points are touching. Points with the same coordinates, or on
   * diagonals aren't touching.
   */
  private static boolean touchingPoints(Point p1, Point p2) {
    int diffX = Math.abs(p1.x - p2.x);
    int diffY = Math.abs(p1.y - p2.y);
    return (diffX + diffY) == 1;
  }

  /**
   * Determines if any of the {@link BoardLocation}s in values are at the same X Y coordinate
   */
  private static boolean anySameXYBoardLoc(Collection<BoardLocation> values) {
    for (BoardLocation i : values) {
      ArrayList<BoardLocation> list = new ArrayList<>(values);
      list.remove(i);
      for (BoardLocation j : list) {
        if (j.sameXY(i)) {
          return true;
        }
      }
    }
    return false;
  }

  private static Point locToOffset(Location loc) {
    switch (loc) {
      case NORTHEAST:
      case NORTHWEST:
        return new Point(0, -1);
      case EASTNORTH:
      case EASTSOUTH:
        return new Point(1, 0);
      case SOUTHEAST:
      case SOUTHWEST:
        return new Point(0, 1);
      case WESTNORTH:
      case WESTSOUTH:
        return new Point(-1, 0);
    }
// should never get here
    throw new IllegalArgumentException("loc wasn't a Location???????????????????????????");
  }

  private static Point nextPointFromLoc(BoardLocation loc) {
    Point offSet = locToOffset(loc.location);

    offSet.translate(loc.x, loc.y);

    return offSet;
  }


}
