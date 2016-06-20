package ferdi.david.tim.pme16_crafting_game;

import android.app.Application;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Tim Fischer on 07.06.2016.
 */
public class ApplicationController extends Application{

    private static ApplicationController    app = null;
    private LevelManager                    levelManager = LevelManager.getInstance();

    public LevelManager getLevelManager() {
        return levelManager;
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

}
