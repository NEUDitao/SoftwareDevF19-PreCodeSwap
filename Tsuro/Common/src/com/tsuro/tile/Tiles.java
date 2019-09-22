package com.tsuro.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Tiles {


  public static void main(String[] args) {

    Set<Tile> tiles = getAllTiles();

    System.out.println(tiles.size());
  }

  private static Set<Tile> getAllTiles() {
    Set<Tile> returnable = new HashSet<>();

    List<Location> locations = Arrays.asList(Location.values());

    getallPaths(locations).forEach(a -> returnable.add(new TsuroTile(a)));

    return returnable;
  }

  private static List<List<Path>> getallPaths(List<Location> locs) {

    ArrayList<List<Path>> theList = new ArrayList<>();

    if (locs.isEmpty()) {
      theList.add(new ArrayList<>());
      return theList;
    }

    List<Path> paths = getAllPathsFrom(locs.get(0), locs.subList(1, locs.size()));

    for (Path p : paths) {
      ArrayList<Location> clonedList = new ArrayList<>(locs);
      clonedList.remove(p.l1);
      clonedList.remove(p.l2);
      List<List<Path>> loopList = getallPaths(clonedList);
      loopList.forEach(a -> a.add(p));
      theList.addAll(loopList);
    }

    return theList;
  }

  private static List<Path> getAllPathsFrom(Location l, List<Location> rest) {

    return rest.stream().map(a -> new Path(l, a)).collect(Collectors.toList());
  }


}
