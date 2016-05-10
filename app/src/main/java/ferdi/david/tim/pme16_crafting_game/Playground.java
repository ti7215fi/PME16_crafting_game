package ferdi.david.tim.pme16_crafting_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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

public class Playground extends AppCompatActivity {
    final static int maxX = 10;
    final static int maxY = 15;
    private ImageView[][] playgroundCells = new ImageView[maxY][maxX];
    private ImageView[][] tempCells = new ImageView[1][1];
    private Context context;
    private Drawable[] drawCell = new Drawable[6];

    private boolean firstClick = true;
    private int firstMoveX;
    private int firstMoveY;

    private int secMoveX;
    private int secMoveY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_playground);
        context = this;
        loadResources();
        designBoard();
    }
    private void loadResources() {
        drawCell[3] = context.getResources().getDrawable(R.drawable.feuer);
        drawCell[0] = context.getResources().getDrawable(R.drawable.eisen);
        drawCell[1] = context.getResources().getDrawable(R.drawable.holz);
        drawCell[2] = context.getResources().getDrawable(R.drawable.wasser);
        drawCell[4] = context.getResources().getDrawable(R.drawable.fleisch);
    }
    @SuppressLint("NewApi")
    private void designBoard() {
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
                    @Override
                    public void onClick(View v) {
                        tempCells[0][0] = null;
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
                            tempCells[0][0] = playgroundCells[firstMoveY][firstMoveX];
                            playgroundCells[secMoveY][secMoveX].setBackground(tempCells[0][0].getBackground());
                            playgroundCells[firstMoveY][firstMoveX].setBackground(playgroundCells[secMoveY][secMoveX].getBackground());

                            firstClick = true;

                        }
                    }
                });

                linRow.addView(playgroundCells[i][j], lpCell);
            }
            linBoardGame.addView(linRow,lpRow);
        }
    }

    private float ScreenWidth() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    private int getRandomInt(int min, int max)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
