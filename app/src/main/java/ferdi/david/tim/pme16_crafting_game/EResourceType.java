package ferdi.david.tim.pme16_crafting_game;

/**
 * Created by Tim Fischer on 11.07.2016.
 */
public enum EResourceType {
    NONE(-1, -1),
    STONE(0, 0),
    ORE(1, 0),
    COTTON(2, 0),
    WOOD(3, 0),
    MEAT(4, 0),
    BAR_IRON(5, 1),
    PLANKS(6, 1),
    ROPE(7, 1),
    STONE_BLOCKS(8, 1);

    private final int value;
    private final int productionChainLevel;
    private EResourceType(int value, int productionChainLevel) {
        this.value = value;
        this.productionChainLevel = productionChainLevel;
    }

    public int getValue() {
        return value;
    }
    public int getProductionChainLevel() {return this.productionChainLevel; }
    public int getResource() {
        if(this.getValue() > 0) {
            return ApplicationController.getInstance().getItems()[this.getValue()];
        }
        return -1;
    }
}
