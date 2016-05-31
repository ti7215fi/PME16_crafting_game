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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InventoryActivity extends AppCompatActivity {

    private GridView    inventoryGridView;
    private int         items[] = {R.mipmap.wood, R.mipmap.stone, R.mipmap.ore, R.mipmap.cotton} ;
    private float       height;
    private float       width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inventory);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

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
            return items.length;
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
            int cellSize = (int) width / (inventoryGridView.getNumColumns() * 2);
            int cellMarginLeft = cellSize / 2;
            View row = convertView;
            Holder holder;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(cellSize, cellSize);
            layoutParams.setMargins(cellMarginLeft,0,0,0);

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.inventory_gridview_item , parent, false);

                holder = new Holder();
                holder.textView = (TextView) row.findViewById(R.id.inventory_gridviewitem_text);
                holder.imageView = (ImageView) row.findViewById(R.id.inventory_gridviewitem_image);

                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }
            holder.textView.setText("0x");
            holder.imageView.setLayoutParams(layoutParams);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setImageResource(items[position]);

            return row;
        }

    }

}
