package com.tsuro.board;

import java.util.Objects;

/**
 * Represents a {@link IToken} with a Color.
 */
public class ColoredIToken implements IToken {


  public final ColorString color;

  /**
   * Creates a ColoredToken from the given {@link ColorString}
   */
  public ColoredIToken(ColorString color) {

    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColoredIToken that = (ColoredIToken) o;
    return color == that.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }
}
