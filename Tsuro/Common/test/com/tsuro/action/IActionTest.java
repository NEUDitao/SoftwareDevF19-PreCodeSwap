package com.tsuro.action;

import static com.tsuro.TsuroTestHelper.BLACK_TOKEN;
import static com.tsuro.TsuroTestHelper.GREEN_TOKEN;
import static com.tsuro.TsuroTestHelper.RED_TOKEN;
import static com.tsuro.TsuroTestHelper.WHITE_TOKEN;
import static com.tsuro.TsuroTestHelper.a1;
import static com.tsuro.TsuroTestHelper.a2;
import static com.tsuro.TsuroTestHelper.a3;
import static com.tsuro.TsuroTestHelper.a4;
import static com.tsuro.TsuroTestHelper.a5;
import static com.tsuro.TsuroTestHelper.b1;
import static com.tsuro.TsuroTestHelper.loopy;
import static com.tsuro.TsuroTestHelper.setUpBoard;
import static com.tsuro.TsuroTestHelper.t1;
import static com.tsuro.TsuroTestHelper.t2;
import static com.tsuro.TsuroTestHelper.tsuroRuleChecker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tsuro.board.IBoard;
import com.tsuro.board.TsuroBoard;
import java.awt.Point;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IActionTest {

  @BeforeEach
  void setUpTest() {
    setUpBoard();
  }

  @Test
  void doActionIfValid() {
    IBoard boardInTest = new TsuroBoard();

    boardInTest = a1
        .doActionIfValid(tsuroRuleChecker, boardInTest, BLACK_TOKEN, Arrays.asList(loopy, t1, t2))
        .get();

    assertFalse(a2.doActionIfValid(tsuroRuleChecker, boardInTest, WHITE_TOKEN,
        Arrays.asList(loopy, loopy, loopy)).isPresent());

    boardInTest = a2
        .doActionIfValid(tsuroRuleChecker, boardInTest, WHITE_TOKEN, Arrays.asList(t1, t2, loopy))
        .get();
    assertFalse(
        a3.doActionIfValid(tsuroRuleChecker, boardInTest, GREEN_TOKEN, Arrays.asList(t1, t2, loopy))
            .isPresent());
    boardInTest = a4
        .doActionIfValid(tsuroRuleChecker, boardInTest, RED_TOKEN, Arrays.asList(t1, t2, loopy))
        .get();

    assertEquals(boardInTest.getAllTokens(), b1.getAllTokens());
    assertEquals(boardInTest.getTileAt(0, 0), b1.getTileAt(0, 0));
    assertEquals(boardInTest.getTileAt(2, 0), b1.getTileAt(2, 0));
    assertEquals(boardInTest.getTileAt(9, 9), b1.getTileAt(9, 9));
    assertEquals(boardInTest.getLocationOf(WHITE_TOKEN), b1.getLocationOf(WHITE_TOKEN));
    assertEquals(boardInTest.getLocationOf(BLACK_TOKEN), b1.getLocationOf(BLACK_TOKEN));
    assertEquals(boardInTest.getLocationOf(RED_TOKEN), b1.getLocationOf(RED_TOKEN));

    assertEquals(
        a5.doActionIfValid(tsuroRuleChecker, boardInTest, BLACK_TOKEN, Arrays.asList(t1, loopy))
            .get(),
        boardInTest.placeTileOnBehalfOfPlayer(t1, BLACK_TOKEN));
  }

  @Test
  void isInitialMove() {
    assertTrue(a1.isInitialMove());
    assertFalse(a5.isInitialMove());
  }

  @Test
  void getLocationOnBoard() {
    assertEquals(new Point(0, 0), a1.getLocationOnBoard().get());
    assertFalse(a5.getLocationOnBoard().isPresent());
  }
}