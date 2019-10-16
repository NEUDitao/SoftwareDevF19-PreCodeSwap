package com.tsuro.player;

import com.tsuro.tile.ITile;
import com.tsuro.tile.TileComponent;
import com.tsuro.utils.RenderUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A panel representing the Hand of a Player.
 */
public class HandPanel extends JPanel {

  /**
   * Super Constructor.
   */
  public HandPanel() {
    super(new GridBagLayout());
  }

  /**
   * Paints a Player's hand represented by the collection of tiles.
   */
  public void paintHand(Collection<ITile> tiles) {

    this.removeAll();

    int i = 0;
    for (ITile tile : tiles) {

      GridBagConstraints c = RenderUtils.createDefaultGBC(0, i);

      JComponent comp = new TileComponent(tile);
      i += 1;

      this.add(comp, c);

    }

    this.invalidate();
  }

}
