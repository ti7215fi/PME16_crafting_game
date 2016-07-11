package ferdi.david.tim.pme16_crafting_game;

/**
 * Created by Tim Fischer on 11.07.2016.
 */
public enum EResourceType {
    NONE(0),
    STONE(1),
    ORE(2),
    COTTON(3),
    WOOD(4),
    MEAT(5);

    private final int value;
    private EResourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
