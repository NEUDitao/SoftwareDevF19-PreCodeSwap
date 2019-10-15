package com.tsuro.tile;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents the connection between two locations inside of a Tile.
 */
@AllArgsConstructor
public class Path {

  @NonNull
  final Location l1;
  @NonNull
  final Location l2;


  /**
   * Rotates this path. Returns new Path. No mutation.
   */
  Path rotate() {
    return new Path(l1.rotate(), l2.rotate());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Path path = (Path) o;
    return (l1 == path.l1 && l2 == path.l2)
        || (l1 == path.l2 && l2 == path.l1);
  }

  @Override
  public int hashCode() {
    return l1.hashCode() + l2.hashCode();
  }
}
