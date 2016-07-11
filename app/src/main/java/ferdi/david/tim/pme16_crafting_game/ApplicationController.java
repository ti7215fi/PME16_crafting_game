package ferdi.david.tim.pme16_crafting_game;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.orm.SugarApp;

import java.util.Arrays;

/**
 * Created by Tim Fischer on 07.06.2016.
 */
public class ApplicationController extends SugarApp{

    private DBUser user;

    /**
     * @return amount of horizontal pixels of the device
     */
    public float ScreenWidth()
    {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public DBUser getUser() {
        return this.user;
    }

    public void setUser(DBUser _user) {
        this.user = _user;
    }

}
