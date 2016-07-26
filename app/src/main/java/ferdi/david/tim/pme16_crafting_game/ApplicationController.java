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
    private int items[] = {
            R.mipmap.stone,
            R.mipmap.ore,
            R.mipmap.cotton,
            R.mipmap.wood,
            R.mipmap.meat,
            R.mipmap.bar_iron,
            R.mipmap.planks,
            R.mipmap.rope,
            R.mipmap.stone_blocks
    } ;

    private static ApplicationController instance;

    public static ApplicationController getInstance () {
        if (ApplicationController.instance == null) {
            ApplicationController.instance = new ApplicationController ();
        }
        return ApplicationController.instance;
    }


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

    public int[] getItems() {
        return this.items;
    }

}
