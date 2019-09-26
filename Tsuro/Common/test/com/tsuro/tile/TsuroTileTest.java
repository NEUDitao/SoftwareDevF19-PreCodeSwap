package com.tsuro.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A Class to test TsuroTiles.
 */
class TsuroTileTest {

  Path p1 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  Path p2 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  Path p3 = new Path(Location.NORTHWEST, Location.WESTSOUTH);
  Path p4 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  Tile t1 = new TsuroTile(p1, p2, p3, p4);

  Path p5 = new Path(Location.WESTSOUTH, Location.EASTSOUTH);
  Path p6 = new Path(Location.WESTNORTH, Location.NORTHEAST);
  Path p7 = new Path(Location.NORTHWEST, Location.EASTNORTH);
  Path p8 = new Path(Location.SOUTHWEST, Location.SOUTHEAST);
  Tile t2 = new TsuroTile(p5, p6, p7, p8);

  Path p9 = new Path(Location.NORTHWEST, Location.NORTHEAST);
  Path p10 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  Path p11 = new Path(Location.SOUTHEAST, Location.SOUTHWEST);
  Path p12 = new Path(Location.WESTNORTH, Location.WESTSOUTH);
  Tile t3 = new TsuroTile(p9, p10, p11, p12);


  @org.junit.jupiter.api.Test
  void rotate() {

    assertEquals(t1.rotate().getPaths(), t2.getPaths());
    assertEquals(t1.rotate().internalConnection(Location.WESTNORTH), Location.NORTHEAST);
    assertEquals(t1.rotate(), t2);
    assertEquals(t1.rotate().rotate().rotate().rotate().getPaths(), t1.getPaths());
    assertNotEquals(t1.rotate().getPaths(), t1.getPaths());
  }

  @org.junit.jupiter.api.Test
  void internalConnection() {
    assertEquals(t1.internalConnection(Location.SOUTHWEST), Location.WESTNORTH);
    assertEquals(t1.internalConnection(Location.WESTNORTH), Location.SOUTHWEST);
    assertEquals(t1.internalConnection(Location.NORTHWEST), Location.WESTSOUTH);

  }

  @org.junit.jupiter.api.Test
  void getPaths() {
    assertEquals(t1.getPaths(), new HashSet<Path>(Arrays.asList(p1, p2, p3, p4)));
    Set s = t1.getPaths();
    s.clear();
    assertEquals(s.size(), 0);
    assertEquals(t1.getPaths().size(), 4);
    assertEquals(t1.getPaths(), new HashSet<Path>(Arrays.asList(p1, p2, p3, p4)));

  }

  @org.junit.jupiter.api.Test
  void testEquals() {
    assertTrue(t1.equals(t2));
    assertTrue(t1.equals(t2.rotate().rotate()));
    assertFalse(t1.equals(t3));
    assertFalse(t1.equals(t3.rotate()));
  }

  @org.junit.jupiter.api.Test
  void testConstructors() {
    Tile t4 = new TsuroTile(new HashSet<>(Arrays.asList(p1, p2, p3, p4)));
    assertEquals(t1, t4);
    assertThrows(IllegalArgumentException.class, () -> new TsuroTile(p1, p2, p3, p4, p5));
    assertThrows(IllegalArgumentException.class, () -> new TsuroTile(p1));
    assertThrows(IllegalArgumentException.class, () -> new TsuroTile(p1, p2, p3, p3));
    assertThrows(IllegalArgumentException.class,
        () -> new TsuroTile(p1, p1, p1, p1, p1, p1, p1, p1, p1, p1, p1, p1, p1, p1, p1));
  }

}