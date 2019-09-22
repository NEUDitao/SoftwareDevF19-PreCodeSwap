package com.tsuro.tile;

public interface Tile {


  public Tile rotate();

  public Location internalConnection(Location start);

  public Port portOn(Location start);


}
