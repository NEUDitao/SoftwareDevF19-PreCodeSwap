package com.tsuro.board;

import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * The valid colors of a Colored token
 */
@AllArgsConstructor
public enum ColorString {
  @SerializedName("white") WHITE(Color.WHITE),
  @SerializedName("black") BLACK(Color.BLACK),
  @SerializedName("red") RED(Color.RED),
  @SerializedName("green") GREEN(Color.GREEN),
  @SerializedName("blue") BLUE(Color.BLUE);

  @NonNull
  public final Color color;

  /**
   * Creates a ColorString given a java.awt.color.
   */
  public static ColorString fromColor(@NonNull Color color) {
    for (ColorString c : ColorString.values()) {
      if (color.equals(c.color)) {
        return c;
      }
    }

    throw new IllegalArgumentException("Given color is not a ColorString");
  }
}
