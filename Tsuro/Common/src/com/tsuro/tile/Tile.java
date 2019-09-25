package com.tsuro.tile;

import java.util.List;
import java.util.Set;

public interface Tile {

  /**
   * Rotates this tile. Does not mutate.
   * @return new rotated tile
   */
  public Tile rotate();

  /**
   * Finds Location that's connected to the {@param start} location.
   * @param start location you start at
   * @return end location
   */
  public Location internalConnection(Location start);

  /**
   * Gets all the {@link Path}s within a {@link Tile}
   * @return a set of the {@link Path}s
   */
  public Set<Path> getPaths();


}
