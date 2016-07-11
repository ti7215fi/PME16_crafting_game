package ferdi.david.tim.pme16_crafting_game;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Arrays;

/**
 * Created by Tim Fischer on 11.07.2016.
 */
public class DBGame extends SugarRecord{

    private int     gameTime;
    private int     score;
    private String  playground;
    private DBUser  user;
    @Ignore
    private int[][] playgroundAsIntArray;

    public DBGame() {
        this.gameTime = 0;
        this.score = 0;
        this.playground = null;
    }

    public DBGame(String _playground) {
        this.gameTime = 0;
        this.score = 0;
        this.playground = _playground;
    }

    public DBGame(int _time, int _score, String _playground) {
        this.gameTime = _time;
        this.score = _score;
        this.playground = _playground;
    }

    public int getGameTime() {
        return this.gameTime;
    }

    public int getScore() {
        return this.score;
    }

    public String getPlayground() {
        return this.playground;
    }

    public int[][] getPlaygroundAsIntArray() {
        if(this.playgroundAsIntArray != null) {
            this.playgroundAsIntArray = new int[7][7];
            String dbPlayground[] = this.playground.split(",");

            int rowIndex = -1;
            for (int itemIndex = 0; itemIndex < dbPlayground.length; ++itemIndex) {
                if (itemIndex % 7 == 0) {
                    ++rowIndex;
                }
                this.playgroundAsIntArray[rowIndex][itemIndex % 7] = Integer.parseInt(dbPlayground[itemIndex]);
            }
        }
        return this.playgroundAsIntArray;
    }

    public void setPlayground(String _playground) {
        this.playground = _playground;
    }

    public void setPlayground(int[][] _playground) {
        String playgroundToString = "";
        for(int index = 0; index < 7; ++index) {
            playgroundToString += Arrays.toString(_playground[index]);
        }
        playgroundToString = playgroundToString.replace("]", ",");
        this.playground = playgroundToString.replace("[", " ");
    }

    public void setUser(DBUser _user) {
        this.user = _user;
    }

}
