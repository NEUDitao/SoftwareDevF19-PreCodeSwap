package com.tsuro.strategy;

import com.tsuro.action.InitialAction;
import com.tsuro.board.BoardLocation;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import com.tsuro.tile.Location;
import com.tsuro.utils.BoardUtils;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Abstract class that takes away common strategies among IPlayerStrategy's
 */
public abstract class AbstractStrategy implements IPlayerStrategy {

  @Override
  public InitialAction strategizeInitMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker) {

    List<Point> edgePoints = BoardUtils.getEdgePoints(board);

    Optional<InitialAction> moveLoc = edgePoints.stream()
        .flatMap(a -> Arrays.stream(Location.values())
            .map(location -> new BoardLocation(location, a.x, a.y)))
        .map(boardLocation -> new InitialAction(hand.get(2), boardLocation))
        .filter(action -> {
          Optional<IBoard> newBoard = action.doActionIfValid(checker, board, avatar, hand);
          return newBoard.isPresent();
        }).findFirst();

    return moveLoc.orElseThrow(() -> new IllegalArgumentException(
        "No legal initial moves for given ruleset using third tile."));
  }
}
