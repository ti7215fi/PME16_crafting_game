package ferdi.david.tim.pme16_crafting_game;

import com.orm.SugarRecord;

/**
 * Created by Tim Fischer on 11.07.2016.
 */
public class DBResource extends SugarRecord {
    private int         type;
    private int         amount;
    private int         productionChainLevel;
    private int         materialCosts;
    private int         materialType;
    private DBUser      user;

    public DBResource() {
    }

    public DBResource(EResourceType _type, DBUser _user) {
        this.type = _type.getValue();
        this.amount = 0;
        this.user = _user;
        this.init();
    }

    private void init() {
        EResourceType[] resources = EResourceType.values();
        switch (EResourceType.values()[this.type + 1]) {
            case BAR_IRON:
                this.materialCosts = 50;
                this.materialType = EResourceType.ORE.getValue();
                break;
            case PLANKS:
                this.materialCosts = 50;
                this.materialType = EResourceType.WOOD.getValue();
                break;
            case ROPE:
                this.materialCosts = 10;
                this.materialType = EResourceType.COTTON.getValue();
                break;
            case STONE_BLOCKS:
                this.materialCosts = 50;
                this.materialType = EResourceType.STONE.getValue();
                break;
            default:
                this.materialCosts = 0;
                this.materialType = -1;
                break;
        }
        this.productionChainLevel = EResourceType.values()[this.type + 1].getProductionChainLevel();
    }

    public int getType() {
        return this.type;
    }

    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int _amount) {
        this.amount = _amount;
    }

    public int getProductionChainLevel() {return this.productionChainLevel;}
    public int getMaterialCosts() {return this.materialCosts;}
    public int getMaterialType() {return this.materialType;}
}
