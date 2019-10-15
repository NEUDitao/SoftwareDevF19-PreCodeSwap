package com.tsuro.observer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tsuro.action.actionpat.ActionPat;
import com.tsuro.tile.ITile;
import java.lang.reflect.Type;
import lombok.NonNull;

public class TurnPatDeserializer implements JsonDeserializer<TurnPat> {

  @Override
  public TurnPat deserialize(@NonNull JsonElement jsonElement, @NonNull Type type,
      @NonNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

    if (jsonElement.isJsonArray()) {

      JsonArray ja = jsonElement.getAsJsonArray();

      if (ja.size() == 3) {
        ActionPat actionPat = jsonDeserializationContext.deserialize(ja.get(0), ActionPat.class);
        ITile t1 = jsonDeserializationContext
            .deserialize(tileIndexToTilePat(ja.get(1)), ITile.class);
        ITile t2 = jsonDeserializationContext
            .deserialize(tileIndexToTilePat(ja.get(2)), ITile.class);

        return new TurnPat(actionPat, t1, t2);
      } else {
        throw new JsonParseException("Not a TurnPat");
      }

    } else {
      throw new JsonParseException("Not an array");
    }
  }

  /**
   * Turns the given element into a JsonArray of [element, 0]
   */
  private JsonArray tileIndexToTilePat(JsonElement element) {
    JsonArray ja = new JsonArray();
    ja.add(element);
    ja.add(0);
    return ja;
  }

}
