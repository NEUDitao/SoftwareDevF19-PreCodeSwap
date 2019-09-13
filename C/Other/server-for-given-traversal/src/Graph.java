import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph implements Labyrinth {

  List<Node> nodes;
  List<Edge> edges;

  private Graph(List<Node> nodes) {
    this.nodes = nodes;
    this.edges = new LinkedList<>();
  }

  @Override
  public Labyrinth createLabyrinth(List<Node> nodes) {
    return new Graph(nodes);
  }

  @Override
  public Labyrinth addToken(Token token, Node node) {
    node.token = token;
    return this;
  }

  @Override
  public boolean canReach(Token token, Node goal) {
    for (var e : edges) {

      // since each node only belongs to 1 edge, find the edge with the node, and determine
      // if one of the two nodes on the edge contains the token
      if (goal == e.node1 || goal == e.node2) {
        if (e.node1.token == token || e.node2.token == token) {
          return true;
        }
      }
    }
    return false;
  }

  ;
}
