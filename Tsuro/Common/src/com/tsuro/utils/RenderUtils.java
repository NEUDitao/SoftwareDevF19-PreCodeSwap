package com.tsuro.utils;

import java.awt.GridBagConstraints;
import lombok.experimental.UtilityClass;

/**
 * Utilities for rendering.
 */
@UtilityClass
public class RenderUtils {

  /**
   * Creates a GridBagConstraints with gridx and gridy of x and y.
   */
  public static GridBagConstraints createDefaultGBC(int x, int y) {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = x;
    c.gridy = y;
    c.weightx = 1;
    c.weighty = 1;
    c.fill = GridBagConstraints.BOTH;
    return c;
  }

}
