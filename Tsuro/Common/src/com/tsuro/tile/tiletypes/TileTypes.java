package com.tsuro.tile.tiletypes;

import com.tsuro.tile.Tile;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates a list of all possible tiles, and converts tile-pats into {@link Tile}s
 */
public class TileTypes {


  private final List<Tile> tiles;

  /**
   * Creates all of the tiles from the indices given and stores it in this.
   */
  public TileTypes(List<TileIndex> indices) {

    tiles = indices.stream().sorted().map(TileIndex::turnToTile).collect(Collectors.toList());

  }

  /**
   * Gets the tile at the given index rotated by the given degrees.
   *
   * @param index    the index of the tile from the tile-index list found
   *                 <a href="https://ccs.neu.edu/home/matthias/4500-f19/tsuro-tiles-index.json">here</a>
   * @param rotation The degrees to rotate by *INVARIANT*: MUST BE NON-NEGATIVE AND DIVISBLE BY 90
   * @return The tile at the index rotate by the given degrees.
   */
  public Tile getTileAtIndexWithRotation(int index, int rotation) {

    if (rotation < 0 || rotation % 90 != 0) {
      throw new IllegalArgumentException("Rotation cannot be done other than in right angles");
    }

    Tile returnable = tiles.get(index);

    int idx = rotation;
    while (idx > 0) {
      returnable = returnable.rotate();
      idx -= 90;
    }

    return returnable;
  }

  /**
   * Turns the given Tile into a TilePat.
   */
  public TilePat turnTileIntoTilePat(Tile tile) {

    Tile theTile;
    for (int i = 0; i < tiles.size(); i++) {
      if (tile.equals(tiles.get(i))) {

        theTile = tiles.get(i);
        for (int j = 0; j < 4; j++) {
          if (theTile.strictEqual(tile)) {
            return new TilePat(i, j * 90);
          } else {
            theTile = theTile.rotate();
          }
        }
      }
    }

    throw new IllegalArgumentException("Given tile doesn't exist?");


  }


}
