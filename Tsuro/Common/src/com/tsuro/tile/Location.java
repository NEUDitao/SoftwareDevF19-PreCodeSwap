package com.tsuro.tile;

public enum Location {

  NORTHWEST(1.0 / 3, 0), NORTHEAST(2.0 / 3, 0), EASTNORTH(1, 1.0 / 3),
  EASTSOUTH(1, 2.0 / 3), SOUTHEAST(2.0 / 3, 1), SOUTHWEST(1.0 / 3, 1),
  WESTSOUTH(0, 2.0 / 3), WESTNORTH(0, 1.0 / 3);

  static {
    NORTHWEST.rotated = EASTNORTH;
    NORTHEAST.rotated = EASTSOUTH;
    EASTNORTH.rotated = SOUTHEAST;
    EASTSOUTH.rotated = SOUTHWEST;
    SOUTHEAST.rotated = WESTSOUTH;
    SOUTHWEST.rotated = WESTNORTH;
    WESTSOUTH.rotated = NORTHWEST;
    WESTNORTH.rotated = NORTHEAST;

    NORTHWEST.paired = SOUTHWEST;
    NORTHEAST.paired = SOUTHEAST;
    EASTNORTH.paired = WESTNORTH;
    EASTSOUTH.paired = WESTSOUTH;
    SOUTHEAST.paired = NORTHEAST;
    SOUTHWEST.paired = NORTHWEST;
    WESTSOUTH.paired = EASTSOUTH;
    WESTNORTH.paired = EASTNORTH;
  }

  public final double y;
  public final double x;

  private Location rotated;
  private Location paired;

  Location(double x, double y) {
    this.x = x;
    this.y = y;
  }

  Location rotate() {
    return this.rotated;
  }

  Location getPaired() {
    return this.paired;
  }
}
