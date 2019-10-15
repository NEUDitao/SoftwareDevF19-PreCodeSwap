package com.tsuro.board;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * A Token for an (unimplemented) player.
 */
@AllArgsConstructor
@EqualsAndHashCode
public class Token {

  @NonNull
  public final ColorString color;

  /**
   * Returns the {@link Color} of this {@link Token}.
   */
  public Color getColor() {
    return color.color;
  }
}
