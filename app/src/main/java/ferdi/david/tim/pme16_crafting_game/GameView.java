package ferdi.david.tim.pme16_crafting_game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Paint paint = new Paint();

    public GameView(Context context) {
        super(context);
        init(null,0);
    }



    private void init(AttributeSet attrs, int defStyle)
    {
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0,0,getWidth(),getHeight(),paint);
    }
}

