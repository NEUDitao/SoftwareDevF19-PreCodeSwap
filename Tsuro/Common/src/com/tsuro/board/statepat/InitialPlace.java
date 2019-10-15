package com.tsuro.board.statepat;

import com.tsuro.board.BoardLocation;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import java.awt.Point;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Represents an initial-place as defined in http://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._initial._place%29
 */
@AllArgsConstructor
public class InitialPlace implements IStatePat {

  @NonNull
  public final ITile tile;
  @NonNull
  public final Token token;
  @NonNull
  public final BoardLocation boardLocation;


  @Override
  public void addToIntermediatePlacementMaps(Map<Point, ITile> tiles,
      Map<Token, BoardLocation> playerLocs) {
    tiles.put(new Point(boardLocation.x, boardLocation.y), this.tile);
    playerLocs.put(token, this.boardLocation);
  }
}
