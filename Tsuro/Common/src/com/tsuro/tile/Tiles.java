package com.tsuro.tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.danielmelcer.shellshare.ImageToShellKt;

/**
 * Class for proving there are 35 unique tiles in Tsuro, and draws them.
 */
public class Tiles {

  final static int HEADLESS_TILE_SIZE = 6;

  /**
   * Runs the program.
   */
  public static void main(String[] args) {

    Set<Tile> tiles = getAllTiles();

    System.out.println(tiles.size());

    if (!GraphicsEnvironment.isHeadless()) {
      renderAllTiles(tiles);
    } else {
      headlessRenderAllTiles(tiles);
    }
  }

  /**
   * Renders given tiles in a 6x6 grid.
   *
   * @param tiles tiles to be rendered
   */
  private static void renderAllTiles(Collection<Tile> tiles) {
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

  private static void headlessRenderAllTiles(Collection<Tile> tiles) {
    List<Tile> listTiles = new ArrayList<>(tiles);

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        if (i == 5 && j == 5) {
          continue;
        }

        TileComponent comp = new TileComponent(listTiles.get(i*6 + j), 6);
        comp.setSize(new Dimension(100, 100));
        Point p = new Point(i * HEADLESS_TILE_SIZE * 2, j * HEADLESS_TILE_SIZE);
        Dimension d = new Dimension(HEADLESS_TILE_SIZE * 2, HEADLESS_TILE_SIZE);
        BufferedImage buff = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        comp.paint(buff.getGraphics());
        System.out.print(ImageToShellKt.imageToShell(buff, d, p));


      }
    }
  }

  /**
   * Gets all the possible {@link Tile} formations in a game of Tsuro
   *
   * @return Set of {@link Tile}s
   */
  private static Set<Tile> getAllTiles() {
    Set<Tile> returnable = new HashSet<>();

    List<Location> locations = Arrays.asList(Location.values());

    getallPaths(locations).forEach(a -> returnable.add(new TsuroTile(a)));

    return returnable;
  }

  /**
   * Gets all possible path combinations off of locations given.
   *
   * @param locs must be an even number of {@link Location}s
   * @return a List of all the combinations of Paths
   */
  private static List<List<Path>> getallPaths(List<Location> locs) {

    Objects.requireNonNull(locs);

    ArrayList<List<Path>> theList = new ArrayList<>();

    // if no locations given, only one possible combination of paths
    if (locs.isEmpty()) {
      theList.add(new ArrayList<>());
      return theList;
    }

    List<Path> paths = getAllPathsFrom(locs.get(0), locs.subList(1, locs.size()));

    // For all paths from starting location
    for (Path p : paths) {
      ArrayList<Location> clonedList = new ArrayList<>(locs);
      clonedList.remove(p.l1);
      clonedList.remove(p.l2);

      // find all other permutations of paths without p
      List<List<Path>> loopList = getallPaths(clonedList);

      // adds p to those permutations
      loopList.forEach(a -> a.add(p));

      // repeat for other perms
      theList.addAll(loopList);
    }

    return theList;
  }

  /**
   * Gets all the paths from a singular Location to all the other locations in rest
   *
   * @param l    starting locations
   * @param rest locations to get to
   * @return List of all Paths from starting to ending locations
   */
  private static List<Path> getAllPathsFrom(Location l, List<Location> rest) {

    return rest.stream().map(a -> new Path(l, a)).collect(Collectors.toList());
  }


}
