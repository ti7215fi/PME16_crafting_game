package ferdi.david.tim.pme16_crafting_game;

import android.widget.ImageView;

/**
 * Created by Tim Fischer on 07.06.2016.
 */
public class LevelManager {

    private static LevelManager     instance;
    private Level                   currentLevel;
    private Level[]                 levels = new Level[1];
    private int                     levelCount = 1;

    private LevelManager() {
        loadLevels();
    }

    public static LevelManager getInstance() {
        if(LevelManager.instance == null) {
            LevelManager.instance = new LevelManager();
        }
        return LevelManager.instance;
    }

    private void createLevel(ImageView[][] _playground, int _levelNumber) {
        Level newLevel = new Level(_playground, _levelNumber);
        levels[_levelNumber - 1] = newLevel;
    }

    public void setCurrentLevel(Level level) {
        currentLevel = level;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public Level getLevel(int levelNumber) {
        return levels[levelNumber - 1];
    }

    private void loadLevels() {
        createLevel(levelOne(), 1);
    }

    // available levels
    private ImageView[][] levelOne() {
        int rowCount = 8;
        int colCount = 8;
        return new ImageView[rowCount][colCount];
    }

}
