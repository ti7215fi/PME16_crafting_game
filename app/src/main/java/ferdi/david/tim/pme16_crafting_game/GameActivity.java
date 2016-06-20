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

    private int                     maxX;  // amount of cells
    private int                     maxY; // amount of rows

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
        app.getLevelManager().setCurrentLevel(app.getLevelManager().getLevel(1));

        maxX = app.getLevelManager().getCurrentLevel().getColCount();
        maxY = app.getLevelManager().getCurrentLevel().getRowCount();

        playground = app.getLevelManager().getCurrentLevel().getPlayground();

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
        txtScore.setText("Punkte: " + newScore);
    }

    private void updateTimeText(int newTime) {
        txtTime.setText("Zeit: " + newTime + " s");
    }

    private void loadImagesResources() {
        imageResources = new Drawable[]{
                ResourcesCompat.getDrawable(getResources(), R.mipmap.stone, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.ore, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.cotton, null),
                ResourcesCompat.getDrawable(getResources(), R.mipmap.wood, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.fleisch, null),
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

        LinearLayout.LayoutParams lpRow = new LinearLayout.LayoutParams(sizeofCell * maxX, sizeofCell);
        LinearLayout.LayoutParams lpCell = new LinearLayout.LayoutParams(sizeofCell, sizeofCell);

        LinearLayout linBoardGame = (LinearLayout) findViewById(R.id.linBoardGame);

        for (int i = 0; i < maxY; i++) {
            LinearLayout linRow = new LinearLayout(context);
            for (int j = 0; j < maxX; j++) {
                playground[i][j] = new ImageView(context);
                playground[i][j].setBackground(imageResources[getRandomInt(0, 4)]);
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

                        if (firstClick) {
                            firstMoveX = x;
                            firstMoveY = y;
                            firstClick = false;
                        } else {
                            secMoveX = x;
                            secMoveY = y;

                            if (checkMove(firstMoveY, firstMoveX, secMoveY, secMoveX)) {
                                tempCell1 = playground[firstMoveY][firstMoveX].getBackground();
                                tempCell2 = playground[secMoveY][secMoveX].getBackground();

                                playground[secMoveY][secMoveX].setBackground(tempCell1);
                                playground[firstMoveY][firstMoveX].setBackground(tempCell2);
                                listOfPoints = new ArrayList<>();

                                Drawable tempDrawFirstMove = getField(firstMoveY, firstMoveX);
                                Drawable tempDrawSecMove = getField(secMoveY, secMoveX);

                                search(tempDrawFirstMove, firstMoveY, firstMoveX);
                                int amountOfCooFirstMove = listOfPoints.size();
                                score += amountOfCooFirstMove;
                                if (amountOfCooFirstMove > 4) // 4 or more connected blocks
                                {
                                    whiteOutBlock(listOfPoints);
                                    listOfPoints.clear();
                                }

                                search(tempDrawSecMove, secMoveY, secMoveX);  //start recursive search for same color fields
                                int amountOfCooSectMove = listOfPoints.size();
                                score += amountOfCooSectMove;
                                if (amountOfCooSectMove > 4) // 4 or more connected blocks
                                {
                                    whiteOutBlock(listOfPoints);
                                    listOfPoints.clear();
                                }
                                //fillWhiteFields();
                                updateScoreText(score);

                            }
                            firstClick = true;
                        }
                    }
                });
                linRow.addView(playground[i][j], lpCell);
            }
            if (linBoardGame != null) {
                linBoardGame.addView(linRow, lpRow);
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
     * recursive function looking for fields with same Drawable and add their Points in the list
     *
     * @param drawable drawable looking for
     * @param poY      start position Y
     * @param poX      start position X
     * @return true / false
     */
    @SuppressLint("NewApi")
    private boolean search(Drawable drawable, int poY, int poX) {
        if (getField(poY, poX) != drawable)  // stop if not correct background
        {
            return false;
        }
        if (containsPoint(listOfPoints, poY, poX))  //   -> prüfe ob feld in der liste if(getField(poY,poX ) == imageResources[5])
        {
            return false;
        }
        if (getField(poY, poX) == drawable)  //mark as visited
        {
            listOfPoints.add(new Point(poX, poY));
        }

        if (poX >= 1) {
            if (search(drawable, poY, poX - 1)) {
                return true;
            }
        }

        if (poY < maxY - 1) {
            if (search(drawable, poY + 1, poX)) {
                return true;
            }
        }

        if (poX < maxX - 1) {
            if (search(drawable, poY, poX + 1)) {
                return true;
            }
        }

        if (poY >= 1) {
            if (search(drawable, poY - 1, poX)) {
                return true;
            }
        }
        listOfPoints.add(new Point(poX, poY));
        return false;
    }

    /**
     * @param poY position y of the cell
     * @param poX position x of the cell
     * @return drawable on position y,x
     */
    private Drawable getField(int poY, int poX) {
        return playground[poY][poX].getBackground();
    }

    /**
     * draw all Points in the list white, to mark them to craft
     *
     * @param listOfPoints list with Points
     */
    @SuppressLint("NewApi")
    private void whiteOutBlock(List<Point> listOfPoints) {
        for (Point Point : listOfPoints) {
            playground[Point.y][Point.x].setBackground(imageResources[5]);
        }
    }

    /**
     * function to check the list, if Point is already
     *
     * @param list list with Points
     * @param posY position y
     * @param posX position x
     * @return true if the list contains the Point
     */
    private boolean containsPoint(List<Point> list, int posY, int posX) {
        for (Point Point : list) {
            if ((Point.y == posY) && (Point.x == posX)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    @SuppressLint("NewApi")
    private void fillWhiteFields() {
        for (int i = 0; i < maxX; i++) {
            for (int j = maxY - 1; j >= 0; j--) {
                int temp = 2;
                while (playground[j][i].getBackground() == imageResources[5]) {

                    if (playground[maxY - temp][i].getBackground() == imageResources[5]) {
                        if (temp >= 1) {

                            temp--;
                        } else {
                            return;
                        }
                    } else if (playground[maxY - temp][i].getBackground() != imageResources[5]) {
                        playground[j][i].setBackground(playground[maxY - temp][i].getBackground());
                        playground[maxY - temp][i].setBackground(imageResources[5]);
                    }
                }

            }
        }
    }
}
