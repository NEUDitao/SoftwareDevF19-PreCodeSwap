package com.tsuro.tile.tiletypes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonStreamParser;
import com.tsuro.tile.ITile;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 * Creates a list of all possible tiles, and converts tile-pats into {@link ITile}s
 */
public class TileTypes implements JsonSerializer<ITile>, JsonDeserializer<ITile> {

  private static final String INDEX_JSON_NAME = "tsuro-tiles-index.json";

  /**
   * Creates a TileTypes containing the List of TileIndexes from the default tile index mapping
   */
  public static TileTypes createTileTypes() {
    InputStream resource = TileTypes.class.getResourceAsStream(INDEX_JSON_NAME);
    JsonStreamParser p = new JsonStreamParser(new InputStreamReader(resource));
    return createTileTypes(p);
  }

  /**
   * Creates a TileTypes containing the List of TileIndexes from the given JsonStreamParser
   */
  private static TileTypes createTileTypes(JsonStreamParser tileTypesStream) {
    Gson g = new Gson();
    List<TileIndex> ti = new LinkedList<>();
    tileTypesStream.forEachRemaining(jel -> ti.add(g.fromJson(jel, TileIndex.class)));
    return new TileTypes(ti);
  }


  private final List<ITile> tiles;

  /**
   * Creates all of the tiles from the indices given and stores it in this.
   */
  private TileTypes(List<TileIndex> indices) {

    tiles = indices.stream().sorted().map(TileIndex::turnToTile).collect(Collectors.toList());

  }

  /**
   * Gets the tile at the given index rotated by the given degrees.
   *
   * @param index    the index of the tile from the tile-index list found
   *                 <a href="https://ccs.neu.edu/home/matthias/4500-f19/tsuro-tiles-index.json">here</a>
   * @param rotation The degrees to rotate by *INVARIANT*: MUST BE NON-NEGATIVE AND DIVISBLE BY 90
   * @return The tile at the index rotate by the given degrees.
   */
  public ITile getTileAtIndexWithRotation(int index, int rotation) {

    if (rotation < 0 || rotation % 90 != 0) {
      throw new IllegalArgumentException("Rotation cannot be done other than in right angles");
    }

    ITile returnable = tiles.get(index);

    int idx = rotation;
    while (idx > 0) {
      returnable = returnable.rotate();
      idx -= 90;
    }

    return returnable;
  }

  /**
   * Turns the given Tile into a TilePat.
   */
  private TilePat turnTileIntoTilePat(ITile tile) {

    ITile theTile;
    for (int i = 0; i < tiles.size(); i++) {
      if (tile.equals(tiles.get(i))) {

        theTile = tiles.get(i);
        for (int j = 0; j < 4; j++) {
          if (theTile.strictEqual(tile)) {
            return new TilePat(i, j * 90);
          } else {
            theTile = theTile.rotate();
          }
        }
      }
    }

    throw new IllegalArgumentException("Given tile doesn't exist?");
  }


  @Override
  public ITile deserialize(@NonNull JsonElement jsonElement, @NonNull Type type,
      @NonNull JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

    if (jsonElement.isJsonArray()) {
      JsonArray ja = jsonElement.getAsJsonArray();
      int tileIndex = ja.get(0).getAsInt();
      int degree = ja.get(1).getAsInt();
      return this.getTileAtIndexWithRotation(tileIndex, degree);
    } else {
      throw new JsonParseException("Not given an array");
    }

  }

  @Override
  public JsonElement serialize(@NonNull ITile tsuroTile, @NonNull Type type,
      @NonNull JsonSerializationContext jsonSerializationContext) {

    TilePat tilePat = this.turnTileIntoTilePat(tsuroTile);
    JsonArray ja = new JsonArray();
    ja.add(tilePat.index);
    ja.add(tilePat.degree);
    return ja;

  }
}
