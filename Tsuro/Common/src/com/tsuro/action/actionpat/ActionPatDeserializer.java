package com.tsuro.action.actionpat;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tsuro.board.ColorString;
import com.tsuro.board.Token;
import com.tsuro.tile.ITile;
import java.lang.reflect.Type;
import lombok.NonNull;

/**
 * Deserializes an action-pat as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._action._pat%29
 * into an {@link ActionPat}.
 */
public class ActionPatDeserializer implements JsonDeserializer<ActionPat> {

  @Override
  public ActionPat deserialize(@NonNull JsonElement jsonElement, @NonNull Type type,
      @NonNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    if (jsonElement.isJsonArray()) {
      JsonArray ja = jsonElement.getAsJsonArray();
      if (ja.size() == 2) {
        Token token = new Token(
            jsonDeserializationContext.deserialize(ja.get(0), ColorString.class));
        ITile tile = jsonDeserializationContext.deserialize(ja.get(1), ITile.class);
        return new ActionPat(token, tile);
      }
    }
    throw new JsonParseException("Not an action-pat");
  }
}
