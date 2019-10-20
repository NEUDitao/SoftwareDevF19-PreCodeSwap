package com.tsuro.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tsuro.board.TsuroBoard;
import java.awt.Point;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BoardUtilsTest {

  @Test
  void strategizeInitMove() {

    assertEquals(Arrays
            .asList(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2),
                new Point(1, 2), new Point(0, 2), new Point(0, 1)),
        BoardUtils.getEdgePoints(new TsuroBoard(3, 3)));
  }
}
