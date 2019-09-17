package labyrinth;

/**
 * labyrinth.Labyrinth Interface.
 */
public interface Labyrinth {

    /**
     * Accepts a labyrinth.ColoredToken and a node name. If a node exists with that name, add the token to that node.
     * Otherwise, throw a NoSuchElementException.
     * @param token Token to be added
     * @param name Name of the node that token will be added to
     */
    public void addColoredToken(ColoredToken token, String name);

    /**
     * Accepts a labyrinth.ColoredToken and a node name. If the given labyrinth.ColoredToken is not on any node, return false.
     * If the given labyrinth.ColoredToken is on any of the nodes, return whether the given node can be reached from a node
     * that contains the given labyrinth.ColoredToken.
     * @param token Token to be navigated from
     * @param name Name of node being searched
     * @return
     */
    public boolean reaches(ColoredToken token, String name);

}
