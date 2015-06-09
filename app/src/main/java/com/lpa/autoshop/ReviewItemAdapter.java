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
import com.lpa.autoshop.entity.Review;

import java.util.List;

/**
 * Created by lpa on 10.06.15.
 */
public class ReviewItemAdapter extends ArrayAdapter<Review> {
    int resource;

    public ReviewItemAdapter(Context context, int resource, List<Review> items){
        super (context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LinearLayout reviewView;
        Review item = getItem(position);

        if (convertView == null){
            reviewView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate (resource, reviewView, true);
        } else {
            reviewView = (LinearLayout)convertView;
        }

        TextView reviewItemName = (TextView)reviewView.findViewById(R.id.review_item_name);
        TextView reviewItemText = (TextView)reviewView.findViewById(R.id.review_item_text);
        RatingBar reviewItemRating = (RatingBar)reviewView.findViewById(R.id.review_item_rating);

        reviewItemName.setText(item.getName());
        reviewItemText.setText(item.getText());
        reviewItemRating.setRating(item.getRating());

        return reviewView;
    }
}
