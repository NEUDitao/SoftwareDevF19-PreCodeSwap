package com.tsuro.tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.QuadCurve2D;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.swing.JComponent;
import lombok.NonNull;

/**
 * A component for rendering tiles.
 */
public class TileComponent extends JComponent {

  private final ITile tile;
  private final Color[] colors = {Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};
  private final int thickness;

  /**
   * Creates Tile Component that will render given tile with lines of a given thickness.
   */
  TileComponent(ITile tile, int thickness) {
    Objects.requireNonNull(tile);
    this.tile = tile;
    this.thickness = thickness;
  }

  /**
   * Creates a TileComponent that will render the given tile.
   */
  public TileComponent(ITile tile) {
    this(tile, 3);
  }

  /**
   * Paints the tile.
   */
  public void paint(@NonNull Graphics g) {

    Graphics2D g2d = (Graphics2D) g;
    super.paint(g);

    if (this.tile.isEmpty()) {
      return;
    }

    Set<QuadCurve2D> curves = new HashSet<>();
    final int W = this.getWidth();
    final int H = this.getHeight();

    for (Path p : tile.getPaths()) {
      curves.add(
          new QuadCurve2D.Double(p.l1.renderX * W, p.l1.renderY
              * H, .5 * W, .5 * H, p.l2.renderX * W, p.l2.renderY * H));
    }

    g2d.setStroke(new BasicStroke(thickness));

    int i = 0;
    for (QuadCurve2D c : curves) {
      g2d.setColor(colors[i]);
      g2d.draw(c);
      i += 1;
    }

  }
}
