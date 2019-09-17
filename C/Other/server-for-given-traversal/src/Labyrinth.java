import java.util.List;

/**
 * A connectivity specification for a Simple Graph.
 */
public interface Labyrinth {

  /**
   * Create a labyrinth.Labyrinth with named Nodes.
   *
   * @param nodes the nodes
   * @return the labyrinth.Labyrinth created
   */
  Labyrinth createLabyrinth(List<Node> nodes);

  /**
   * Adds a colored Token to a Node of a specified name.
   *
   * @param token the colored token
   * @param node  node's name
   * @return the updated labyrinth.Labyrinth.
   */
  Labyrinth addToken(Token token, Node node);

  /**
   * Determines if some colored token can reach some named Node by following the Edges within a
   * particular labyrinth.Labyrinth
   *
   * @param token the token queried
   * @param goal  the node trying to be found
   * @return if the token can reach the node
   */
  boolean canReach(Token token, Node goal);

}
