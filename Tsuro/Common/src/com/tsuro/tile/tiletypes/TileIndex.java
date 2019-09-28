package com.tsuro.tile.tiletypes;


import com.google.gson.annotations.SerializedName;
import com.tsuro.tile.Location;
import com.tsuro.tile.Path;
import com.tsuro.tile.Tile;
import com.tsuro.tile.TsuroTile;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Tile Index as a Java Object.
 */
public class TileIndex implements Comparable<TileIndex> {

  public List<List<Location>> edges;
  @SerializedName("tile#")
  public int tileIndex;

  public TileIndex(List<List<Location>> edges, int tileIndex) {
    this.edges = edges;
    this.tileIndex = tileIndex;
  }


  @Override
  public int compareTo(@NotNull TileIndex tileIndex) {
    return Integer.compare(this.tileIndex, tileIndex.tileIndex);
  }

  /**
   * Turns an instance of a {@link TileIndex} into a {@link Tile}
   *
   * @return a Tile with the edges in edges
   */
  Tile turnToTile() {

    return new TsuroTile(edges.stream().map(a -> new Path(a.get(0), a.get(1))).collect(
        Collectors.toList()));

  }
}
