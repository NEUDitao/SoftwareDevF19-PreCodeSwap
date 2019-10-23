package com.tsuro.referee;

import com.tsuro.tile.ITile;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import lombok.NonNull;

/**
 * An {@link Iterator<ITile>} that cycles through the injected Tiles.
 */
public class CycleThroughTiles implements Iterator<ITile> {

  @NonNull
  private Queue<ITile> tiles;

  /**
   * Creates an Iterator off the given tiles.
   */
  public CycleThroughTiles(Collection<ITile> tiles) {
    this.tiles = new LinkedList<>(tiles);
  }

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public ITile next() {
    ITile returnable = tiles.poll();
    tiles.add(returnable);
    return returnable;
  }
}
