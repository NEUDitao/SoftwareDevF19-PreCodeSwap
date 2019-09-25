package com.tsuro.tile;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class Tiles {


  public static void main(String[] args) {

    Set<Tile> tiles = getAllTiles();

    System.out.println(tiles.size());

    renderAllTiles(tiles);
  }

  private static void renderAllTiles(Set<Tile> tiles) {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new GridBagLayout());
    frame.add(panel);

    List<Tile> listTiles = new ArrayList<>(tiles);

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        if (i == 5 && j == 5) {
          continue;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = i;
        c.gridy = j;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        TileComponent comp = new TileComponent(listTiles.get(i * 6 + j));
        comp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        panel.add(comp, c);
      }
    }

    frame.pack();
    frame.setSize(1000, 1000);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

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
