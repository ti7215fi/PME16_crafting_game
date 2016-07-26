package ferdi.david.tim.pme16_crafting_game;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 11.07.2016.
 */
public class ExtendedCoordinate {
    private Point point;
    private Drawable drawable;

    public ExtendedCoordinate(Point point, Drawable drawable)
    {
        this.point = point;
        this.drawable = drawable;
    }

    public int getPointX()
    {
        return this.point.x;
    }
    public int getPointY()
    {
        return this.point.y;
    }
    public Drawable getDrawable()
    {
        return  this.drawable;
    }
}
