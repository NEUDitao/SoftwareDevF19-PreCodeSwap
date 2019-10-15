package com.tsuro.board.statepat;

import com.tsuro.board.BoardLocation;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import java.awt.Point;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Creates an IntermediatePlace which has a ITile, and the location it'll be placed.
 */
@AllArgsConstructor
public class IntermediatePlace implements IStatePat {

  @NonNull
  private final ITile tile;
  @NonNull
  private final Point location;

  @Override
  public void addToIntermediatePlacementMaps(Map<Point, ITile> tiles,
      Map<Token, BoardLocation> playerLocs) {
    tiles.put(new Point(location), tile);
  }
}
