package com.tsuro.tile;

import java.util.List;
import java.util.Set;

public interface Tile {


  public Tile rotate();

  public Location internalConnection(Location start);

  public Port portOn(Location start);

  public Set<Path> getPaths();


}
