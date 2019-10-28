package com.tsuro.referee.xref;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tsuro.player.IPlayer;
import com.tsuro.player.StrategyPlayer;
import com.tsuro.referee.CycleThroughTiles;
import com.tsuro.referee.Referee;
import com.tsuro.rulechecker.TsuroRuleChecker;
import com.tsuro.strategy.FirstPlayerStrategy;
import com.tsuro.tile.ITile;
import com.tsuro.tile.tiletypes.TileTypes;
import com.tsuro.utils.GsonUtils;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class XRef {

  public static void main(String[] args) {
    doStuff(System.in);
  }

  private static void doStuff(InputStream in) {

    Gson g = GsonUtils.getTsuroGson();

    List<String> inputArray = g.fromJson(new InputStreamReader(in),
        TypeToken.getParameterized(ArrayList.class, String.class).getType());

    BiMap<String, IPlayer> playerNames = HashBiMap.create();

    for (String s : inputArray) {
      playerNames.put(s, new StrategyPlayer(new FirstPlayerStrategy()));
    }

    List<IPlayer> players = new ArrayList<>();

    for (String s : inputArray) {
      players.add(playerNames.get(s));
    }

    List<ITile> allTiles = TileTypes.createTileTypes().getAllTiles();

    List<Set<IPlayer>> rankings = new Referee(new TsuroRuleChecker(), players,
        new CycleThroughTiles(allTiles)).startGame();

    JsonArray cheaters = g.toJsonTree(inputArray.stream()
        .filter(playerName -> rankings.stream()
            .noneMatch(ordinal -> ordinal.contains(playerNames.get(playerName))))
        .sorted()
        .collect(Collectors.toList())).getAsJsonArray();

    JsonArray winners = g.toJsonTree(rankings.stream().map(
        ordinal -> ordinal.stream().map(playerNames.inverse()::get).sorted()
            .collect(Collectors.toList())).collect(Collectors.toList())).getAsJsonArray();

    JsonObject retVal = new JsonObject();
    retVal.add("winners", winners);
    retVal.add("cheaters", cheaters);

    System.out.println(retVal);

    return;

  }

}
