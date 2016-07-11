package ferdi.david.tim.pme16_crafting_game;

import com.orm.SugarRecord;

/**
 * Created by Tim Fischer on 11.07.2016.
 */
public class DBResource extends SugarRecord {
    private int         type;
    private int         amount;
    private DBUser      user;

    public DBResource() {
        this.amount = 0;
    }

    public DBResource(int _type, DBUser _user) {
        this.type = _type;
        this.amount = 0;
        this.user = _user;
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
}
