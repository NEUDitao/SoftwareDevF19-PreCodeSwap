package labyrinth;

import cs4500.hw2.json.NodePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * Class for labyrinth.Labyrinth.
 */
public class LabyrinthImpl implements Labyrinth {

  private List<NodePair> edges;
  private Map<String, Set<ColoredToken>> tokens = new HashMap<>();

  /**
   * Accepts a List of unique Strings where each String is the name of a node, and returns a
   * labyrinth.Labyrinth.
   */
  public LabyrinthImpl(List<NodePair> pairs) {
    edges = new ArrayList<>();
    edges.addAll(pairs);
  }

  @Override
  public void addColoredToken(ColoredToken token, String name) {
    Set<ColoredToken> ct = tokens.computeIfAbsent(name, k -> new HashSet<>());
    ct.add(token);
  }

  @Override
  public boolean reaches(ColoredToken token, String goal) {
    String currentNode = null;

    for (Entry<String, Set<ColoredToken>> theNode : tokens.entrySet()) {
      if (theNode.getValue().contains(token)) {
        currentNode = theNode.getKey();
        break;
      }

    }

    for (int i = 0; i <= edges.size(); i++) {
      if (Objects.equals(currentNode, goal)) {
        break;
      }

      for (NodePair e : edges) {
        if (Objects.equals(e.from, currentNode)) {
          if (e.to != null) {
            currentNode = e.to;
          }
        }
      }
    }

    return Objects.equals(currentNode, goal);

  }


}
