package ferdi.david.tim.pme16_crafting_game;

import android.widget.ImageView;

/**
 * Created by Tim Fischer on 07.06.2016.
 */
public class Level {

    private ImageView[][]   playground;
    private final int       levelNumber;
    private final int       colCount;
    private final int       rowCount;

    public Level(ImageView[][] _playground, int _levelNumber) {
        colCount = 8;
        rowCount = 8;
        playground = _playground;
        levelNumber = _levelNumber;
    }

    public Level(ImageView[][] _playground, int _levelNumber, int _colCount, int _rowCount) {
        colCount = _colCount;
        rowCount = _rowCount;
        playground = _playground;
        levelNumber = _levelNumber;
    }

    public ImageView[][] getPlayground() {
        return playground;
    }

    public final  int getLevelNumber() {
        return levelNumber;
    }

    public final int getColCount() {
        return colCount;
    }

    public final int getRowCount() {
        return rowCount;
    }

}
