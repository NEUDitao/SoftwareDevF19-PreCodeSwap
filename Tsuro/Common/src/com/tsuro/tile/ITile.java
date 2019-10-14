package com.tsuro.tile;

import java.util.Set;

/**
 * An ITile is one of:
 *   - {@link EmptyTile}
 *   - {@link TsuroTile}
 * and represents a Tile for a game. All implementations of this interface must be immutable.
 */
public interface ITile {

  /**
   * Returns a new instance of the Tile rotated by 90 degrees clockwise.
   *
   * @return new rotated tile by 90 degrees
   */
  public ITile rotate();

  /**
   * Finds {@link Location} that's connected to the {@param start} {@link Location}.
   *
   * @param start {@link Location} you start query at
   * @return end {@link Location} connected to {@param start}
   */
  public Location internalConnection(Location start);

  /**
   * Gets all the {@link Path}s within a {@link ITile}.
   *
   * @return a set of the {@link Path}s that this {@link ITile} contains
   */
  public Set<Path> getPaths();

  /**
   * Determines if the given tile has all the same paths as this tile. The tiles are only strictly
   * equal if they're in the same orientation.
   *
   * @param o The other tile to compare to
   * @return Whether the tiles are strictly equal
   */
  public default boolean strictEqual(ITile o) {
    return this.getPaths().equals(o.getPaths());
  }

  /**
   * Two tiles are equal if they can be transformed into each other through rotation.
   */
  @Override
  public boolean equals(Object o);

  /**
   * Determines if this tile is empty.
   */
  public boolean isEmpty();

}