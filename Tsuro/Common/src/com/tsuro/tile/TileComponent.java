package com.tsuro.tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.QuadCurve2D;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.swing.JComponent;

public class TileComponent extends JComponent {

  private Tile tile;
  private final Color colors[] = {Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};

  TileComponent(Tile tile) {
    Objects.requireNonNull(tile);
    this.tile = tile;
  }

  public void paint(Graphics g) {
    Objects.requireNonNull(g);

    Graphics2D g2d = (Graphics2D) g;

    Set<QuadCurve2D> curves = new HashSet<>();
    final int W = this.getWidth();
    final int H = this.getHeight();

    for (Path p : tile.getPaths()) {
      curves.add(
          new QuadCurve2D.Double(p.l1.x * W, p.l1.y * H, .5 * W, .5 * H, p.l2.x * W, p.l2.y * H));
    }

    g2d.setStroke(new BasicStroke(3));

    int i = 0;
    for (QuadCurve2D c : curves) {
      g2d.setColor(colors[i]);
      g2d.draw(c);
      i += 1;
    }

    super.paint(g);

  }
}
