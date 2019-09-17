import java.awt.*;

public class ColoredTokenImpl implements ColoredToken {

    Color color;

    /**
     * Accepts a color and returns a ColoredToken of that color.
     * @param color Color of token
     */
    public ColoredTokenImpl(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

}
