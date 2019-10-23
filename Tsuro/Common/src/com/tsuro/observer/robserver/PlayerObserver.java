package com.tsuro.observer.robserver;

import com.tsuro.action.IAction;
import com.tsuro.board.BoardPanel;
import com.tsuro.board.IBoard;
import com.tsuro.observer.IObserver;
import com.tsuro.player.HandPanel;
import com.tsuro.player.PlayerState;
import com.tsuro.rulechecker.HackerIdealRuleChecker;
import com.tsuro.utils.RenderUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import lombok.NonNull;

/**
 * A Graphical {@link IObserver} that watches a Player and renders the move they perform.
 */
public class PlayerObserver implements IObserver<PlayerState> {


  @NonNull
  private final JFrame frame;
  @NonNull
  private final BoardPanel panel;
  @NonNull
  private final HandPanel handPanel;
  @NonNull
  private final JPanel overallPanel;

  /**
   * Instantiates defaults for a {@link JFrame} and {@link BoardPanel} for rendering the state of
   * the game that this Player is acting on
   */
  public PlayerObserver() {
    this.frame = new JFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    overallPanel = new JPanel(new GridBagLayout());
    frame.add(overallPanel);

    GridBagConstraints boardConstraints = RenderUtils.createDefaultGBC(0, 0);
    boardConstraints.weightx = 2;
    panel = new BoardPanel();
    overallPanel.add(panel, boardConstraints);

    GridBagConstraints handConstraints = RenderUtils.createDefaultGBC(1, 0);
    handPanel = new HandPanel();
    overallPanel.add(handPanel, handConstraints);
  }

  @Override
  public void update(PlayerState state) {

    @NonNull final IAction actionPat = state.action;

    Optional<IBoard> newBoard = actionPat
        .doActionIfValid(new HackerIdealRuleChecker(), state.board, state.token, state.hand);

    IBoard theNewBoard = newBoard
        .orElseThrow(() -> new IllegalStateException("Board has invalid rules"));

    List<Point> highlightedPoints = getDifferences(state.board, theNewBoard);

    panel.drawBoard(theNewBoard, highlightedPoints);

    handPanel.paintHand(state.hand);

    frame.pack();
    frame.setSize(1900, 1000);

    /*
    Color p = panel.getBackground();

    panel.setBackground(Color.BLACK);
    handPanel.setBackground(Color.BLACK);
    overallPanel.setBackground(Color.BLACK);

    BufferedImage buff = new BufferedImage(1900, 1000, BufferedImage.TYPE_INT_RGB);

    overallPanel.paint(buff.getGraphics());
    System.out.print(ImageToShellKt.imageToShell(buff, new Dimension(279, 70), new Point(0,0)));

    panel.setBackground(p);
    handPanel.setBackground(p);
    overallPanel.setBackground(p);

     */

    frame.setVisible(true);
  }

  /**
   * Returns a List of the Points that have differences between the old and new board.
   */
  private List<Point> getDifferences(IBoard oldBoard, IBoard theNewBoard) {

    List<Point> highlightedPoints = new ArrayList<>();

    for (int x = 0; x < theNewBoard.getSize().width; x++) {
      for (int y = 0; y < theNewBoard.getSize().height; y++) {
        if (!oldBoard.getTileAt(x, y).equals(theNewBoard.getTileAt(x, y))) {
          highlightedPoints.add(new Point(x, y));
        }
      }
    }

    return highlightedPoints;
  }

}

