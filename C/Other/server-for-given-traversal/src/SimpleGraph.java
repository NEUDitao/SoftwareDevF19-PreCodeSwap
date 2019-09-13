
import java.util.LinkedList;
import java.util.List;

/**
 * A Graph whose Nodes each connect to at most one other Node.
 */
public class SimpleGraph extends Graph implements Labyrinth {

  private SimpleGraph(List<Node> nodes) {
    this.nodes = nodes;
    this.edges = new LinkedList<>();
  }

  @Override
  public Labyrinth createLabyrinth(List<Node> nodes) {
    return new SimpleGraph(nodes);
  }

  @Override
  public Labyrinth addToken(Token token, Node node) {
    node.token = token;
    return this;
  }

  @Override
  public boolean canReach(Token token, Node goal) {
    return canGetToken(goal, token);
  }

  /**
   * Determines if some colored token can reach some named Node by following the Edges within a
   * particular Labyrinth
   * @param goal the node trying to be found
   * @param token the token queried
   * @return if the token can reach the node
   */
  private boolean canGetToken(Node goal, Token token) {

    Node currentNode = null;

    for (var theNode : nodes) {
      if (theNode.token == token) {
        currentNode = theNode;
        break;
      }

    }

    for (var i = 0; i <= nodes.size(); i++) {
      if (currentNode == goal) {
        break;
      }

      for (var e : edges) {
        if (e.from == currentNode) {
          if (e.to != null) {
            currentNode = e.to;
          }
        }
      }
    }

    return currentNode == goal;

  }

  ;
}
