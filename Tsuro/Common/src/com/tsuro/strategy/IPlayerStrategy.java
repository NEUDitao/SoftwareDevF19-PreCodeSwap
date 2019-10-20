package com.tsuro.strategy;

import com.tsuro.action.IAction;
import com.tsuro.board.IBoard;
import com.tsuro.board.Token;
import com.tsuro.rulechecker.IRuleChecker;
import com.tsuro.tile.ITile;
import java.util.List;

public interface IPlayerStrategy {

  IAction strategizeInitMove(List<ITile> hand, Token avatar, IBoard board, IRuleChecker checker);

  IAction strategizeIntermediateMove(List<ITile> hand, Token avatar, IBoard board,
      IRuleChecker checker);


}
