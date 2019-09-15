package labyrinth;

import java.util.NoSuchElementException;

public interface Labyrinth {
    void addColoredToken(ColoredToken ct, String node) throws NoSuchElementException;
    boolean reaches(ColoredToken ct, String node);
}
