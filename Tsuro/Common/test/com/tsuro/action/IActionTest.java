package com.tsuro.action;

import static com.tsuro.TsuroTestHelper.BLACK_TOKEN;
import static com.tsuro.TsuroTestHelper.BLUE_TOKEN;
import static com.tsuro.TsuroTestHelper.GREEN_TOKEN;
import static com.tsuro.TsuroTestHelper.RED_TOKEN;
import static com.tsuro.TsuroTestHelper.WHITE_TOKEN;
import static com.tsuro.TsuroTestHelper.a1;
import static com.tsuro.TsuroTestHelper.a2;
import static com.tsuro.TsuroTestHelper.a3;
import static com.tsuro.TsuroTestHelper.a4;
import static com.tsuro.TsuroTestHelper.a5;
import static com.tsuro.TsuroTestHelper.loopy;
import static com.tsuro.TsuroTestHelper.setUpBoard;
import static com.tsuro.TsuroTestHelper.t1;
import static com.tsuro.TsuroTestHelper.t2;
import static com.tsuro.TsuroTestHelper.tsuroRuleChecker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.board.TsuroBoard;
import com.tsuro.rulechecker.HackerIdealRuleChecker;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.awt.Point;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    // makes sure token returned is one on hand
    assertFalse(a2.doActionIfValid(tsuroRuleChecker, boardInTest, WHITE_TOKEN,
        Arrays.asList(loopy, loopy, loopy)).isPresent());

    boardInTest = a2
        .doActionIfValid(tsuroRuleChecker, boardInTest, WHITE_TOKEN, Arrays.asList(t1, t2, loopy))
        .get();

    // makes sure you can't place touching other tiles
    assertFalse(
        a3.doActionIfValid(tsuroRuleChecker, boardInTest, GREEN_TOKEN, Arrays.asList(t1, t2, loopy))
            .isPresent());

    boardInTest = a4
        .doActionIfValid(tsuroRuleChecker, boardInTest, RED_TOKEN, Arrays.asList(t1, t2, loopy))
        .get();

    // anything rulechecker says is valid returns some Board
    assertTrue(a3.doActionIfValid(new HackerIdealRuleChecker(), boardInTest, GREEN_TOKEN,
        Arrays.asList(t1, t1, t1, t1, t1, t1, t1, t1, t1)).isPresent());

    assertEquals(
        a5.doActionIfValid(tsuroRuleChecker, boardInTest, BLACK_TOKEN, Arrays.asList(t1, loopy))
            .get(),
        boardInTest.placeTileOnBehalfOfPlayer(t1, BLACK_TOKEN));

    assertFalse(a5.doActionIfValid(tsuroRuleChecker, boardInTest, BLUE_TOKEN, Arrays.asList(t1, t1))
        .isPresent());
  }

  @Test
  void testActionFunc() {

    // Testing flow of data to show Action itself is not placing things
    IRuleChecker mockChecker = Mockito.mock(IRuleChecker.class);
    IBoard mockBoard = Mockito.mock(IBoard.class);
    IBoard mockBoard2 = Mockito.mock(IBoard.class);

    Mockito.when(mockChecker.isValidIntermediateMove(any(IBoard.class), any(ITile.class), any(
        Token.class), Mockito.anyCollection())).thenReturn(true);
    Mockito.when(mockBoard.placeTileOnBehalfOfPlayer(any(ITile.class), any(Token.class)))
        .thenReturn(mockBoard2);

    assertEquals(mockBoard2,
        a5.doActionIfValid(mockChecker, mockBoard, RED_TOKEN, Arrays.asList(t1, t1)).get());


  }

  @Test
  void isInitialMove() {
    assertTrue(a1.isInitialMove());
    assertFalse(a5.isInitialMove());
  }

}