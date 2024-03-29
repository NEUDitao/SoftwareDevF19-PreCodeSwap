package com.tsuro.tile;

import java.util.Set;

/**
 * Represents an Empty Tile. Has nothing. Nada. Zilch.
 */
public class EmptySquare implements ITile {

  @Override
  public ITile rotate() {
    return this;
  }

  @Override
  public Location internalConnection(Location start) {
    throw new IllegalArgumentException("There are no internal connections");
  }

  @Override
  public Set<Path> getPaths() {
    throw new IllegalArgumentException("What paths, you peasant?");
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public boolean strictEqual(ITile o) {
    return o.isEmpty();
  }


  @Override
  public int hashCode() {
    return 4;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    return (obj instanceof ITile) && ((ITile) obj).isEmpty();
  }
}
