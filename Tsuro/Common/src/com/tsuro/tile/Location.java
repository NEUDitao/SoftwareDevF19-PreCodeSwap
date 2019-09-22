package com.tsuro.tile;

public enum Location {

  NORTHWEST, NORTHEAST, EASTNORTH, EASTSOUTH, SOUTHEAST, SOUTHWEST, WESTSOUTH, WESTNORTH;

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

  private Location rotated;
  private Location paired;

  Location rotate() {
    return this.rotated;
  }

  Location getPaired() {
    return this.paired;
  }
}
