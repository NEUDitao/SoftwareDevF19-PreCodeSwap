package com.tsuro;

import com.tsuro.action.IAction;
import com.tsuro.action.InitialAction;
import com.tsuro.action.IntermediateAction;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.ColorString;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroBoard;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.rulechecker.TsuroRuleChecker;
import com.tsuro.tile.ITile;
import com.tsuro.tile.Location;
import com.tsuro.tile.Path;
import com.tsuro.tile.TsuroTile;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class TsuroTestHelper {

  public static final Token BLACK_TOKEN = new Token(ColorString.BLACK);
  public static final Token WHITE_TOKEN = new Token(ColorString.WHITE);
  public static final Token RED_TOKEN = new Token(ColorString.RED);
  public static final Token GREEN_TOKEN = new Token(ColorString.GREEN);
  public static final Token BLUE_TOKEN = new Token(ColorString.BLUE);

  public static final Path p1 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  public static final Path p2 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  public static final Path p3 = new Path(Location.NORTHWEST, Location.WESTSOUTH);
  public static final Path p4 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  public static final ITile t1 = new TsuroTile(p1, p2, p3, p4);

  public static final Path p5 = new Path(Location.NORTHWEST, Location.EASTNORTH);
  public static final Path p6 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  public static final Path p7 = new Path(Location.EASTSOUTH, Location.WESTSOUTH);
  public static final Path p8 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  public static final ITile t2 = new TsuroTile(p5, p6, p7, p8);

  public static final Path p9 = new Path(Location.NORTHWEST, Location.NORTHEAST);
  public static final Path p10 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  public static final Path p11 = new Path(Location.SOUTHEAST, Location.SOUTHWEST);
  public static final Path p12 = new Path(Location.WESTNORTH, Location.WESTSOUTH);
  public static final ITile loopy = new TsuroTile(p9, p10, p11, p12);

  public static final ITile cornering = new TsuroTile(
      new Path(Location.NORTHEAST, Location.EASTNORTH),
      new Path(Location.NORTHWEST, Location.WESTNORTH),
      new Path(Location.SOUTHWEST, Location.WESTSOUTH),
      new Path(Location.SOUTHEAST, Location.EASTSOUTH));


  public static final IRuleChecker tsuroRuleChecker = new TsuroRuleChecker();

  public static Map<Point, ITile> m1;
  public static Map<Token, BoardLocation> m2;

  public static IBoard b1;

  public static IAction a1 = new InitialAction(t1, new BoardLocation(Location.EASTNORTH, 0, 0));
  public static IAction a2 = new InitialAction(t2, new BoardLocation(Location.WESTNORTH, 2, 0));
  public static IAction a3 = new InitialAction(loopy, new BoardLocation(Location.SOUTHEAST, 1, 0));
  public static IAction a4 = new InitialAction(loopy, new BoardLocation(Location.NORTHWEST, 9, 9));
  public static IAction a5 = new IntermediateAction(t1);

  public static void setUpBoard() {

    m1 = new HashMap<>();
    m2 = new HashMap<>();

    m1.put(new Point(0, 0), t1);
    m1.put(new Point(2, 0), t2);
    m1.put(new Point(9, 9), loopy);

    m2.put(BLACK_TOKEN, new BoardLocation(Location.EASTNORTH, 0, 0));
    m2.put(WHITE_TOKEN, new BoardLocation(Location.WESTNORTH, 2, 0));
    m2.put(RED_TOKEN, new BoardLocation(Location.NORTHWEST, 9, 9));

    // This calls fromIntermediatePlacements internally
    b1 = TsuroBoard.fromInitialPlacements(m1, m2);
  }
}
