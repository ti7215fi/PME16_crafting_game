package ferdi.david.tim.pme16_crafting_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private ApplicationController   app;
    private static final String     LOG_TAG = MainActivity.class.getSimpleName();

    private int                     maxX = 7;  // amount of cells
    private int                     maxY = 7; // amount of rows

    private ImageView[][]           playground;
    private Context                 context;
    private Drawable[]              imageResources;
    private List<Point>             listOfPoints = new ArrayList<>();

    private boolean                 firstClick = true;
    private int                     firstMoveX;
    private int                     firstMoveY;
    private int                     secMoveX;
    private int                     secMoveY;

    private int                     score;
    private TextView                txtScore;
    private TextView                txtTime;

    private int                     time;
    private Timer                   timer;
    private TimerTask               timerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateTimeText(time);
                    time++;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        app = (ApplicationController) getApplication();
        playground = new ImageView[maxX][maxY];

        context = this;

        score = 0;
        txtScore = (TextView) findViewById(R.id.txt_score);

        txtTime = (TextView) findViewById(R.id.txt_time);
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        loadImagesResources();
        designBoard();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState( state );
        state.putInt("score", score);
        state.putInt("time", time);
    }
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState( state );
        updateScoreText(state.getInt("score"));
        time = state.getInt("time");
        updateTimeText(time);
    }

    private void updateScoreText(int newScore) {
        txtScore.setText(getString(R.string.game_points) + newScore);
    }

    private void updateTimeText(int newTime) {
        txtTime.setText(getString(R.string.game_time) + newTime + " s");
    }

    private void loadImagesResources() {
        imageResources = new Drawable[]{
                ResourcesCompat.getDrawable(getResources(), R.mipmap.stone, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.ore, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.cotton, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.wood, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.meat, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.leer, null)
        };
    }

    /**
     * - Creates the playground with width and height of the device
     * - Fill ImageView array with random pictures
     * - add OnClickListener du every picture
     * - call recursive function to mark fields
     */
    @SuppressLint("NewApi")
    private void designBoard() {
        int sizeofCell = Math.round(app.ScreenWidth() / maxX);

        //LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCell * maxX, sizeofCell);
        //LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCell, sizeofCell);

        LinearLayout linBoardGame = (LinearLayout) findViewById(R.id.linBoardGame);

        for (int i = 0; i < maxY; i++) {
            LinearLayout linRow = new LinearLayout(context);
            for (int j = 0; j < maxX; j++) {
                playground[i][j] = new ImageView(context);
                playground[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                playground[i][j].setImageDrawable(imageResources[getRandomInt(0, 4)]);
                final int y = i;
                final int x = j;
                playground[i][j].setOnClickListener(new View.OnClickListener() {
                    /**
                     * - check if first or second move
                     * - if second move, switch images
                     * @param v actual view
                     */
                    @Override
                    public void onClick(View v) {
                        Drawable tempCell1, tempCell2;

                        if (firstClick)
                        {
                            firstMoveX = x;
                            firstMoveY = y;
                            firstClick = false;
                        }
                        else
                        {
                            secMoveX = x;
                            secMoveY = y;

                            if (checkMove(firstMoveY, firstMoveX, secMoveY, secMoveX))
                            {
                                tempCell1 = playground[firstMoveY][firstMoveX].getDrawable();
                                tempCell2 = playground[secMoveY][secMoveX].getDrawable();

                                playground[secMoveY][secMoveX].setImageDrawable(tempCell1);
                                playground[firstMoveY][firstMoveX].setImageDrawable(tempCell2);

                                listOfPoints = new ArrayList<>();
                                do {
                                    listOfPoints.clear();
                                    checkPlayground();
                                    fillWhiteFields();
                                }while(listOfPoints.size()!=0);

                                listOfPoints.clear();

                                updateScoreText(score);

                            }
                            firstClick = true;
                        }
                    }
                });
                linRow.addView(playground[i][j]);
            }
            if (linBoardGame != null) {
                linBoardGame.addView(linRow);
            }
        }
    }


    /**
     * Returns a random number between two parameters
     *
     * @param min minimum number
     * @param max maximum
     * @return random number
     */
    private int getRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Check, if the move done by the player is correct
     *
     * @param m1Y value X first  move
     * @param m1X value Y first  move
     * @param m2Y value Y second  move
     * @param m2X value Y second  move
     * @return true, if move is correct, false if move is not allowed
     */
    private boolean checkMove(int m1Y, int m1X, int m2Y, int m2X) {
        int diffYY, diffXX;

        if (m1Y < m2Y) {
            diffYY = m2Y - m1Y;
        } else {
            diffYY = m1Y - m2Y;
        }

        if (m1X < m2X) {
            diffXX = m2X - m1X;
        } else {
            diffXX = m1X - m2X;
        }

        if (diffYY == 0 ^ diffXX == 0) {
            if (diffXX == 1 ^ diffYY == 1) {
                return true;
            }

        }
        return false;
    }

    /**
     * @param posY position y of the cell
     * @param posX position x of the cell
     * @return drawable on position y,x
     */
    private Drawable getField(int posY, int posX) {
        return playground[posY][posX].getDrawable();
    }

    /**
     * function to check the list, if Point is already
     *
     * @param posY position y
     * @param posX position x
     * @return true if the list contains the Point
     */
    private boolean containsPoint(int posY, int posX)
    {
        return listOfPoints.contains(new Point(posY,posX));
    }

    /**
     * check every field with search
     */
    private void checkPlayground()
    {
        for(int i = 0; i < maxY; i++)
        {
            for(int j = 0; j < maxX; j++)
            {
                search(i,j);
            }
        }
        whiteOutBlocks();
    }

    /**
     * check field and the field y+1/-1 and x+1/-1 if there are 3 Blocks together. Add the coordinates
     * to the list
     * @param posY position x
     * @param posX position y
     */
    private void search(int posY, int posX)
    {
        // check vertical
        if((posY >=1) && (posY < (maxY-1))) {
            Drawable drawable = getField(posY,posX);
            if ((getField(posY - 1, posX) == drawable) && (getField(posY + 1, posX) == drawable)) {
                if (!containsPoint(posY - 1, posX)) {
                    listOfPoints.add(new Point(posY - 1, posX));
                }
                if (!containsPoint(posY, posX)) {
                    listOfPoints.add(new Point(posY, posX));
                }
                if (!containsPoint(posY + 1, posX)) {
                    listOfPoints.add(new Point(posY + 1, posX));
                }
            }
        }
        // check horizontal
        if((posX >=1)&&(posX < (maxX-1))) {
            Drawable drawable = getField(posY,posX);
            if ((getField(posY, posX - 1) == drawable) && (getField(posY, posX + 1) == drawable)) {
                if (!containsPoint(posY, posX - 1)) {
                    listOfPoints.add(new Point(posY, posX - 1));
                }
                if (!containsPoint(posY, posX)) {
                    listOfPoints.add(new Point(posY, posX));
                }
                if (!containsPoint(posY, posX + 1)) {
                    listOfPoints.add(new Point(posY, posX + 1));
                }
            }
        }
    }

    /**
     * mark all fields form the list of coordinates with with background
     */
    @SuppressLint("NewApi")
    private void whiteOutBlocks() {
        for (Point Point : listOfPoints) {
            playground[Point.x][Point.y].setImageDrawable(imageResources[5]);
        }
    }

    /**
     * go through th playground and switch blocks to replace white fields with blocks above or
     * new random fields
     */
    @SuppressLint("NewApi")
    private void fillWhiteFields()
    {
        while(checkForWhiteFields())
        {
            for(int i = maxY-1; i >= 0 ; i--) {
                for (int j = 0; j < maxX; j++) {

                    if (playground[i][j].getDrawable() == imageResources[5])
                    {

                        if(i != 0) {
                            playground[i][j].setImageDrawable(playground[i - 1][j].getDrawable());
                            playground[i - 1][j].setImageDrawable(imageResources[5]);
                        }
                        else {
                            playground[i][j].setImageDrawable(imageResources[getRandomInt(0, 4)]);
                        }
                    }
                }
            }
        }
    }

    /**
     * going through the field to find at least one white field
     * @return true, if there is at least one white block; false, if there is no white fields
     */
    private boolean checkForWhiteFields()
    {
        for(int i = 0; i < maxY; i++)
        {
            for(int j = 0; j < maxX; j++)
            {
                if(playground[i][j].getDrawable() == imageResources[5])
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkForLockedPlayground()
    {
        for(int i = 0; i < maxY; i++)
        {
            for(int j = 0; j < maxX; j++)
            {
                Drawable drawable = getField(i,j);
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ///#
                ///#
                //#
                if((i >=1 && i < (maxY -1))&&(j >=1 && j < (maxX-1)))
                {
                    if(playground[i-1][j].getDrawable() == drawable) // eins drüber
                    {
                        if(playground[i+1][j-1].getDrawable() == drawable) // einsrunter links
                        {
                            return true;
                        }
                        else if(playground[i+1][j+1].getDrawable() == drawable) // einsrunter rechts
                        {
                            return true;
                        }
                    }
                }
                if(playground[i+1][j].getDrawable() == drawable) // eins drunter
                {
                    if(playground[i-1][j-1].getDrawable() == drawable) // einshoch links
                    {
                        return true;
                    }
                    else if(playground[i-1][j+1].getDrawable() == drawable) // einshoch rechts
                    {
                        return true;
                    }
                }
                if(playground[i][j+1].getDrawable() == drawable) // eins rechst
                {
                    if(playground[i+1][j-1].getDrawable() == drawable) // einshoch links
                    {
                        return true;
                    }
                    else if(playground[i-1][j-1].getDrawable() == drawable) // einshoch rechts
                    {
                        return true;
                    }
                }
                if(playground[i][j-1].getDrawable() == drawable) // eins links
                {
                    if(playground[i+1][j+1].getDrawable() == drawable) // einshoch links
                    {
                        return true;
                    }
                    else if(playground[i-1][j+1].getDrawable() == drawable) // einshoch rechts
                    {
                        return true;
                    }
                }

            }
        }

        return false;
    }
}
