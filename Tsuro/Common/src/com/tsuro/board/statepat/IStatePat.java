package com.tsuro.board.statepat;

import com.tsuro.board.BoardLocation;
import com.tsuro.board.IToken;
import com.tsuro.tile.ITile;
import java.awt.Point;
import java.util.Map;
import lombok.NonNull;

/**
 * An interface to represent state-pats as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._state._pat%29
 */
public interface IStatePat {

  /**
   * Adds {@code this} StatePat to the maps used in placeIntermediateTiles.
   */
  void addToIntermediatePlacementMaps(@NonNull Map<Point, ITile> tiles,
      @NonNull Map<IToken, BoardLocation> playerLocs);
}
