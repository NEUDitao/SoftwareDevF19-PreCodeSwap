package cs4500.hw2.json;

import com.google.gson.annotations.SerializedName;
import java.awt.Color;

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
}
