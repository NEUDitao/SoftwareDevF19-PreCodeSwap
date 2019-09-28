package com.tsuro.tile;

import java.util.Set;

public interface Tile {

  /**
   * Rotates this tile. Does not mutate.
   *
   * @return new rotated tile
   */
  public Tile rotate();

  /**
   * Finds Location that's connected to the {@param start} location.
   *
   * @param start location you start at
   * @return end location
   */
  public Location internalConnection(Location start);

  /**
   * Gets all the {@link Path}s within a {@link Tile}
   *
   * @return a set of the {@link Path}s
   */
  public Set<Path> getPaths();

  /**
   * Determines if the given tile has all the same paths as this tile. The tiles are only strictly
   * equal if they're in the same orientation.
   *
   * @param o The other tile to compare to
   * @return Whether the tiles are strictly equal
   */
  public default boolean strictEqual(Tile o) {
    return this.getPaths().equals(o.getPaths());
  }

  ;


  /**
   * Two tiles are equal if they can be transformed into each other through rotation.
   */
  @Override
  public boolean equals(Object o);

}
