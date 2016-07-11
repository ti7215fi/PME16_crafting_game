package ferdi.david.tim.pme16_crafting_game;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Tim Fischer on 11.07.2016.
 */
public class DBUser extends SugarRecord{
    private String      username;
    private String      password;

    public DBUser() {
    }

    public DBUser(String _username, String _password) {
        this.username = _username;
        this.password = _password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public DBGame getGame() {
        List<DBGame> game = DBGame.find(DBGame.class,"user = ?", this.getId().toString());
        if(game.size() == 0) {
            return null;
        }
        return game.get(0);
    }

    public List<DBResource> getInventory() {
        return DBResource.find(DBResource.class,"user = ?", this.getId().toString());
    }

}
