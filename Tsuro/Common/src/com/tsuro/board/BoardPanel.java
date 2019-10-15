package com.tsuro.board;

import com.tsuro.tile.TileComponent;
import com.tsuro.tile.TokenLayer;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import lombok.NonNull;

public class BoardPanel extends JPanel {

  public BoardPanel() {
    super(new GridBagLayout());
  }

  public void drawBoard(@NonNull IBoard board) {

    this.removeAll();

    for (int x = 0; x < board.getSize().width; x++) {
      for (int y = 0; y < board.getSize().height; y++) {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        JComponent comp = new TileComponent(board.getTileAt(x, y));
        comp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        for (Token t : board.getAllTokens()) {

          BoardLocation loc = board.getLocationOf(t);

          if (loc.x == x && loc.y == y) {
            comp = new JLayer<>(comp, new TokenLayer(t, loc.location));
          }
        }

        this.add(comp, c);

      }
    }

    this.invalidate();

  }


}
