package com.tsuro.board;

import com.tsuro.tile.Location;
import java.util.Objects;

/**
 * A utility class that contains a coordinate and a {@link Location}. Represents a unique position
 * on a {@link Board}
 */
public class BoardLocation {

  public final Location location;
  public final int x;
  public final int y;

  /**
   * Creates a BoardLocation at the given {@link Location} at the x and y coordinates
   */
  public BoardLocation(Location location, int x, int y) {
    this.location = location;
    this.x = x;
    this.y = y;
  }

  /**
   * Determines if the other BoardLocation's coordinates are the same as this BoardLocation's
   * coordinates
   */
  public boolean sameXY(BoardLocation other) {
    return this.x == other.x && this.y == other.y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoardLocation that = (BoardLocation) o;
    return x == that.x &&
        y == that.y &&
        location == that.location;
  }

  @Override
  public int hashCode() {
    return Objects.hash(location, x, y);
  }
}
