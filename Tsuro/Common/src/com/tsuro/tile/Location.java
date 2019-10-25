package com.tsuro.tile;

import com.google.gson.annotations.SerializedName;

/**
 * Represents one of 8 port locations on a {@link ITile}
 */
public enum Location {

  @SerializedName("A") NORTHWEST(1.0 / 3, 0),
  @SerializedName("B") NORTHEAST(2.0 / 3, 0),
  @SerializedName("C") EASTNORTH(1, 1.0 / 3),
  @SerializedName("D") EASTSOUTH(1, 2.0 / 3),
  @SerializedName("E") SOUTHEAST(2.0 / 3, 1),
  @SerializedName("F") SOUTHWEST(1.0 / 3, 1),
  @SerializedName("G") WESTSOUTH(0, 2.0 / 3),
  @SerializedName("H") WESTNORTH(0, 1.0 / 3);

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

  public final double renderY;
  public final double renderX;

  private Location rotated;
  private Location paired;

  /**
   * Constructs a location's ratios to be used when rendering where the location is
   *
   * @param renderX x-coord
   * @param renderY y-coord
   */
  Location(double renderX, double renderY) {
    this.renderX = renderX;
    this.renderY = renderY;
  }

  /**
   * Rotates this Location
   *
   * @return Location that's been rotated to
   */
  public Location rotate() {
    return this.rotated;
  }

  /**
   * Gets the Location on a another tile the starting port would be linked to
   */
  public Location getPaired() {
    return this.paired;
  }


}
