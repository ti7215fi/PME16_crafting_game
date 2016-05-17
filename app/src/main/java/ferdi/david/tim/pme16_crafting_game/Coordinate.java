package ferdi.david.tim.pme16_crafting_game;

/**
 * struct to realize a coordinate with y and x value
 */
public class Coordinate {
    private int y;
    private int x;

    public Coordinate(int posY, int posX)
    {
        y = posY; x = posX;
    }

    public int getX() {
        return x;
    }

    public void setX(int value) {
        this.x = value;
    }

    public int getY() {
        return y;
    }

    public void setY(int value) {
        this.y = value;
    }
}
