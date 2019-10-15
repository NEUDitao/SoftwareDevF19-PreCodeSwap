package com.tsuro.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import com.tsuro.action.actionpat.ActionPat;
import com.tsuro.action.actionpat.ActionPatDeserializer;
import com.tsuro.board.statepat.IStatePat;
import com.tsuro.board.statepat.InitialPlace;
import com.tsuro.board.statepat.InitialPlaceSerializer;
import com.tsuro.board.statepat.StatePatDeserializer;
import com.tsuro.tile.ITile;
import com.tsuro.tile.tiletypes.TileTypes;
import java.awt.Point;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;

/**
 * Tests board by serializing StatePats and turning them into actions performed on a {@link IBoard}
 */
public class XBoard {

  /**
   * Runs the program by reading System.in and processing it.
   */
  public static void main(@NonNull String[] args) {
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
    gb.registerTypeAdapter(IStatePat.class, new StatePatDeserializer());
    gb.registerTypeAdapter(InitialPlace.class, new InitialPlaceSerializer());
    Gson g = gb.create();

    JsonElement first = jsp.next();
    List<IStatePat> statePats = getStatePats(g, first);
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
   * Turns Json state-pats into a list of {@link IStatePat}s
   */
  private static List<IStatePat> getStatePats(Gson g, JsonElement el) {
    return g.fromJson(el, TypeToken.getParameterized(List.class, IStatePat.class).getType());
  }

  /**
   * Turns a List of {@link IStatePat}s into a {@link IBoard}
   */
  private static IBoard statePatsToBoard(List<IStatePat> statePats) {
    Map<Point, ITile> tiles = new HashMap<>();
    Map<IToken, BoardLocation> playerLocs = new HashMap<>();
    statePats.forEach(statePat -> statePat.addToIntermediatePlacementMaps(tiles, playerLocs));
    return TsuroBoard.fromIntermediatePlacements(tiles, playerLocs);
  }
}
