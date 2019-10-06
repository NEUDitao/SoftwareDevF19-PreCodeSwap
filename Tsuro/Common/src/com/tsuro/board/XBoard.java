package com.tsuro.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import com.tsuro.tile.Location;
import com.tsuro.tile.Tile;
import com.tsuro.tile.tiletypes.TileTypes;
import java.awt.Point;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class XBoard {

  private interface StatePat {

    void addToIntermediatePlacementMaps(Map<Point, Tile> tiles,
        Map<Token, BoardLocation> playerLocs);
  }

  private static class InitialPlace implements StatePat {

    private final Tile tile;
    private final Token token;
    private final BoardLocation location;

    public InitialPlace(Tile t, Token cs, BoardLocation location) {
      tile = t;
      token = cs;
      this.location = location;
    }

    @Override
    public void addToIntermediatePlacementMaps(Map<Point, Tile> tiles,
        Map<Token, BoardLocation> playerLocs) {
      tiles.put(new Point(location.x, location.y), this.tile);
      playerLocs.put(token, this.location);
    }
  }

  private static class IntermediatePlace implements StatePat {

    private final Tile tile;
    private final Point location;

    public IntermediatePlace(Tile tile, Point location) {
      this.tile = tile;
      this.location = location;
    }

    @Override
    public void addToIntermediatePlacementMaps(Map<Point, Tile> tiles,
        Map<Token, BoardLocation> playerLocs) {
      tiles.put(new Point(location), tile);
    }
  }

  private static class StatePatDeserializer implements JsonDeserializer<StatePat> {

    @Override
    public StatePat deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      if (jsonElement.isJsonArray()) {
        JsonArray ja = jsonElement.getAsJsonArray();
        if (ja.size() == 5) {
          Tile t = jsonDeserializationContext.deserialize(ja.get(0), Tile.class);
          ColorString cs = jsonDeserializationContext.deserialize(ja.get(1), ColorString.class);
          Location loc = jsonDeserializationContext.deserialize(ja.get(2), Location.class);
          BoardLocation bl = new BoardLocation(loc, ja.get(3).getAsInt(), ja.get(4).getAsInt());
          return new InitialPlace(t, new ColoredToken(cs), bl);
        } else if (ja.size() == 3) {
          Tile t = jsonDeserializationContext.deserialize(ja.get(0), Tile.class);
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

  private static class ActionPat {

    private final Token token;
    private final Tile tile;

    public ActionPat(Token token, Tile tile) {
      this.token = token;
      this.tile = tile;
    }
  }

  private static class ActionPatDeserializer implements JsonDeserializer<ActionPat> {

    @Override
    public ActionPat deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      if (jsonElement.isJsonArray()) {
        JsonArray ja = jsonElement.getAsJsonArray();
        if (ja.size() == 2) {
          Token token = new ColoredToken(
              jsonDeserializationContext.deserialize(ja.get(0), ColorString.class));
          Tile tile = jsonDeserializationContext.deserialize(ja.get(1), Tile.class);
          return new ActionPat(token, tile);
        }
      }
      throw new JsonParseException("Not an action-pat");
    }
  }

  public static void main(String[] args) {
    GsonBuilder gb = new GsonBuilder();
    TileTypes allTypes = TileTypes.createTileTypes();
    gb.registerTypeAdapter(Tile.class, allTypes);
    gb.registerTypeAdapter(ActionPat.class, new ActionPatDeserializer());
    gb.registerTypeAdapter(StatePat.class, new StatePatDeserializer());
    Gson g = gb.create();

    JsonStreamParser jsp = new JsonStreamParser(new InputStreamReader(System.in))
  }

  private static List<StatePat> getStatePats(Gson g, JsonElement el) {
    return g.fromJson(el, TypeToken.getParameterized(List.class, StatePat.class).getType());
  }
}
