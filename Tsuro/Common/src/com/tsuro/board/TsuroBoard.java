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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An implementation of a board for the game of Tsuro
 */
public class TsuroBoard implements Board {

  /**
   * The internal representation of the board
   */
  private final Tile[][] board;

  /**
   * The locations of all the tokens
   */
  private final Map<Token, BoardLocation> tokenLocations;

  private final Set<Token> loopingTokens;

  /**
   * Constructs a TsuroBoard with the given width and height, and no players
   */
  public TsuroBoard(int width, int height) {
    board = new Tile[width][height];

    for (Tile[] t : board) {
      Arrays.fill(t, new EmptyTile());
    }

    tokenLocations = new HashMap<>();
    loopingTokens = new HashSet<>();
  }

  /**
   * Constructs a TsuroBoard with default width and height (10x10)
   */
  public TsuroBoard() {
    this(10, 10);
  }

  /**
   * Creates a TsuroBoard as a clone of the given {@link Board}.
   */
  public TsuroBoard(Board board) {
    this(board.getSize().width, board.getSize().height);

    for (int i = 0; i < board.getSize().width; i++) {
      for (int j = 0; j < board.getSize().height; j++) {
        this.board[i][j] = board.getTileAt(i, j);
      }
    }

    for (Token c : board.getAllTokens()) {
      this.tokenLocations.put(c, board.getLocationOf(c));
    }

    loopingTokens.addAll(board.getLoopingTokens());
  }

  /**
   * Creates a board from a bunch of initial tile & player placements. Validates that placements are
   * valid.
   * @param tiles all initially placed tiles
   * @param locs  all initial player positions
   */
  public static Board fromInitialPlacements(Map<Point, Tile> tiles,
      Map<Token, BoardLocation> locs) {
    return fromInitialPlacements(new Dimension(10, 10), tiles, locs);
  }

  /**
   * Creates a board from a bunch of initial tile & player placements. Validates that placements are
   * valid.
   * @param size  The size of the board
   * @param tiles all initially placed tiles
   * @param locs  all initial player positions
   */
  public static Board fromInitialPlacements(Dimension size, Map<Point, Tile> tiles,
      Map<Token, BoardLocation> locs) {

    if (tiles.size() != locs.size()) {
      throw new IllegalArgumentException("Every tile requires an initial Token/BoardLoc pairing");
    }

    // board only used for edge matching.
    TsuroBoard newBoard = new TsuroBoard(size.width, size.height);

    if (!tiles.keySet().stream().allMatch(newBoard::isOnEdgeOfBoard)) {
      throw new IllegalArgumentException("Initial tiles are not on edge");
    }

    if (anyTouchingAny(tiles.keySet())) {
      throw new IllegalArgumentException("Initial tiles are touching");
    }

    if (anySameXYBoardLoc(locs.values())) {
      throw new IllegalArgumentException("Tokens cannot start on same Locations");
    }

    return fromIntermediatePlacements(size, tiles, locs);

  }

  /**
   * Creates a board from a bunch of intermediate tile & player placements. Attempts to validate
   * that the placements could reasonably have been caused by a series of valid moves.
   * @param tiles all placed tiles
   * @param locs  all player positions
   */
  public static Board fromIntermediatePlacements(Map<Point, Tile> tiles,
      Map<Token, BoardLocation> locs) {
    return fromIntermediatePlacements(new Dimension(10, 10), tiles, locs);
  }

  /**
   * Creates a board from a bunch of intermediate tile & player placements. Attempts to validate
   * that the placements could reasonably have been caused by a series of valid moves.
   * @param size  size of the board
   * @param tiles all placed tiles
   * @param locs  all player positions
   */
  public static Board fromIntermediatePlacements(Dimension size, Map<Point, Tile> tiles,
      Map<Token, BoardLocation> locs) {

    if (!locs.values().stream().allMatch(a -> tiles.containsKey(new Point(a.x, a.y)))) {
      throw new IllegalArgumentException("Not all tokens are on a tile");
    }

    TsuroBoard newBoard = new TsuroBoard(size.width, size.height);

    if (!locs.values().stream().map(TsuroBoard::nextPointFromLoc).allMatch(newBoard::isOnBoard)) {
      throw new IllegalArgumentException("One of the tokens is dead");
    }

    if (locs.values().stream().map(TsuroBoard::nextPointFromLoc)
        .anyMatch(tiles.keySet()::contains)) {
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
  public Board placeFirstTile(Tile tile, Token token, BoardLocation loc) {
    final Point point = new Point(loc.x, loc.y);
    if (!isOnEdgeOfBoard(point)) { // Rule checker
      throw new IllegalArgumentException("Given tile is not on edge of board");
    }

    if (!isEmpty(point)) { // Exception
      throw new IllegalArgumentException("Given location is already occupied");
    }

    if (touchingAny(point, nonEmptyPoints())) { // Rule checker
      throw new IllegalArgumentException("Given tile touches another tile already on the board");
    }

    if (tokenLocations.containsKey(token)) { // Exception
      throw new IllegalArgumentException("Given player is already on the board");
    }

    if (!isOnBoard(nextPointFromLoc(loc))) { // Rule checker
      // TODO: something
      throw new IllegalArgumentException("Player token is facing edge of board");
    }

    TsuroBoard newBoard = new TsuroBoard(this);
    newBoard.board[point.x][point.y] = tile;
    newBoard.tokenLocations.put(token, loc);

    return newBoard;
  }

  @Override
  public Board placeTileOnBehalfOfPlayer(Tile tile, Token token) {
    if (!tokenLocations.containsKey(token)) {
      throw new IllegalArgumentException("Placing tile for nonexistent player");
    }

    final Point point = nextPointFromLoc(tokenLocations.get(token));

    // This should never happen because the tokens always face an empty spot on the board
    if (!isOnBoard(point) || !board[point.x][point.y].isEmpty()) {
      throw new IllegalStateException("Inconsistent state of game");
    }

    TsuroBoard newBoard = new TsuroBoard(this);

    newBoard.board[point.x][point.y] = tile;

    newBoard.updateTokenLocations(point);

    if (!newBoard.getAllTokens().contains(token)) {
      //TODO do something about suicide if needed
    }

    return newBoard;
  }

  /**
   * For all tokens that face the newly placed tile, update their location
   * @param pointForNewTile Where the new tile is
   * @throws IllegalStateException If the board has loops. If this is thrown, the token locations
   * are not modified.
   */
  private void updateTokenLocations(Point pointForNewTile) {

    Map<Token, BoardLocation> newLocations = tokenLocations.entrySet().stream()
        .filter(a -> nextPointFromLoc(a.getValue()).equals(pointForNewTile))
        .collect(Collectors.toMap(Entry::getKey, a -> {
          try {
            return moveTokenAsFarAsPossible(a.getValue());
          } catch (IllegalStateException e) {
            loopingTokens.add(a.getKey());
            return a.getValue();
          }
        }));

    tokenLocations.putAll(newLocations);

    List<Token> tokensOffBoard = newLocations.entrySet().stream()
        .filter(a -> !isOnBoard(nextPointFromLoc(a.getValue())))
        .map(Entry::getKey)
        .collect(Collectors.toList());

    tokensOffBoard.forEach(this::removePlayer);
  }

  /**
   * Gets the board location after moving anything on a given location forward as far as possible.
   * Doesn't remove anything from the board- the returned location will either face an empty tile,
   * or it will face the edge of the board.
   * @param location Where to start from for moving forward
   * @return The final resting place of anything that starts at location
   * @throws IllegalStateException if the board has loops
   */
  private BoardLocation moveTokenAsFarAsPossible(BoardLocation location) {

    BoardLocation initialLoc = location;

    while (isOnBoard(nextPointFromLoc(location))
        && !isEmpty(nextPointFromLoc(location))) {

      Point thisPoint = new Point(location.x, location.y);

      Point nextPoint = nextPointFromLoc(location);

      Tile tile = getTileAt(nextPoint.x, nextPoint.y);
      Location locationOnPrevTile = location.location;
      Location entryPort = locationOnPrevTile.getPaired();
      Location newExitPort = tile.internalConnection(entryPort);

      location = new BoardLocation(newExitPort, nextPoint.x, nextPoint.y);

      if (location.equals(initialLoc)) {
        throw new IllegalStateException("Board has loops");
      }

    }

    return location;
  }

  @Override
  public Board kickPlayer(Token token) {
    TsuroBoard newBoard = new TsuroBoard(this);
    newBoard.removePlayer(token);
    return newBoard;
  }

  /**
   * Removes the player represented by the given token from this board
   */
  private void removePlayer(Token token) {
    this.tokenLocations.remove(token);
    this.loopingTokens.remove(token);
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
    if (!tokenLocations.containsKey(token)) {
      throw new IllegalArgumentException("Token not on board");
    }
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

  @Override
  public Set<Token> getLoopingTokens() {
    return new HashSet<>(this.loopingTokens);
  }


  /**
   * Is the Tile at the given point empty?
   */
  private boolean isEmpty(Point p) {
    return board[p.x][p.y].isEmpty();
  }

  /**
   * Gets all Points on the board that aren't empty
   */
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

  /**
   * Gets the Point offset for the given {@link Location}
   */
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

  /**
   * Gets the connecting Point that the given BoardLocation feeds into
   */
  private static Point nextPointFromLoc(BoardLocation loc) {
    Point offSet = locToOffset(loc.location);

    offSet.translate(loc.x, loc.y);

    return offSet;
  }


}
