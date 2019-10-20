package com.tsuro.utils;

import com.tsuro.board.IBoard;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardUtils {

  public static List<Point> getEdgePoints(IBoard board) {

    List<Point> returanble = new ArrayList<>();
    for (int x = 0; x < board.getSize().width; x++) {
      returanble.add(new Point(x, 0));
    }
    for (int y = 1; y < board.getSize().getHeight(); y++) {
      returanble.add(new Point(board.getSize().width - 1, y));
    }
    for (int x = board.getSize().width - 2; x >= 0; x--) {
      returanble.add(new Point(x, board.getSize().height - 1));
    }
    for (int y = board.getSize().height - 2; y > 0; y--) {
      returanble.add(new Point(0, y));
    }

    return returanble;

  }
}
