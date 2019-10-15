package com.tsuro.board.statepat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.ColorString;
import com.tsuro.tile.ITile;
import com.tsuro.tile.Location;
import java.lang.reflect.Type;
import lombok.NonNull;

/**
 * Serializes an initial-place as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._initial._place%29
 * into an {@code InitialPlace}.
 */
public class InitialPlaceSerializer implements JsonSerializer<InitialPlace> {

  @Override
  public JsonElement serialize(@NonNull InitialPlace initialPlace, @NonNull Type type,
      @NonNull JsonSerializationContext jsonSerializationContext) {
    JsonArray ja = new JsonArray();
    ja.add(jsonSerializationContext.serialize(initialPlace.tile, ITile.class));
    ja.add(jsonSerializationContext.serialize(initialPlace.token.color, ColorString.class));

    BoardLocation boardLocation = initialPlace.boardLocation;
    ja.add(jsonSerializationContext.serialize(boardLocation.location, Location.class));
    ja.add(boardLocation.x);
    ja.add(boardLocation.y);

    return ja;
  }
}
