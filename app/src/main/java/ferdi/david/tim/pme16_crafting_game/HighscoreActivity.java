package ferdi.david.tim.pme16_crafting_game;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    private List<DBGame> games;
    private ListView     highscoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_highscore);

        games = DBGame.findWithQuery(DBGame.class, "SELECT * FROM DB_GAME order by score desc");

        highscoreList = (ListView) findViewById(R.id.lVHighscore);
        highscoreList.setAdapter(new HighscoreAdapter(this));
    }

   public class HighscoreAdapter extends BaseAdapter {
        private Context context;

        public HighscoreAdapter(Context c)
        {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return games.size();
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return games.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            TextView tvName;
            TextView tvScore;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            Holder holder;

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.highscore_item , parent, false);

                holder = new Holder();
                holder.tvName = (TextView) row.findViewById(R.id.tvHighscoreName);
                holder.tvScore = (TextView) row.findViewById(R.id.tvHighscoreScore);

                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }
            int ranking = position + 1;
            holder.tvName.setText(ranking+". " + games.get(position).getUser().getUsername());

            int score = games.get(position).getScore();
            holder.tvScore.setText(""+score);

            return row;
        }

    }


}
