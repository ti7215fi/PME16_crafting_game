package ferdi.david.tim.pme16_crafting_game;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private GridView                shopGridView;
    private ApplicationController   app;
    private List<DBResource>        items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        app = (ApplicationController) getApplication();
        items = app.getUser().getItemsByProductionChainLevel(1);

        shopGridView = (GridView) findViewById(R.id.shopGridView);
        shopGridView.setAdapter(new ImageAdapter(this));
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return items.size();
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            ImageView itemImageView;
            ImageView materialImageView;
            TextView textView;
        }

        //---returns an ImageView view---
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            Holder holder;

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(R.layout.shop_item , parent, false);

                holder = new Holder();
                holder.textView = (TextView) row.findViewById(R.id.shop_gridviewitem_text);
                holder.itemImageView = (ImageView) row.findViewById(R.id.shop_gridviewitem_image);
                holder.materialImageView = (ImageView) row.findViewById(R.id.shop_imageView);

                row.setTag(holder);

            } else {
                holder = (Holder) row.getTag();
            }

            int amountOfItem = 0;
            if(items.size() > 0) {
                amountOfItem = items.get(position).getMaterialCosts();
            }
            holder.textView.setText(amountOfItem + " x");

            holder.itemImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.itemImageView.setImageResource(app.getItems()[items.get(position).getType()]);
            holder.itemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBResource item = app.getUser().getInventoryItem(items.get(position).getType());
                    DBResource material = app.getUser().getInventoryItem(items.get(position).getMaterialType());
                    int budget = material.getAmount() - item.getMaterialCosts();
                    String message = "";

                    if(budget >= 0) {
                        item.setAmount(item.getAmount() + 1);
                        item.save();

                        material.setAmount(budget);
                        material.save();

                        message = "Kauf erfolgreich!";
                    } else {
                        message = "Nicht genügend Materialien!";
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });

            holder.materialImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.materialImageView.setImageResource(app.getItems()[items.get(position).getMaterialType()]);

            return row;
        }

    }
}
