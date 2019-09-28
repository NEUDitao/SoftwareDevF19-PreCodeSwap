package com.tsuro.board;

import com.tsuro.tile.Location;

/**
 * A utility class that contains a coordinate and a {@link Location}
 */
public class BoardLocation {

  public final Location location;
  public final int x;
  public final int y;

  public BoardLocation(Location location, int x, int y) {
    this.location = location;
    this.x = x;
    this.y = y;
  }

  public boolean sameXY(BoardLocation other) {
    return this.x == other.x && this.y == other.y;
  }
}
