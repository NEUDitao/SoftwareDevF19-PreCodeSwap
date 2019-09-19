package cs4500.hw2.json;

import com.google.gson.annotations.SerializedName;
import cs4500.hw2.NotARequestException;
import java.awt.Color;
import java.util.NoSuchElementException;

public enum ColorString {
  @SerializedName("white") WHITE(Color.WHITE),
  @SerializedName("black") BLACK(Color.BLACK),
  @SerializedName("red") RED(Color.RED),
  @SerializedName("green") GREEN(Color.GREEN),
  @SerializedName("blue") BLUE(Color.BLUE);

  public final Color color;

  ColorString(Color color){
    this.color = color;
  }

  public static ColorString fromColor(Color color) {
    for (ColorString c : ColorString.values()) {
      if (color.equals(c.color)) {
        return c;
      }
    }

    throw new NotARequestException("Given color is not a ColorString");
  }
}
