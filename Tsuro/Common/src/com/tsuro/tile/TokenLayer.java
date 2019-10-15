package com.tsuro.tile;

import com.tsuro.board.Token;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.LayerUI;
import lombok.NonNull;

public class TokenLayer extends LayerUI<JComponent> {

  private final Location loc;
  private final Token token;
  private final double RADIUS = .1;

  public TokenLayer(@NonNull Token token, @NonNull Location loc) {
    super();
    this.token = token;
    this.loc = loc;

  }

  @Override
  public void paint(@NonNull Graphics g, @NonNull JComponent comp) {

    super.paint(g, comp);

    g.setColor(token.getColor());
    final int W = comp.getWidth();
    final int H = comp.getHeight();
    final int xLocation = (int) (loc.renderX * W);
    final int yLocation = (int) (loc.renderY * H);
    final int ovalXRadius = (int) (W * RADIUS);
    final int ovalYRadius = (int) (H * RADIUS);

    g.fillOval(xLocation - ovalXRadius, yLocation - ovalYRadius, ovalXRadius * 2, ovalYRadius * 2);


  }


}
