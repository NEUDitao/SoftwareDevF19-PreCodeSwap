package com.tsuro;

import com.tsuro.board.Board;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.ColorString;
import com.tsuro.board.ColoredToken;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroBoard;
import com.tsuro.rulechecker.RuleChecker;
import com.tsuro.rulechecker.TsuroRuleChecker;
import com.tsuro.tile.Location;
import com.tsuro.tile.Path;
import com.tsuro.tile.Tile;
import com.tsuro.tile.TsuroTile;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class TsuroTestHelper {

  public static final ColoredToken BLACK_TOKEN = new ColoredToken(ColorString.BLACK);
  public static final ColoredToken WHITE_TOKEN = new ColoredToken(ColorString.WHITE);
  public static final ColoredToken RED_TOKEN = new ColoredToken(ColorString.RED);
  public static final ColoredToken GREEN_TOKEN = new ColoredToken(ColorString.GREEN);
  public static final ColoredToken BLUE_TOKEN = new ColoredToken(ColorString.BLUE);

  public static final Path p1 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  public static final Path p2 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  public static final Path p3 = new Path(Location.NORTHWEST, Location.WESTSOUTH);
  public static final Path p4 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  public static final Tile t1 = new TsuroTile(p1, p2, p3, p4);

  public static final Path p5 = new Path(Location.NORTHWEST, Location.EASTNORTH);
  public static final Path p6 = new Path(Location.NORTHEAST, Location.SOUTHEAST);
  public static final Path p7 = new Path(Location.EASTSOUTH, Location.WESTSOUTH);
  public static final Path p8 = new Path(Location.WESTNORTH, Location.SOUTHWEST);
  public static final Tile t2 = new TsuroTile(p5, p6, p7, p8);

  public static final Path p9 = new Path(Location.NORTHWEST, Location.NORTHEAST);
  public static final Path p10 = new Path(Location.EASTNORTH, Location.EASTSOUTH);
  public static final Path p11 = new Path(Location.SOUTHEAST, Location.SOUTHWEST);
  public static final Path p12 = new Path(Location.WESTNORTH, Location.WESTSOUTH);
  public static final Tile loopy = new TsuroTile(p9, p10, p11, p12);

  public static final Tile cornering = new TsuroTile(
      new Path(Location.NORTHEAST, Location.EASTNORTH),
      new Path(Location.NORTHWEST, Location.WESTNORTH),
      new Path(Location.SOUTHWEST, Location.WESTSOUTH),
      new Path(Location.SOUTHEAST, Location.EASTSOUTH));


  public static final RuleChecker tsuroRuleChecker = new TsuroRuleChecker();

  public static Map<Point, Tile> m1;
  public static Map<Token, BoardLocation> m2;

  public static Board b1;

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
