package com.tsuro.board;


import com.tsuro.tile.Tile;
import java.awt.Dimension;
import java.util.Set;

public interface Board {

  void placeFirstTile(Tile tile, Token token, BoardLocation loc);

  void placeTileOnBehalfOfPlayer(Tile tile, Token token);

  Tile getTileAt(int x, int y);

  BoardLocation getLocationOf(Token token);

  Set<Token> getAllTokens();

  Dimension getSize();

}
