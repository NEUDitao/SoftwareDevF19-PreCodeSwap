package com.tsuro.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  Path p1 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  Path p2 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  Path p3 = new Path(Location.NORTHWEST, Location.WESTSOUTH);
  Path p4 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  Tile t1 = new TsuroTile(p1, p2, p3, p4);

  Path p5 = new Path(Location.NORTHWEST, Location.EASTNORTH);
  Path p6 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  Path p7 = new Path(Location.EASTSOUTH, Location.WESTSOUTH);
  Path p8 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  Tile t2 = new TsuroTile(p5, p6, p7, p8);

  Path p9 = new Path(Location.NORTHWEST, Location.NORTHEAST);
  Path p10 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  Path p11 = new Path(Location.SOUTHEAST, Location.SOUTHWEST);
  Path p12 = new Path(Location.WESTNORTH, Location.WESTSOUTH);
  Tile loopy = new TsuroTile(p9, p10, p11, p12);

  Map<Point, Tile> m1;
  Map<Token, BoardLocation> m2;

  Board b1;

  @BeforeEach
  void setUpBoard() {

    m1 = new HashMap<>();
    m2 = new HashMap<>();

    m1.put(new Point(0, 0), t1);
    m1.put(new Point(2, 0), t2);
    m1.put(new Point(9, 9), loopy);

    m2.put(new ColoredToken(ColorString.BLACK), new BoardLocation(Location.SOUTHEAST, 0, 0));
    m2.put(new ColoredToken(ColorString.WHITE), new BoardLocation(Location.EASTNORTH, 2, 0));
    m2.put(new ColoredToken(ColorString.RED), new BoardLocation(Location.NORTHWEST, 9, 9));

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
    m2.remove(new ColoredToken(ColorString.WHITE));
    assertConstructionFailsInitial("Every tile requires an initial Token/BoardLoc pairing");
  }

  @Test
  void fromInitialPlacementsTileNotOnEdge() {
    m1.put(new Point(1, 1), loopy);
    m2.put(new ColoredToken(ColorString.GREEN), new BoardLocation(Location.WESTNORTH, 1, 1));
    assertConstructionFailsInitial("Initial tiles are not on edge");
  }

  @Test
  void fromInitalPlacementsTouching() {
    m1.put(new Point(1, 0), loopy);
    m2.put(new ColoredToken(ColorString.GREEN), new BoardLocation(Location.WESTNORTH, 1, 0));
    assertConstructionFailsInitial("Initial tiles are touching");
  }

  @Test
  void fromInitialPlacementsShareLocation() {
    m2.put(new ColoredToken(ColorString.RED), new BoardLocation(Location.SOUTHWEST, 0, 0));
    assertConstructionFailsInitial("Tokens cannot start on same Locations");
  }

  @Test
  void fromIntermediatePlacementsTokenOffTile() {
    m2.put(new ColoredToken(ColorString.RED), new BoardLocation(Location.SOUTHWEST, 8, 8));
    assertConstructionFailsBoth("Not all tokens are on a tile");
  }

  @Test
  void fromIntermediatePlacementsTokenDead() {
    m2.put(new ColoredToken(ColorString.WHITE), new BoardLocation(Location.NORTHWEST, 2, 0));
    assertConstructionFailsBoth("One of the tokens is dead");
  }

  @Test
  void fromIntermediatePlacementsFacingTile() {
    m1.put(new Point(0, 1), loopy);
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

  }

  @Test
  void placeTileOnBehalfOfPlayer() {
  }

  @Test
  void kickPlayer() {
    b1.kickPlayer(new ColoredToken(ColorString.BLACK));
    Set<Token> expected = new HashSet<>(Arrays
        .asList(new ColoredToken(ColorString.WHITE), new ColoredToken(ColorString.RED)));

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
    assertEquals(new BoardLocation(Location.SOUTHEAST, 0, 0),
        b1.getLocationOf(new ColoredToken(ColorString.BLACK)));
    assertThrows(IllegalArgumentException.class,
        () -> b1.getLocationOf(new ColoredToken(ColorString.BLUE)));
  }

  @Test
  void getAllTokens() {
    Set<Token> expected = new HashSet<>(Arrays
        .asList(new ColoredToken(ColorString.BLACK), new ColoredToken(ColorString.WHITE),
            new ColoredToken(ColorString.RED)));
    assertEquals(expected, b1.getAllTokens());
  }

  @Test
  void getSize() {
    assertEquals(new Dimension(15, 10), new TsuroBoard(15, 10).getSize());
    assertEquals(new Dimension(10, 10), new TsuroBoard().getSize());
  }
}