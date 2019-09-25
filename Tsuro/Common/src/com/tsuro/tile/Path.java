package com.tsuro.tile;

/**
 * Represents the connection between two locations inside of a Tile.
 */
public class Path {

  final Location l1;
  final Location l2;

  public Path(Location l1, Location l2) {
    this.l1 = l1;
    this.l2 = l2;
  }

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
