package com.tsuro.rulechecker;


import static com.tsuro.TsuroTestHelper.BLACK_TOKEN;
import static com.tsuro.TsuroTestHelper.BLUE_TOKEN;
import static com.tsuro.TsuroTestHelper.WHITE_TOKEN;
import static com.tsuro.TsuroTestHelper.b1;
import static com.tsuro.TsuroTestHelper.cornering;
import static com.tsuro.TsuroTestHelper.loopy;
import static com.tsuro.TsuroTestHelper.t1;
import static com.tsuro.TsuroTestHelper.t2;
import static com.tsuro.TsuroTestHelper.tsuroRuleChecker;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tsuro.TsuroTestHelper;
import com.tsuro.board.BoardLocation;
import com.tsuro.tile.Location;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TsuroRuleCheckerTest {


  @BeforeEach
  void setUp() {
    TsuroTestHelper.setUpBoard();
  }

  @Test
  void isValidInitialMove() {
    // move is valid
    assertTrue(tsuroRuleChecker.isValidInitialMove(b1, loopy, BLUE_TOKEN,
        new BoardLocation(Location.NORTHEAST, 0, 8), Arrays.asList(loopy, t1)));

    // move off the board
    assertFalse(tsuroRuleChecker
        .isValidInitialMove(b1, loopy, BLUE_TOKEN, new BoardLocation(Location.WESTNORTH, -1, 0),
            Arrays.asList(loopy, t1)));

    // initial move not on edge
    assertFalse(tsuroRuleChecker
        .isValidInitialMove(b1, loopy, BLUE_TOKEN, new BoardLocation(Location.NORTHWEST, 5, 5),
            Arrays.asList(loopy, t1)));

    // initial move on another tile
    assertFalse(tsuroRuleChecker
        .isValidInitialMove(b1, loopy, BLUE_TOKEN, new BoardLocation(Location.SOUTHWEST, 0, 0),
            Arrays.asList(loopy, t1)));

    // touching someone else
    assertFalse(tsuroRuleChecker
        .isValidInitialMove(b1, loopy, BLUE_TOKEN, new BoardLocation(Location.EASTNORTH, 8, 9),
            Arrays.asList(loopy, t1)));

    // BLACK has already been placed
    assertFalse(tsuroRuleChecker
        .isValidInitialMove(b1, loopy, BLACK_TOKEN, new BoardLocation(Location.SOUTHEAST, 5, 0),
            Arrays.asList(loopy, t1)));

    // suicide
    assertFalse(tsuroRuleChecker
        .isValidInitialMove(b1, loopy, BLUE_TOKEN, new BoardLocation(Location.WESTNORTH, 0, 5),
            Arrays.asList(t1, loopy)));

    // doesn't contain tile in deck
    assertFalse(tsuroRuleChecker.isValidInitialMove(b1, loopy, BLUE_TOKEN,
        new BoardLocation(Location.NORTHEAST, 0, 8), Arrays.asList(t2, t1)));
  }

  @Test
  void isValidIntermediateMove() {
    // normal move
    assertTrue(tsuroRuleChecker
        .isValidIntermediateMove(b1, t1.rotate().rotate().rotate(), BLACK_TOKEN,
            Arrays.asList(t1, loopy)));

    // doesn't contain tile in deck
    assertFalse(
        tsuroRuleChecker.isValidIntermediateMove(b1, loopy, BLUE_TOKEN, Arrays.asList(t2, t1)));

    // suicide
    assertFalse(
        tsuroRuleChecker.isValidIntermediateMove(b1, t2, WHITE_TOKEN, Arrays.asList(t2, loopy)));
    // create loop
    assertFalse(
        tsuroRuleChecker.isValidIntermediateMove(b1, loopy, WHITE_TOKEN, Arrays.asList(t2, loopy)));
  }

  @Test
  void isValidIntermediateMoveOnlyLoopy() {
    // If making a loop is the only valid choice

    assertFalse(tsuroRuleChecker
        .isValidIntermediateMove(b1, loopy.rotate(), BLACK_TOKEN, Arrays.asList(t1, loopy)));
    assertTrue(tsuroRuleChecker
        .isValidIntermediateMove(b1, loopy.rotate(), BLACK_TOKEN, Arrays.asList(loopy, loopy)));
  }

  @Test
  void isValidIntermediateMoveOnlySuicide() {
    assertFalse(tsuroRuleChecker
        .isValidIntermediateMove(b1, cornering, BLACK_TOKEN, Arrays.asList(t1, cornering)));
    assertTrue(tsuroRuleChecker.isValidIntermediateMove(b1, cornering, BLACK_TOKEN,
        Arrays.asList(cornering.rotate(), cornering)));
    assertTrue(tsuroRuleChecker
        .isValidIntermediateMove(b1, cornering, BLACK_TOKEN, Arrays.asList(loopy, cornering)));


  }
}