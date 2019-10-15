package com.tsuro.board;

import static com.tsuro.TsuroTestHelper.BLACK_TOKEN;
import static com.tsuro.TsuroTestHelper.BLUE_TOKEN;
import static com.tsuro.TsuroTestHelper.GREEN_TOKEN;
import static com.tsuro.TsuroTestHelper.RED_TOKEN;
import static com.tsuro.TsuroTestHelper.WHITE_TOKEN;
import static com.tsuro.TsuroTestHelper.b1;
import static com.tsuro.TsuroTestHelper.loopy;
import static com.tsuro.TsuroTestHelper.m1;
import static com.tsuro.TsuroTestHelper.m2;
import static com.tsuro.TsuroTestHelper.t1;
import static com.tsuro.TsuroTestHelper.t2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tsuro.TsuroTestHelper;
import com.tsuro.tile.EmptySquare;
import com.tsuro.tile.ITile;
import com.tsuro.tile.Location;
import com.tsuro.tile.Path;
import com.tsuro.tile.TsuroTile;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TsuroBoardTest {

  @BeforeEach
  void setUpBoard() {
    TsuroTestHelper.setUpBoard();
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
  void fromIntermediatePlacementsAltSize() {
    m1.put(new Point(9, 14), loopy);
    m2.put(GREEN_TOKEN, new BoardLocation(Location.NORTHWEST, 9, 14));

    b1 = TsuroBoard.fromIntermediatePlacements(new Dimension(10, 15), m1, m2);

    assertEquals(loopy, b1.getTileAt(9, 14));
    assertEquals(new BoardLocation(Location.NORTHWEST, 9, 14), b1.getLocationOf(GREEN_TOKEN));
    verifyB1NoChange();
  }

  @Test
  void fromInitialPlacementsAltSize() {
    m1.put(new Point(9, 14), loopy);
    m2.put(GREEN_TOKEN, new BoardLocation(Location.NORTHWEST, 9, 14));

    b1 = TsuroBoard.fromInitialPlacements(new Dimension(10, 15), m1, m2);

    assertEquals(loopy, b1.getTileAt(9, 14));
    assertEquals(new BoardLocation(Location.NORTHWEST, 9, 14), b1.getLocationOf(GREEN_TOKEN));
    verifyB1NoChange();
  }

  @Test
  void placeFirstTile() {
    b1 = b1.placeFirstTile(loopy, BLUE_TOKEN,
        new BoardLocation(Location.NORTHEAST, 0, 8));
    assertEquals(new BoardLocation(Location.NORTHEAST, 0, 8),
        b1.getLocationOf(BLUE_TOKEN));
    assertEquals(loopy, b1.getTileAt(0, 8));
  }

  @Test
  void placeFirstTileNotEdge() {

    b1 = b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.NORTHWEST, 5, 5));
    assertTrue(b1.getStatuses().contains(TsuroStatus.INIT_TILE_NOT_ON_EDGE_BOARD));
    assertEquals(b1.getTileAt(5, 5), loopy);
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
    b1 = b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.EASTNORTH, 8, 9));
    assertTrue(b1.getStatuses().contains(TsuroStatus.INIT_TILE_TOUCHING_ANY));
    assertTrue(b1.getStatuses().contains(TsuroStatus.CONTAINS_LOOP));
    assertTrue(b1.getLoopingTokens().contains(BLUE_TOKEN));
  }

  @Test
  void placeFirstTileAlreadyPlayer() {
    assertThrows(IllegalArgumentException.class,
        () -> b1.placeFirstTile(loopy, BLACK_TOKEN, new BoardLocation(Location.SOUTHEAST, 5, 0)),
        "Given player is already on the board");
  }

  @Test
  void placeFirstTileFacingEdge() {
    b1 = b1.placeFirstTile(loopy, BLUE_TOKEN, new BoardLocation(Location.WESTNORTH, 0, 5));
    assertTrue(b1.getStatuses().contains(TsuroStatus.INIT_TOKEN_SUICIDE));
    assertFalse(b1.getAllTokens().contains(BLUE_TOKEN));
  }

  @Test
  void placeTileOnBehalfOfPlayer1() {
    b1 = b1.placeTileOnBehalfOfPlayer(t1.rotate().rotate().rotate(), BLACK_TOKEN);

    assertTrue(t1.rotate().rotate().rotate().strictEqual(b1.getTileAt(1, 0)));

    assertEquals(new BoardLocation(Location.SOUTHWEST, 2, 0), b1.getLocationOf(BLACK_TOKEN));
    assertEquals(new BoardLocation(Location.SOUTHEAST, 1, 0), b1.getLocationOf(WHITE_TOKEN));
  }

  @Test
  void placeTileOnBehalfOfPlayer2() {
    b1 = b1.placeTileOnBehalfOfPlayer(t2, WHITE_TOKEN);

    assertTrue(t2.strictEqual(b1.getTileAt(1, 0)));
    assertEquals(new BoardLocation(Location.SOUTHWEST, 1, 0), b1.getLocationOf(BLACK_TOKEN));
    assertThrows(IllegalArgumentException.class, () -> b1.getLocationOf(WHITE_TOKEN),
        "Token not on board");
  }

  void verifyB1NoChange() {
    assertEquals(new EmptySquare(), b1.getTileAt(1, 0));
    assertEquals(new BoardLocation(Location.EASTNORTH, 0, 0), b1.getLocationOf(BLACK_TOKEN));
    assertEquals(new BoardLocation(Location.WESTNORTH, 2, 0), b1.getLocationOf(WHITE_TOKEN));
  }

  @Test
  void placeTileLoop() {
    b1 = b1.placeTileOnBehalfOfPlayer(loopy, WHITE_TOKEN);
    assertEquals(loopy, b1.getTileAt(1, 0));
    assertEquals(b1.getLoopingTokens(), new HashSet<>(Collections.singletonList(BLACK_TOKEN)));
    assertEquals(b1.getLocationOf(BLACK_TOKEN), new BoardLocation(Location.EASTNORTH, 0, 0));
    assertEquals(b1.getLocationOf(WHITE_TOKEN), new BoardLocation(Location.EASTSOUTH, 2, 0));
    assertTrue(b1.getStatuses().contains(TsuroStatus.CONTAINS_LOOP));
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
    b1 = b1.kickPlayer(BLACK_TOKEN);
    Set<Token> expected = new HashSet<>(Arrays
        .asList(WHITE_TOKEN, RED_TOKEN));

    assertEquals(expected, b1.getAllTokens());
  }

  @Test
  void getTileAt() {
    assertEquals(loopy, b1.getTileAt(9, 9));
    assertThrows(IndexOutOfBoundsException.class, () -> b1.getTileAt(10, 9));
    assertEquals(new EmptySquare(), b1.getTileAt(4, 4));
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

  @Test
  void testCopyConstructor() {
    IBoard clone = new TsuroBoard(b1);

    assertEquals(b1.getAllTokens(), clone.getAllTokens());

    assertEquals(b1.getSize(), clone.getSize());

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        assertEquals(b1.getTileAt(i, j), clone.getTileAt(i, j));
      }
    }

    for (Token c : b1.getAllTokens()) {
      assertEquals(b1.getLocationOf(c), clone.getLocationOf(c));
    }
  }

  @Test
  void testBoardImmutable() {
    b1.placeTileOnBehalfOfPlayer(loopy, WHITE_TOKEN);
    assertEquals(new EmptySquare(), b1.getTileAt(1, 0));
  }

  @Test
  void testInitBoard() {
    IBoard tBoard = new TsuroBoard();
    tBoard = tBoard.placeFirstTile(loopy, WHITE_TOKEN, new BoardLocation(Location.SOUTHEAST, 0, 0));
    assertEquals(loopy, tBoard.getTileAt(0, 0));
  }

  @Test
  void testBoardCollision() {
    ITile tNew = new TsuroTile(new Path(Location.WESTNORTH, Location.EASTSOUTH),
        new Path(Location.WESTSOUTH, Location.EASTNORTH),
        new Path(Location.NORTHEAST, Location.NORTHWEST),
        new Path(Location.SOUTHEAST, Location.SOUTHWEST));

    b1 = b1.placeTileOnBehalfOfPlayer(tNew, WHITE_TOKEN);
    assertEquals(b1.getLocationOf(WHITE_TOKEN), b1.getLocationOf(BLACK_TOKEN));
    assertEquals(b1.getLocationOf(WHITE_TOKEN), new BoardLocation(Location.EASTSOUTH, 2, 0));
    assertEquals(b1.getStatuses(), Collections.singletonList(TsuroStatus.COLLISION));
  }
}