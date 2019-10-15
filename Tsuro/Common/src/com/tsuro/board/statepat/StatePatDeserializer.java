package com.tsuro.board.statepat;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.ColorString;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import com.tsuro.tile.Location;
import java.awt.Point;
import java.lang.reflect.Type;
import lombok.NonNull;

/**
 * Serializes an intermediate-place as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._intermediate._place%29
 * into an {@code IntermediatePlace}.
 */
public class StatePatDeserializer implements JsonDeserializer<IStatePat> {

  @Override
  public IStatePat deserialize(@NonNull JsonElement jsonElement, @NonNull Type type,
      @NonNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

    if (jsonElement.isJsonArray()) {
      JsonArray ja = jsonElement.getAsJsonArray();

      if (ja.size() == 5) {
        ITile t = jsonDeserializationContext.deserialize(ja.get(0), ITile.class);
        ColorString cs = jsonDeserializationContext.deserialize(ja.get(1), ColorString.class);
        Location loc = jsonDeserializationContext.deserialize(ja.get(2), Location.class);
        BoardLocation bl = new BoardLocation(loc, ja.get(3).getAsInt(), ja.get(4).getAsInt());

        return new InitialPlace(t, new Token(cs), bl);
      } else if (ja.size() == 3) {
        ITile t = jsonDeserializationContext.deserialize(ja.get(0), ITile.class);
        Point p = new Point(ja.get(1).getAsInt(), ja.get(2).getAsInt());

        return new IntermediatePlace(t, p);
      } else {
        throw new JsonParseException("Not a state-pat");
      }

    } else {
      throw new JsonParseException("Not an array");
    }
  }

}
