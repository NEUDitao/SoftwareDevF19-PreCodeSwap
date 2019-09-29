package com.tsuro.board;

import java.util.Objects;

public class ColoredToken implements Token {


  private final ColorString color;

  public ColoredToken(ColorString color) {

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
    ColoredToken that = (ColoredToken) o;
    return color == that.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }
}
