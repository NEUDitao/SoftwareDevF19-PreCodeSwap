package com.tsuro.tile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonStreamParser;
import com.tsuro.tile.tiletypes.TileTypes;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class XTiles {

  /**
   * Contains elements used when querying TileTypes for a Tile's connection on a port.
   */
  private static class XTilesQuery {

    public final int index;
    public final int degrees;
    public final Location port;

    /**
     * Creates a XTileQuery to get Tile with given index from TileTypes, rotating it by degrees, and
     * getting the connection off of the port given.
     */
    public XTilesQuery(int index, int degrees, Location port) {
      this.index = index;
      this.degrees = degrees;
      this.port = port;
    }
  }

  /**
   * Takes a JsonElement and turns it into an XTilesQuery.
   */
  private static class XTilesQueryAdapter implements JsonDeserializer<XTilesQuery> {

    @Override
    public XTilesQuery deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      if (jsonElement.isJsonArray()) {
        JsonArray ja = jsonElement.getAsJsonArray();
        int index = ja.get(0).getAsInt();
        int degrees = ja.get(1).getAsInt();
        Location port = jsonDeserializationContext.deserialize(ja.get(2), Location.class);

        return new XTilesQuery(index, degrees, port);
      } else {
        throw new JsonParseException("Not an array");
      }
    }
  }

  public static void main(String[] args) {

    TileTypes allTypes = TileTypes.createTileTypes();

    JsonStreamParser parse = new JsonStreamParser(new InputStreamReader(System.in));

    while (parse.hasNext()) {
      processOneTile(parse.next(), allTypes);
    }
  }

  /**
   * Processes a single Tile based off its index gotten from allTypes
   */
  private static void processOneTile(JsonElement element, TileTypes allTypes) {

    GsonBuilder gb = new GsonBuilder();
    gb.registerTypeAdapter(XTilesQuery.class, new XTilesQueryAdapter());
    Gson g = gb.create();

    XTilesQuery xtq = g.fromJson(element, XTilesQuery.class);
    Tile tile = allTypes.getTileAtIndexWithRotation(xtq.index, xtq.degrees);
    Location loc = tile.internalConnection(xtq.port);

    JsonArray returnable = new JsonArray();
    returnable.add("if ");
    returnable.add(g.toJsonTree(xtq.port));
    returnable.add(" is the entrance, ");
    returnable.add(g.toJsonTree(loc));
    returnable.add(" is the exit.");
    System.out.println(returnable);

  }

}
