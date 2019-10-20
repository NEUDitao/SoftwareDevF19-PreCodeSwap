package com.tsuro.board;

import com.tsuro.tile.TileComponent;
import com.tsuro.tile.TokenLayer;
import com.tsuro.utils.RenderUtils;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import lombok.NonNull;

/**
 * A {@link JPanel} that renders an {@link IBoard}.
 */
public class BoardPanel extends JPanel {

  /**
   * Constructor calls super for {@link JPanel} convention.
   */
  public BoardPanel() {
    super(new GridBagLayout());
  }

  /**
   * Draws the given {@link IBoard}.
   */
  public void drawBoard(@NonNull IBoard board) {
    this.drawBoard(board, new ArrayList<>());
  }

  /**
   * Draws the given {@link IBoard} where all points given are highlighted red.
   */
  public void drawBoard(@NonNull IBoard board, @NonNull List<Point> pointsToHighlight) {

    this.removeAll();

    for (int x = 0; x < board.getSize().width; x++) {
      for (int y = 0; y < board.getSize().height; y++) {

        // Draws individual tiles
        GridBagConstraints c = RenderUtils.createDefaultGBC(x, y);

        JComponent comp = new TileComponent(board.getTileAt(x, y));
        if (pointsToHighlight.contains(new Point(x, y))) {
          comp.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
        } else {
          comp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }

        int index = 0;

        for (Token t : board.getAllTokens()) {

          BoardLocation loc = board.getLocationOf(t);

          if (loc.x == x && loc.y == y) {
            comp = new JLayer<>(comp, new TokenLayer(t, loc.location, index));
          }

          index++;
        }

        this.add(comp, c);


      }
    }

    this.invalidate();

  }


}
