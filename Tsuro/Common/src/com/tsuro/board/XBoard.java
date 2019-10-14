package com.tsuro.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import com.tsuro.tile.Location;
import com.tsuro.tile.ITile;
import com.tsuro.tile.tiletypes.TileTypes;
import java.awt.Point;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests board by serializing StatePats and turning them into actions performed on a {@link IBoard}
 */
public class XBoard {

  /**
   * An interface to represent state-pats as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._state._pat%29
   */
  private interface StatePat {

    /**
     * Adds {@code this} StatePat to the maps used in placeIntermediateTiles.
     */
    void addToIntermediatePlacementMaps(Map<Point, ITile> tiles,
        Map<IToken, BoardLocation> playerLocs);
  }

  /**
   * Represents an initial-place as defined in http://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._initial._place%29
   */
  private static class InitialPlace implements StatePat {

    public final ITile tile;
    public final ColoredIToken token;
    public final BoardLocation boardLocation;

    /**
     * Creates an InitialPlace which has a Tile, the token that placed it, and the BoardLocation the
     * Tile will be placed at.
     */
    public InitialPlace(ITile t, ColoredIToken cs, BoardLocation boardLocation) {
      tile = t;
      token = cs;
      this.boardLocation = boardLocation;
    }

    @Override
    public void addToIntermediatePlacementMaps(Map<Point, ITile> tiles,
        Map<IToken, BoardLocation> playerLocs) {
      tiles.put(new Point(boardLocation.x, boardLocation.y), this.tile);
      playerLocs.put(token, this.boardLocation);
    }
  }

  /**
   * Serializes an initial-place as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._initial._place%29
   * into an {@code InitialPlace}.
   */
  private static class InitialPlaceSerializer implements JsonSerializer<InitialPlace> {

    @Override
    public JsonElement serialize(InitialPlace initialPlace, Type type,
        JsonSerializationContext jsonSerializationContext) {
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

  /**
   * Creates an IntermediatePlace which has a Tile, and the location it'll be placed.
   */
  private static class IntermediatePlace implements StatePat {

    private final ITile tile;
    private final Point location;

    /**
     * Constructs an IntermediatePlace with the tile and point taken.
     */
    public IntermediatePlace(ITile tile, Point location) {
      this.tile = tile;
      this.location = location;
    }

    @Override
    public void addToIntermediatePlacementMaps(Map<Point, ITile> tiles,
        Map<IToken, BoardLocation> playerLocs) {
      tiles.put(new Point(location), tile);
    }
  }

  /**
   * Serializes an intermediate-place as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._intermediate._place%29
   * into an {@code IntermediatePlace}.
   */
  private static class StatePatDeserializer implements JsonDeserializer<StatePat> {

    @Override
    public StatePat deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      if (jsonElement.isJsonArray()) {
        JsonArray ja = jsonElement.getAsJsonArray();
        if (ja.size() == 5) {
          ITile t = jsonDeserializationContext.deserialize(ja.get(0), ITile.class);
          ColorString cs = jsonDeserializationContext.deserialize(ja.get(1), ColorString.class);
          Location loc = jsonDeserializationContext.deserialize(ja.get(2), Location.class);
          BoardLocation bl = new BoardLocation(loc, ja.get(3).getAsInt(), ja.get(4).getAsInt());
          return new InitialPlace(t, new ColoredIToken(cs), bl);
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

  /**
   * Represents an ActionPAt which has a Token and the Tile that it places.
   */
  private static class ActionPat {

    private final IToken token;
    private final ITile tile;

    /**
     * Constructor for an ActionPat with the token and the tile it will place.
     */
    public ActionPat(IToken token, ITile tile) {
      this.token = token;
      this.tile = tile;
    }
  }

  /**
   * Deserializes an action-pat as defined https://ccs.neu.edu/home/matthias/4500-f19/4.html#%28tech._action._pat%29
   * into an {@link ActionPat}
   */
  private static class ActionPatDeserializer implements JsonDeserializer<ActionPat> {

    @Override
    public ActionPat deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      if (jsonElement.isJsonArray()) {
        JsonArray ja = jsonElement.getAsJsonArray();
        if (ja.size() == 2) {
          IToken token = new ColoredIToken(
              jsonDeserializationContext.deserialize(ja.get(0), ColorString.class));
          ITile tile = jsonDeserializationContext.deserialize(ja.get(1), ITile.class);
          return new ActionPat(token, tile);
        }
      }
      throw new JsonParseException("Not an action-pat");
    }
  }

  /**
   * Runs the program by reading System.in and processing it.
   */
  public static void main(String[] args) {
    JsonStreamParser jsp = new JsonStreamParser(new InputStreamReader(System.in));
    System.out.println(doStuff(jsp));
  }

  /**
   * Processes the Json in JsonStreamParser into state-pats and returns result.
   */
  private static String doStuff(JsonStreamParser jsp) {
    GsonBuilder gb = new GsonBuilder();
    TileTypes allTypes = TileTypes.createTileTypes();
    gb.registerTypeAdapter(ITile.class, allTypes);
    gb.registerTypeAdapter(ActionPat.class, new ActionPatDeserializer());
    gb.registerTypeAdapter(StatePat.class, new StatePatDeserializer());
    gb.registerTypeAdapter(InitialPlace.class, new InitialPlaceSerializer());
    Gson g = gb.create();

    JsonElement first = jsp.next();
    List<StatePat> statePats = getStatePats(g, first);
    IBoard b = statePatsToBoard(statePats);

    final ColoredIToken red = new ColoredIToken(ColorString.RED);

    if (!b.getAllTokens().contains(red)) {
      return "\"red never played\"";
    }

    while (jsp.hasNext()) {
      ActionPat ap = g.fromJson(jsp.next(), ActionPat.class);

      b = b.placeTileOnBehalfOfPlayer(ap.tile, ap.token);

      if (b.getStatuses().contains(TsuroStatus.CONTAINS_LOOP)) {
        return "\"infinite\"";
      } else if (b.getStatuses().contains(TsuroStatus.COLLISION)) {
        return "\"collision\"";
      } else if (!b.getAllTokens().contains(red)) {
        return "\"red died\"";
      }
    }

    final BoardLocation loc = b.getLocationOf(red);
    final InitialPlace ip = new InitialPlace(b.getTileAt(loc.x, loc.y), red, loc);

    return g.toJson(ip, InitialPlace.class);

  }

  /**
   * Turns Json state-pats into a list of {@link StatePat}s
   */
  private static List<StatePat> getStatePats(Gson g, JsonElement el) {
    return g.fromJson(el, TypeToken.getParameterized(List.class, StatePat.class).getType());
  }

  /**
   * Turns a List of {@link StatePat}s into a {@link IBoard}
   */
  private static IBoard statePatsToBoard(List<StatePat> statePats) {
    Map<Point, ITile> tiles = new HashMap<>();
    Map<IToken, BoardLocation> playerLocs = new HashMap<>();
    statePats.forEach(statePat -> statePat.addToIntermediatePlacementMaps(tiles, playerLocs));
    return TsuroBoard.fromIntermediatePlacements(tiles, playerLocs);
  }
}
