package ferdi.david.tim.pme16_crafting_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity
{
    final static int            maxX = 8;  // amount of cells
    final static int            maxY = 8; // amount of rows

    private ImageView[][]       playgroundCells = new ImageView[maxY][maxX];
    private Context             context;
    private Drawable[]          drawCell = new Drawable[6];
    private List<Coordinate>    listOfCoordinates = new ArrayList<>();

    private boolean             firstClick = true;
    private int                 firstMoveX;
    private int                 firstMoveY;
    private int                 secMoveX;
    private int                 secMoveY;

    private int                 score = 0;

    TextView txtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        context = this;
        txtScore = (TextView)findViewById(R.id.txt_score);
        loadResources();
        designBoard();
    }

    /**
     * load pictures into array
     */
    private void loadResources()
    {
        drawCell[3] = ResourcesCompat.getDrawable(getResources(), R.drawable.feuer, null);
        drawCell[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.eisen, null);
        drawCell[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.holz, null);
        drawCell[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.wasser, null);
        drawCell[4] = ResourcesCompat.getDrawable(getResources(), R.drawable.fleisch, null);
        drawCell[5] = ResourcesCompat.getDrawable(getResources(), R.drawable.leer, null);
    }

    /**
     * - Creates the playground with width and height of the device
     * - Fill ImageView array with random pictures
     * - add OnClickListener du every picture
     * - call recursive function to mark fields
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
                                listOfCoordinates = new ArrayList<>();

                                Drawable tempDrawFirstMove = getField(firstMoveY,firstMoveX);
                                Drawable tempDrawSecMove = getField(secMoveY,secMoveX);

                                search(tempDrawFirstMove,firstMoveY,firstMoveX);
                                int amountOfCooFirstMove = listOfCoordinates.size();
                                if(amountOfCooFirstMove > 4) // 4 or more connected blocks
                                {
                                    whiteOutBlock(listOfCoordinates);
                                    listOfCoordinates.clear();
                                }

                                search(tempDrawSecMove,secMoveY,secMoveX);  //start recursive search for same color fields
                                int amountOfCooSectMove = listOfCoordinates.size();
                                if(amountOfCooSectMove > 4) // 4 or more connected blocks
                                {
                                    whiteOutBlock(listOfCoordinates);
                                    listOfCoordinates.clear();
                                }
                                //fillWhiteFields();
                                //txtScore.setText("Punkte: " + score);   // update score text field

                            }
                            firstClick = true;
                        }
                    }
                });
                linRow.addView(playgroundCells[i][j], lpCell);
            }
            if(linBoardGame != null) {
                linBoardGame.addView(linRow, lpRow);
            }
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
        return rand.nextInt((max - min) + 1) + min;
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
        int diffYY, diffXX;

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
     * recursive function looking for fields with same Drawable and add their coordinates in the list
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
        if(containsCoordinate(listOfCoordinates,poY,poX))  //   -> prüfe ob feld in der liste if(getField(poY,poX ) == drawCell[5])
        {
            return false;
        }
        if(getField(poY,poX ) == drawable)  //mark as visited
        {
            listOfCoordinates.add(new Coordinate(poY,poX));
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
        listOfCoordinates.add(new Coordinate(poY,poX));
        return false;
    }

    /**
     *
     * @param poY position y of the cell
     * @param poX position x of the cell
     * @return drawable on position y,x
     */
    private Drawable getField(int poY, int poX)
    {
        return playgroundCells[poY][poX].getBackground();
    }

    /**
     * draw all coordinates in the list white, to mark them to craft
     * @param listOfCoordinates list with coordinates
     */
    @SuppressLint("NewApi")
    private void whiteOutBlock(List<Coordinate> listOfCoordinates)
    {
        for(Coordinate coordinate : listOfCoordinates)
        {
            playgroundCells[coordinate.getY()][coordinate.getX()].setBackground(drawCell[5]);
        }
    }

    /**
     * function to check the list, if coordinate is already
     * @param list list with coordinates
     * @param posY position y
     * @param posX position x
     * @return true if the list contains the coordinate
     */
    private boolean containsCoordinate(List<Coordinate> list, int posY, int posX)
    {
        for(Coordinate coordinate : list)
        {
            if((coordinate.getY() == posY) && (coordinate.getX() ==posX))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    @SuppressLint("NewApi")
    private void fillWhiteFields()
    {
        for(int i = 0; i < maxX; i++)
        {
            for(int j = maxY-1; j >= 0; j--)
            {
                int temp = 2;
                while(playgroundCells[j][i].getBackground() == drawCell[5])
                {

                    if(playgroundCells[maxY-temp][i].getBackground() == drawCell[5] )
                    {
                        if(temp >=1) {

                            temp--;
                        }
                        else
                        {
                            return;
                        }
                    }
                    else if(playgroundCells[maxY-temp][i].getBackground() != drawCell[5] )
                    {
                        playgroundCells[j][i].setBackground(playgroundCells[maxY-temp][i].getBackground());
                        playgroundCells[maxY-temp][i].setBackground(drawCell[5]);
                    }
                }

            }
        }
    }
}