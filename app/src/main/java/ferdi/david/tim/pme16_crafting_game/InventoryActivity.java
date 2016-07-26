package ferdi.david.tim.pme16_crafting_game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class InventoryActivity extends AppCompatActivity {

    private GridView    inventoryGridView;
    private ApplicationController app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inventory);

        app = (ApplicationController) getApplication();
        inventoryGridView = (GridView)findViewById(R.id.inventoryGridView);
        inventoryGridView.setAdapter(new ImageAdapter(this));
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return app.getItems().length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            ImageView imageView;
            TextView textView;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            Holder holder;

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.inventory_gridview_item , parent, false);

                holder = new Holder();
                holder.textView = (TextView) row.findViewById(R.id.shop_gridviewitem_text);
                holder.imageView = (ImageView) row.findViewById(R.id.shop_gridviewitem_image);

                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

            int amountOfItem = 0;
            if(app.getUser() != null) {
                amountOfItem = app.getUser().getInventoryItem(position).getAmount();
            }
            holder.textView.setText(amountOfItem + " x");
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setImageResource(app.getItems()[position]);

            return row;
        }

    }

}
