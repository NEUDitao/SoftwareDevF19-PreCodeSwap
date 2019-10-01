package com.tsuro.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Tile for the game of Tsuro.
 */
public class TsuroTile implements Tile {

  private Set<Path> paths;

  /**
   * Creates a {@link Tile} from the {@link Path}s given. Should be given exactly 4 paths.
   */
  public TsuroTile(Collection<Path> paths) {

    List<Location> valuesList = new ArrayList<>(Arrays.asList(Location.values()));
    this.paths = new HashSet<>();

    for (Path p : paths) {
      List<Location> locs = Arrays.asList(p.l1, p.l2);
      if (!valuesList.containsAll(locs)) {
        throw new IllegalArgumentException(
            "Path given in list was not valid, or used locations multiple times");
      }
      valuesList.removeAll(locs);
      this.paths.add(p);
    }

    if (!valuesList.isEmpty()) {
      throw new IllegalArgumentException("Given paths didn't use all possible Locations");
    }

  }

  /**
   * Convenience constructor.
   */
  public TsuroTile(Path... paths) {
    this(Arrays.asList(paths));
  }

  @Override
  public Tile rotate() {
    return new TsuroTile(paths.stream().map(Path::rotate).collect(Collectors.toList()));
  }

  @Override
  public Location internalConnection(Location start) {
    Objects.requireNonNull(start);
    for (Path p : paths) {
      if (p.l1 == start) {
        return p.l2;
      } else if (p.l2 == start) {
        return p.l1;
      }
    }
    throw new IllegalStateException("start Location does not have a corresponding end");
  }


  @Override
  public Set<Path> getPaths() {
    return new HashSet<>(this.paths);
  }

  /**
   * Gets all of the rotations of this tile.
   */
  private List<TsuroTile> getAllRotations() {
    TsuroTile currTile = this;

    List<TsuroTile> rotatedTiles = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      currTile = (TsuroTile) currTile.rotate();
      rotatedTiles.add(currTile);
    }
    return rotatedTiles;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }

    if (!(o instanceof TsuroTile)) {
      return false;
    }

    TsuroTile tile = (TsuroTile) o;
    List<TsuroTile> tiles = tile.getAllRotations();
    return tiles.stream().anyMatch(l -> l.paths.equals(this.paths));

  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public int hashCode() {
    return this.getAllRotations().stream().mapToInt(t -> t.paths.hashCode()).sum();
  }
}
