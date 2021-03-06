package ferdi.david.tim.pme16_crafting_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ApplicationController app;
    private TextView tVUsername;

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

    private Button buttonLogout;
    private View.OnClickListener buttonLogoutOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            app.setUser(null);
            //Intent i = new Intent(MainActivity.this, LoginActivity.class);
            //startActivity(i);
            finish();
        }
    };

    private Button buttonShop;
    private View.OnClickListener buttonShopOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent i = new Intent(MainActivity.this, ShopActivity.class);
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

        app = (ApplicationController) getApplication();
        tVUsername = (TextView) findViewById(R.id.tVUsername);

        buttonStart = (Button)findViewById(R.id.bntStart);
        if(buttonStart != null) {
            buttonStart.setOnClickListener(buttonStartOnClick);
        }

        buttonHighscore = (Button)findViewById(R.id.btnHighscore);
        if(buttonHighscore != null) {
            buttonHighscore.setOnClickListener(buttonHighScoreOnClick);
        }

        buttonShop = (Button)findViewById(R.id.btnShop);
        if(buttonShop != null) {
            buttonShop.setOnClickListener(buttonShopOnClick);
        }

        buttonInventory = (Button)findViewById(R.id.btnInventory);
        if(buttonInventory != null) {
            buttonInventory.setOnClickListener(buttonInventoryOnClick);
        }

        buttonLogout = (Button)findViewById(R.id.btnLogout);
        if(buttonLogout != null) {
            buttonLogout.setOnClickListener(buttonLogoutOnClick);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onUserStateChanged();
    }

    private void onUserStateChanged() {
        if(app.getUser() != null) {
            tVUsername.setText("Benutzer: " + app.getUser().getUsername());
            tVUsername.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
        } else if(tVUsername.getVisibility() != View.INVISIBLE){
            tVUsername.setVisibility(View.INVISIBLE);
            buttonLogout.setVisibility(View.INVISIBLE);
        }
    }

}
