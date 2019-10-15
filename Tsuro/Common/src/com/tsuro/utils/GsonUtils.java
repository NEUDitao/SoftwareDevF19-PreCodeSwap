package com.tsuro.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsuro.action.actionpat.ActionPat;
import com.tsuro.action.actionpat.ActionPatDeserializer;
import com.tsuro.board.statepat.IStatePat;
import com.tsuro.board.statepat.InitialPlace;
import com.tsuro.board.statepat.InitialPlaceSerializer;
import com.tsuro.board.statepat.StatePatDeserializer;
import com.tsuro.observer.TurnPat;
import com.tsuro.observer.TurnPatDeserializer;
import com.tsuro.tile.ITile;
import com.tsuro.tile.tiletypes.TileTypes;

public class GsonUtils {

  public static Gson getTsuroGson() {
    GsonBuilder gb = new GsonBuilder();
    TileTypes allTypes = TileTypes.createTileTypes();
    gb.registerTypeAdapter(ITile.class, allTypes);
    gb.registerTypeAdapter(ActionPat.class, new ActionPatDeserializer());
    gb.registerTypeAdapter(IStatePat.class, new StatePatDeserializer());
    gb.registerTypeAdapter(InitialPlace.class, new InitialPlaceSerializer());
    gb.registerTypeAdapter(TurnPat.class, new TurnPatDeserializer());
    return gb.create();
  }

}
