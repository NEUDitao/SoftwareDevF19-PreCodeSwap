package com.tsuro.tile;

import com.tsuro.board.Token;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.LayerUI;
import lombok.NonNull;

/**
 * A {@link LayerUI} for {@link TileComponent}s, where this Layer draws the token on a Tile.
 */
public class TokenLayer extends LayerUI<JComponent> {

  private final Location loc;
  private final int index;
  private final Token token;
  private final double RADIUS = .1;
  private final double INDEX_PERCENT = .1;

  /**
   * Construct with a preset index
   */
  public TokenLayer(@NonNull Token token, @NonNull Location loc) {
    this(token, loc, 0);
  }

  /**
   * Normal constructor that calls super() for convention.
   *
   * @param index Draws the token INDEX_PERCENT% smaller for each index so that tokens can overlap
   */
  public TokenLayer(@NonNull Token token, @NonNull Location loc, int index) {
    super();
    this.token = token;
    this.loc = loc;
    this.index = index;
  }

  @Override
  public void paint(@NonNull Graphics g, @NonNull JComponent comp) {

    super.paint(g, comp);

    g.setColor(token.getColor());

    double radiusToUse = RADIUS * Math.pow(1 - INDEX_PERCENT, index);

    final int W = comp.getWidth();
    final int H = comp.getHeight();
    final int xLocation = (int) (loc.renderX * W);
    final int yLocation = (int) (loc.renderY * H);
    final int ovalXRadius = (int) (W * radiusToUse);
    final int ovalYRadius = (int) (H * radiusToUse);

    g.fillOval(xLocation - ovalXRadius, yLocation - ovalYRadius, ovalXRadius * 2, ovalYRadius * 2);


  }


}
