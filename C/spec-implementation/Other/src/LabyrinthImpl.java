import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class for Labyrinth.
 */
public class LabyrinthImpl implements Labyrinth {

    private ArrayList<String> namesOfNodes = new ArrayList<>();

    /**
     * Accepts a List of unique Strings where each String is the name of a node, and returns a Labyrinth.
     * @param loS List of Strings to be sorted and turned into a Labyrinth
     */
    public LabyrinthImpl(ArrayList<String> loS) {
        HashSet<String> sortedLOS = new HashSet<>();

        for (String name : loS) {
            //Naturally prevents duplicates from being added to itself.
            sortedLOS.add(name);
        }

        namesOfNodes.addAll(sortedLOS);
    }

    @Override
    public void addColoredToken(ColoredToken token, String name) {
        if (namesOfNodes.contains(name)) {
            //Add token to namesOfNodes.get(name)
        }
    }

    @Override
    public boolean reaches(ColoredToken token, String name) {
        //Unable to implement.
        return false;
    }

}
