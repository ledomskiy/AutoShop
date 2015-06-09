package com.lpa.autoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lpa.autoshop.entity.Product;

import java.util.List;

/**
 * Created by lpa on 10.06.15.
 */
public class ProductItemAdapter extends ArrayAdapter<Product> {
    int resource;

    public ProductItemAdapter (Context context, int resource, List<Product> items){
        super (context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LinearLayout productView;
        Product item = getItem(position);

        if (convertView == null){
            productView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate (resource, productView, true);
        } else {
            productView = (LinearLayout)convertView;
        }

        TextView productItemName = (TextView)productView.findViewById(R.id.product_item_name);
        TextView productItemPrice = (TextView)productView.findViewById(R.id.product_item_price);
        RatingBar productItemRating = (RatingBar)productView.findViewById(R.id.product_item_rating);

        productItemName.setText(item.getName());
        productItemPrice.setText(""+item.getPrice());
        productItemRating.setRating(item.getRating());
        //productItemRating.setEnabled(false);

        return productView;
    }
}
