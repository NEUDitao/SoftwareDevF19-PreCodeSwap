package com.tsuro.board;

import com.tsuro.tile.Location;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * A utility class that contains a coordinate and a {@link Location}. Represents a unique position
 * on a {@link IBoard}
 */
@AllArgsConstructor
@ToString
public class BoardLocation {

  @NonNull
  public final Location location;
  @NonNull
  public final int x;
  @NonNull
  public final int y;

  /**
   * Determines if this {@link BoardLocation} and the given one have the same XY.
   */
  public boolean sameXY(@NonNull BoardLocation other) {
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
