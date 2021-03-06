package ferdi.david.tim.pme16_crafting_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private ApplicationController   app;
    private static final String     LOG_TAG = MainActivity.class.getSimpleName();

    private int                     maxX = 7;  // amount of cells
    private int                     maxY = 7; // amount of rows

    private boolean                 firstClick = true;
    private int                     firstMoveX;
    private int                     firstMoveY;
    private int                     secMoveX;
    private int                     secMoveY;

    private ImageView[][]           playground;
    private int[][]                 dbPlayground;
    private Context                 context;
    private Drawable[]              imageResources;
    private Drawable                noneResource;
    private List<ExtendedCoordinate>listOfPoints = new ArrayList<>();
    private LinearLayout            linBoardGame;
    private Animation               blinkAnimation;

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
        linBoardGame = (LinearLayout) findViewById(R.id.linBoardGame);
        playground = new ImageView[maxX][maxY];
        dbPlayground = new int[maxX][maxY];
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);

        context = this;

        txtScore = (TextView) findViewById(R.id.txt_score);

        int score = 0;
        if(this.app.getUser().getGame() != null) {
            score = this.app.getUser().getGame().getScore();
        }

        this.updateScoreText(score);

        txtTime = (TextView) findViewById(R.id.txt_time);
        timer = new Timer();

        loadImagesResources();
        designBoard();

        if(this.app.getUser() != null && this.app.getUser().getGame() != null) {
            Log.i(LOG_TAG, "Spielfeld vorhanden || " + this.app.getUser().getGame().getPlayground() );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState( state );
        //state.putInt("score", score);
        state.putInt("time", time);
        DBGame game = this.app.getUser().getGame();
        if(game != null) {
            game.setPlayground(dbPlayground);
            game.save();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState( state );
        //updateScoreText(state.getInt("score"));
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
        noneResource =  ResourcesCompat.getDrawable(getResources(), R.drawable.leer, null);

        imageResources = new Drawable[5];
        imageResources[EResourceType.STONE.getValue()] =  ResourcesCompat.getDrawable(getResources(), R.mipmap.stone, null);
        imageResources[EResourceType.ORE.getValue()] =  ResourcesCompat.getDrawable(getResources(), R.mipmap.ore, null);
        imageResources[EResourceType.COTTON.getValue()] =  ResourcesCompat.getDrawable(getResources(), R.mipmap.cotton, null);
        imageResources[EResourceType.WOOD.getValue()] =  ResourcesCompat.getDrawable(getResources(), R.mipmap.wood, null);
        imageResources[EResourceType.MEAT.getValue()] =  ResourcesCompat.getDrawable(getResources(), R.mipmap.meat, null);
    }

    /**
     * - Creates the playground with width and height of the device
     * - Fill ImageView array with random pictures
     * - add OnClickListener du every picture
     * - call recursive function to mark fields
     */
    @SuppressLint("NewApi")
    private void designBoard() {
        for (int i = 0; i < maxY; i++) {
            LinearLayout linRow = new LinearLayout(context);
            for (int j = 0; j < maxX; j++) {
                int resource;

                if(this.app.getUser().getGame() == null || this.app.getUser().getGame().getPlaygroundAsIntArray() == null) {
                    resource = getRandomInt(0, 4);
                } else {
                    resource = this.app.getUser().getGame().getPlaygroundAsIntArray()[i][j];
                }
                ImageView item = initItem(i,j,resource);
                dbPlayground[i][j] = resource;

                final int y = i;
                final int x = j;
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Drawable tempCell1, tempCell2;

                        if (firstClick)
                        {
                            firstMoveX = x;
                            firstMoveY = y;
                            firstClick = false;
                            view.startAnimation(blinkAnimation);
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
                                    saveResourcesToInventory();
                                }while(listOfPoints.size()!=0);

                                listOfPoints.clear();
                            }
                            firstClick = true;
                        }
                    }
                });
                linRow.addView(item);
            }
            linBoardGame.addView(linRow);
        }

        if(this.app.getUser() != null && this.app.getUser().getGame() == null) {
            DBGame newGame = new DBGame();
            newGame.setUser(this.app.getUser());
            newGame.setPlayground(dbPlayground);
            newGame.save();
            Log.i(LOG_TAG, "Spiel gesichert || " + this.app.getUser().getGame().getPlayground() );
        }
    }

    private ImageView initItem(int row, int col, int resource) {
        LinearLayout.LayoutParams itemLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        itemLayout.setMargins(3,3,0,0);

        playground[row][col] = new ImageView(context);
        playground[row][col].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        playground[row][col].setImageDrawable(imageResources[resource]);
        playground[row][col].setBackgroundColor(ContextCompat.getColor(context, R.color.materialBlueGrey800));
        playground[row][col].setLayoutParams(itemLayout);

        return playground[row][col];
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
    public boolean checkMove(int m1Y, int m1X, int m2Y, int m2X) {
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
    public boolean containsPoint(int posY, int posX)
    {
        for(int i = 0; i< listOfPoints.size(); i++)
        {
            ExtendedCoordinate ec = listOfPoints.get(i);
            if((ec.getPointY() == posY) && (ec.getPointX() == posX))
            {
                return true;
            }

        }
        return false;
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
                    listOfPoints.add(new ExtendedCoordinate(new Point(posY - 1, posX),drawable));
                }
                if (!containsPoint(posY, posX)) {
                    listOfPoints.add(new ExtendedCoordinate(new Point(posY, posX),drawable));
                }
                if (!containsPoint(posY + 1, posX)) {
                    listOfPoints.add(new ExtendedCoordinate(new Point(posY + 1, posX),drawable));
                }
            }
        }
        // check horizontal
        if((posX >=1)&&(posX < (maxX-1))) {
            Drawable drawable = getField(posY,posX);
            if ((getField(posY, posX - 1) == drawable) && (getField(posY, posX + 1) == drawable)) {
                if (!containsPoint(posY, posX - 1)) {
                    listOfPoints.add(new ExtendedCoordinate(new Point(posY, posX - 1),drawable));
                }
                if (!containsPoint(posY, posX)) {
                    listOfPoints.add(new ExtendedCoordinate(new Point(posY, posX),drawable));
                }
                if (!containsPoint(posY, posX + 1)) {
                    listOfPoints.add(new ExtendedCoordinate(new Point(posY, posX + 1),drawable));
                }
            }
        }
    }

    /**
     * mark all fields form the list of coordinates with with background
     */
    @SuppressLint("NewApi")
    private void whiteOutBlocks() {
        for (ExtendedCoordinate point : listOfPoints) {
            playground[point.getPointX()][point.getPointY()].setImageDrawable(noneResource);
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

                    if (playground[i][j].getDrawable() == noneResource)
                    {

                        if(i != 0) {
                            playground[i][j].setImageDrawable(playground[i - 1][j].getDrawable());
                            playground[i - 1][j].setImageDrawable(noneResource);
                        }
                        else {
                            int randomResource = getRandomInt(0, 4);
                            playground[i][j].setImageDrawable(imageResources[randomResource]);
                            dbPlayground[i][j] = randomResource;
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
                if(playground[i][j].getDrawable() == noneResource)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *count the collected resources and multiply with calculated value
     * than add the value to values in the database
     */
    private void saveResourcesToInventory()
    {
        if(listOfPoints.size() !=0)
        {
            int collectedPoints = 0;
            int amountOfWood = 0;
            int amountOfMeat = 0;
            int amountOfCotton = 0;
            int amountOfStone = 0;
            int amountOfOre = 0;
            for (ExtendedCoordinate point : listOfPoints) {
                if(point.getDrawable() == imageResources[EResourceType.WOOD.getValue()] )
                {
                    amountOfWood +=1;
                }
                else if(point.getDrawable() == imageResources[EResourceType.COTTON.getValue()] )
                {
                    amountOfCotton +=1;
                }
                else if(point.getDrawable() == imageResources[EResourceType.MEAT.getValue()] )
                {
                    amountOfMeat +=1;
                }
                else if(point.getDrawable() == imageResources[EResourceType.ORE.getValue()] )
                {
                    amountOfOre +=1;
                }
                else if(point.getDrawable() == imageResources[EResourceType.STONE.getValue()] )
                {
                    amountOfStone +=1;
                }
            }

            if(amountOfWood > 0) {
                DBResource wood = this.app.getUser().getInventory().get(EResourceType.WOOD.getValue());
                amountOfWood *= multiplicator(amountOfWood);
                collectedPoints += (amountOfWood * 10);
                wood.setAmount(wood.getAmount() + amountOfWood);
                wood.save();
            }

            if(amountOfCotton > 0) {
                DBResource cotton = this.app.getUser().getInventory().get(EResourceType.COTTON.getValue());
                amountOfCotton *= multiplicator(amountOfCotton);
                collectedPoints += (amountOfCotton * 10);
                cotton.setAmount(cotton.getAmount() + amountOfCotton);
                cotton.save();
            }

            if(amountOfMeat > 0) {
                DBResource meat = this.app.getUser().getInventory().get(EResourceType.MEAT.getValue());
                amountOfMeat *= multiplicator(amountOfMeat);
                collectedPoints += (amountOfMeat * 10);
                meat.setAmount(meat.getAmount() + amountOfMeat);
                meat.save();
            }

            if(amountOfStone > 0) {
                DBResource stone = this.app.getUser().getInventory().get(EResourceType.STONE.getValue());
                amountOfStone *= multiplicator(amountOfStone);
                collectedPoints += (amountOfStone * 10);
                stone.setAmount(stone.getAmount() + amountOfStone);
                stone.save();
            }

            if(amountOfOre > 0) {
                DBResource ore = this.app.getUser().getInventory().get(EResourceType.ORE.getValue());
                amountOfOre *= multiplicator(amountOfOre);
                collectedPoints += (amountOfOre * 10);
                ore.setAmount(ore.getAmount() + amountOfOre);
                ore.save();
            }

            // set Score
            DBGame game = this.app.getUser().getGame();


            int userScore = game.getScore();
            userScore += collectedPoints;
            game.setScore(userScore);
            game.save();
            updateScoreText(userScore);
        }
    }

    /**
     * calculate the multiplicator for the collected item
     * @param amountOf the amount of collected items of a resource
     * @return new amount of items
     */
    private int multiplicator(int amountOf)
    {
        if(amountOf == 3)
        {
            return 1;
        }
        else if(amountOf == 4)
        {
            return 2;
        }
        if(amountOf > 4)
        {
            return 3;
        }
        return 1;
    }

}
