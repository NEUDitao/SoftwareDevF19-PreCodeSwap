package com.tsuro.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tsuro.tile.EmptyTile;
import com.tsuro.tile.Location;
import com.tsuro.tile.Path;
import com.tsuro.tile.Tile;
import com.tsuro.tile.TsuroTile;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TsuroBoardTest {

  private static final ColoredToken BLACK_TOKEN = new ColoredToken(ColorString.BLACK);
  private static final ColoredToken WHITE_TOKEN = new ColoredToken(ColorString.WHITE);
  private static final ColoredToken RED_TOKEN = new ColoredToken(ColorString.RED);
  private static final ColoredToken GREEN_TOKEN = new ColoredToken(ColorString.GREEN);
  private static final ColoredToken BLUE_TOKEN = new ColoredToken(ColorString.BLUE);

  private Path p1 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  private Path p2 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  private Path p3 = new Path(Location.NORTHWEST, Location.WESTSOUTH);
  private Path p4 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  private Tile t1 = new TsuroTile(p1, p2, p3, p4);

  private Path p5 = new Path(Location.NORTHWEST, Location.EASTNORTH);
  private Path p6 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  private Path p7 = new Path(Location.EASTSOUTH, Location.WESTSOUTH);
  private Path p8 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  private Tile t2 = new TsuroTile(p5, p6, p7, p8);

  private Path p9 = new Path(Location.NORTHWEST, Location.NORTHEAST);
  private Path p10 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  private Path p11 = new Path(Location.SOUTHEAST, Location.SOUTHWEST);
  private Path p12 = new Path(Location.WESTNORTH, Location.WESTSOUTH);
  private Tile loopy = new TsuroTile(p9, p10, p11, p12);

  private Map<Point, Tile> m1;
  private Map<Token, BoardLocation> m2;

  private Board b1;

  @BeforeEach
  void setUpBoard() {

    m1 = new HashMap<>();
    m2 = new HashMap<>();

    m1.put(new Point(0, 0), t1);
    m1.put(new Point(2, 0), t2);
    m1.put(new Point(9, 9), loopy);

    m2.put(BLACK_TOKEN, new BoardLocation(Location.EASTNORTH, 0, 0));
    m2.put(WHITE_TOKEN, new BoardLocation(Location.WESTNORTH, 2, 0));
    m2.put(RED_TOKEN, new BoardLocation(Location.NORTHWEST, 9, 9));

    // This calls fromIntermediatePlacements internally
    b1 = TsuroBoard.fromInitialPlacements(m1, m2);
  }

  void assertConstructionFailsInitial(String message) {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> TsuroBoard.fromInitialPlacements(m1, m2));
    assertEquals(message, e.getMessage());
  }

  void assertConstructionFailsIntermediate(String message) {
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
        () -> TsuroBoard.fromIntermediatePlacements(m1, m2));
    assertEquals(message, e.getMessage());
  }

  void assertConstructionFailsBoth(String message) {
    assertConstructionFailsInitial(message);
    assertConstructionFailsIntermediate(message);
  }

  @Test
  void fromInitialPlacementsMismatchedTileTokens() {
    m2.remove(WHITE_TOKEN);
    assertConstructionFailsInitial("Every tile requires an initial Token/BoardLoc pairing");
  }

  @Test
  void fromInitialPlacementsTileNotOnEdge() {
    m1.put(new Point(1, 1), loopy);
    m2.put(GREEN_TOKEN, new BoardLocation(Location.WESTNORTH, 1, 1));
    assertConstructionFailsInitial("Initial tiles are not on edge");
  }

  @Test
  void fromInitalPlacementsTouching() {
    m1.put(new Point(1, 0), loopy);
    m2.put(GREEN_TOKEN, new BoardLocation(Location.WESTNORTH, 1, 0));
    assertConstructionFailsInitial("Initial tiles are touching");
  }

  @Test
  void fromInitialPlacementsShareLocation() {
    m2.put(RED_TOKEN, new BoardLocation(Location.SOUTHWEST, 0, 0));
    assertConstructionFailsInitial("Tokens cannot start on same Locations");
  }

  @Test
  void fromIntermediatePlacementsTokenOffTile() {
    m2.put(RED_TOKEN, new BoardLocation(Location.SOUTHWEST, 8, 8));
    assertConstructionFailsBoth("Not all tokens are on a tile");
  }

  @Test
  void fromIntermediatePlacementsTokenDead() {
    m2.put(WHITE_TOKEN, new BoardLocation(Location.NORTHWEST, 2, 0));
    assertConstructionFailsBoth("One of the tokens is dead");
  }

  @Test
  void fromIntermediatePlacementsFacingTile() {
    m1.put(new Point(1, 0), loopy);
    assertConstructionFailsIntermediate("One of the tokens is facing another Tile");
  }

  @Test
  void fromIntermediatePlacementsMidTile() {
    m1.put(new Point(5, 5), loopy);
    assertConstructionFailsIntermediate(
        "Tile is not on the edge of the board and not touching another tile");
  }

  @Test
  void placeFirstTile() {
    b1.placeFirstTile(loopy, BLUE_TOKEN,
        new BoardLocation(Location.NORTHEAST, 0, 8));
    assertEquals(new BoardLocation(Location.NORTHEAST, 0, 8),
        b1.getLocationOf(BLUE_TOKEN));
    assertEquals(loopy, b1.getTileAt(0, 8));
  }

  @Test
  void placeFirstTileNotEdge() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.NORTHWEST, 5, 5)),
        "Given tile is not on edge of board");
  }

  @Test
  void placeFirstTileOffBoard() {
    assertThrows(IndexOutOfBoundsException.class,
        () -> b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.WESTNORTH, -1, 0)));
  }

  @Test
  void placeFirstTileOccupied() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.SOUTHWEST, 0, 0)),
        "Given location is already occupied");
  }

  @Test
  void placeFirstTileTouching() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.SOUTHWEST, 0, 1)),
        "Given tile touches another tile already on the board");
  }

  @Test
  void placeFirstTileAlreadyPlayer() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeFirstTile(loopy, BLACK_TOKEN, new BoardLocation(Location.SOUTHEAST, 5, 0)),
        "Given player is already on the board");
  }

  @Test
  void placeFirstTileFacingEdge() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.WESTNORTH, 0, 5)),
        "Player token is facing edge of board");
  }

  @Test
  void placeTileOnBehalfOfPlayer1() {
    b1.placeTileOnBehalfOfPlayer(t1.rotate().rotate().rotate(), BLACK_TOKEN);

    assertTrue(t1.rotate().rotate().rotate().strictEqual(b1.getTileAt(1, 0)));

    assertEquals(new BoardLocation(Location.SOUTHWEST, 2, 0), b1.getLocationOf(BLACK_TOKEN));
    assertEquals(new BoardLocation(Location.SOUTHEAST, 1, 0), b1.getLocationOf(WHITE_TOKEN));
  }

  @Test
  void placeTileOnBehalfOfPlayer2() {
    b1.placeTileOnBehalfOfPlayer(t2, WHITE_TOKEN);

    assertTrue(t2.strictEqual(b1.getTileAt(1, 0)));
    assertEquals(new BoardLocation(Location.SOUTHWEST, 1, 0), b1.getLocationOf(BLACK_TOKEN));
    assertThrows(IllegalArgumentException.class, () -> b1.getLocationOf(WHITE_TOKEN),
        "Token not on board");
  }

  void verifyB1NoChange() {
    assertEquals(new EmptyTile(), b1.getTileAt(1, 0));
    assertEquals(new BoardLocation(Location.EASTNORTH, 0, 0), b1.getLocationOf(BLACK_TOKEN));
    assertEquals(new BoardLocation(Location.WESTNORTH, 2, 0), b1.getLocationOf(WHITE_TOKEN));
  }

  @Test
  void placeTileLoop() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeTileOnBehalfOfPlayer(loopy, WHITE_TOKEN), "Board has loops");
    verifyB1NoChange();
  }

  @Test
  void placeTileNonexist() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeTileOnBehalfOfPlayer(loopy, BLUE_TOKEN),
        "Placing tile for nonexistent player");
    verifyB1NoChange();
  }

  @Test
  void kickPlayer() {
    b1.kickPlayer(BLACK_TOKEN);
    Set<Token> expected = new HashSet<>(Arrays
        .asList(WHITE_TOKEN, RED_TOKEN));

    assertEquals(expected, b1.getAllTokens());
  }

  @Test
  void getTileAt() {
    assertEquals(loopy, b1.getTileAt(9, 9));
    assertThrows(IndexOutOfBoundsException.class, () -> b1.getTileAt(10, 9));
    assertEquals(new EmptyTile(), b1.getTileAt(4, 4));
  }

  @Test
  void getLocationOf() {
    assertEquals(new BoardLocation(Location.EASTNORTH, 0, 0),
        b1.getLocationOf(BLACK_TOKEN));
    assertThrows(IllegalArgumentException.class,
        () -> b1.getLocationOf(BLUE_TOKEN));
  }

  @Test
  void getAllTokens() {
    Set<Token> expected = new HashSet<>(Arrays
        .asList(BLACK_TOKEN, WHITE_TOKEN,
            RED_TOKEN));
    assertEquals(expected, b1.getAllTokens());
  }

  @Test
  void getSize() {
    assertEquals(new Dimension(15, 10), new TsuroBoard(15, 10).getSize());
    assertEquals(new Dimension(10, 10), new TsuroBoard().getSize());
  }
}