package ferdi.david.tim.pme16_crafting_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class Playground extends AppCompatActivity
{
    final static int maxX = 8;  // amount of cells
    final static int maxY = 13; // amount of rows

    private ImageView[][] playgroundCells = new ImageView[maxY][maxX];
    private Context context;
    private Drawable[] drawCell = new Drawable[6];

    private boolean firstClick = true;
    private int firstMoveX;
    private int firstMoveY;

    private int secMoveX;
    private int secMoveY;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_playground);
        context = this;
        loadResources();
        designBoard();
    }

    /**
     * load pictures into array
     */
    private void loadResources()
    {
        drawCell[3] = context.getResources().getDrawable(R.drawable.feuer);
        drawCell[0] = context.getResources().getDrawable(R.drawable.eisen);
        drawCell[1] = context.getResources().getDrawable(R.drawable.holz);
        drawCell[2] = context.getResources().getDrawable(R.drawable.wasser);
        drawCell[4] = context.getResources().getDrawable(R.drawable.fleisch);
        drawCell[5] = context.getResources().getDrawable(R.drawable.leer);
    }

    /**
     * - Creates the playground with width and height of the device
     * - Fill ImageView array with random pictures
     * - add OnClickListener du every picture
     */
    @SuppressLint("NewApi")
    private void designBoard()
    {
        int sizeofCell = Math.round(ScreenWidth()/ maxX);

        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCell * maxX, sizeofCell);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCell, sizeofCell);

        LinearLayout linBoardGame = (LinearLayout) findViewById(R.id.linBoardGame);

        for(int i = 0; i< maxY;i++)
        {
            LinearLayout linRow = new LinearLayout(context);
            for(int j = 0; j< maxX;j++)
            {
                playgroundCells[i][j] = new ImageView(context);
                playgroundCells[i][j].setBackground(drawCell[getRandomInt(0,4)]);
                final int y = i;
                final int x = j;
                playgroundCells[i][j].setOnClickListener(new View.OnClickListener() {
                    /**
                     * - check if first or second move
                     * - if second move, switch images
                     * @param v actual view
                     */
                    @Override
                    public void onClick(View v) {
                        Drawable tempCell1,tempCell2;

                        if(firstClick)
                        {
                            firstMoveX = x;
                            firstMoveY = y;
                            firstClick = false;
                        }
                        else
                        {
                            secMoveX = x;
                            secMoveY = y;

                            if(checkMove(firstMoveY,firstMoveX,secMoveY,secMoveX))
                            {
                                tempCell1 = playgroundCells[firstMoveY][firstMoveX].getBackground();
                                tempCell2 = playgroundCells[secMoveY][secMoveX].getBackground();

                                playgroundCells[secMoveY][secMoveX].setBackground(tempCell1);
                                playgroundCells[firstMoveY][firstMoveX].setBackground(tempCell2);
                                search(getField(secMoveY,secMoveX),secMoveY,secMoveX);            //only test
                            }
                            firstClick = true;
                        }
                    }
                });

                linRow.addView(playgroundCells[i][j], lpCell);
            }
            linBoardGame.addView(linRow,lpRow);
        }
    }

    /**
     *
     * @return amount of horizontal pixels of the device
     */
    private float ScreenWidth()
    {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * Returns a random number between two parameters
     * @param min minimum number
     * @param max maximum
     * @return random number
     */
    private int getRandomInt(int min, int max)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    /**
     * Check, if the move done by the player is correct
     * @param m1Y value X first  move
     * @param m1X value Y first  move
     * @param m2Y value Y second  move
     * @param m2X value Y second  move
     * @return true, if move is correct, false if move is not allowed
     */
    private boolean checkMove(int m1Y, int m1X, int m2Y, int m2X)
    {
        int diffYY, diffXX = 0;

        if(m1Y < m2Y)
        {
            diffYY = m2Y - m1Y;
        }
        else
        {
            diffYY = m1Y - m2Y;
        }

        if(m1X < m2X)
        {
            diffXX = m2X - m1X;
        }
        else
        {
            diffXX = m1X - m2X;
        }

        if (diffYY == 0 ^ diffXX == 0)
        {
            if(diffXX == 1 ^ diffYY == 1)
            {
                return true;
            }

        }
        return false;
    }

    /**
     * recursive function looking for field with same Drawable and set group as white image
     * @param drawable drawable looking for
     * @param poY start position Y
     * @param poX start position X
     * @return true / false
     */
    @SuppressLint("NewApi")
    private boolean search(Drawable drawable,int poY, int poX)
    {
        if(getField(poY,poX ) != drawable)  // stop if not correct background
        {
            return false;
        }
        if(getField(poY,poX ) == drawCell[5])  // stop if white field
        {
            return false;
        }
        if(getField(poY,poX ) == drawable)  //mark as visited
        {
            playgroundCells[poY][poX].setBackground(drawCell[5]);
        }

        if(poX >=1) {
            if (search(drawable, poY, poX - 1)) {
                return true;
            }
        }

        if(poY < maxY-1) {
            if (search(drawable, poY + 1, poX)) {
                return true;
            }
        }

        if(poX < maxX-1) {
            if (search(drawable, poY, poX + 1)) {
                return true;
            }
        }

        if(poY >= 1) {
            if (search(drawable, poY - 1, poX)) {
                return true;
            }
        }

        playgroundCells[poY][poX].setBackground(drawCell[5]);
        return false;
    }

    /**
     *
     * @param poY
     * @param poX
     * @return drawable on position y,x
     */
    private Drawable getField(int poY, int poX)
    {
        return playgroundCells[poY][poX].getBackground();
    }
}
