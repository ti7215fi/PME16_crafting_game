package ferdi.david.tim.pme16_crafting_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button buttonStart;
    private View.OnClickListener buttonStartOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            startActivity(i);
        }
    };

    private Button buttonHighscore;
    private View.OnClickListener buttonHighScoreOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent i = new Intent(MainActivity.this, HighscoreActivity.class);
            startActivity(i);
        }
    };

    private Button buttonLogin;
    private View.OnClickListener buttonLoginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    };

    private Button buttonInventory;
    private View.OnClickListener buttonInventoryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent i = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        buttonStart = (Button)findViewById(R.id.bntStart);
        if(buttonStart != null) {
            buttonStart.setOnClickListener(buttonStartOnClick);
        }

        buttonHighscore = (Button)findViewById(R.id.btnHighscore);
        if(buttonHighscore != null) {
            buttonHighscore.setOnClickListener(buttonHighScoreOnClick);
        }

        buttonInventory = (Button)findViewById(R.id.btnInventory);
        if(buttonInventory != null) {
            buttonInventory.setOnClickListener(buttonInventoryOnClick);
        }

        buttonLogin = (Button)findViewById(R.id.btnLogin);
        if(buttonLogin != null) {
            buttonLogin.setOnClickListener(buttonLoginOnClick);
        }
    }

}
