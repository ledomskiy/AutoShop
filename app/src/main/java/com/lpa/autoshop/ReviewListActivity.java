package com.lpa.autoshop;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lpa.autoshop.entity.Product;
import com.lpa.autoshop.entity.ProductRegistry;
import com.lpa.autoshop.entity.ProductTypeRegistry;
import com.lpa.autoshop.entity.Review;
import com.lpa.autoshop.entity.ReviewRegistry;

import java.util.ArrayList;


public class ReviewListActivity extends ActionBarActivity {
    private int idProduct;

    private ArrayList<Review> reviewArrayList;
    private ReviewItemAdapter reviewArrayAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        reviewArrayList = new ArrayList<>();
        reviewArrayAdapter = new ReviewItemAdapter(this, R.layout.review_item, reviewArrayList);
        idProduct = getIntent().getIntExtra(ProductRegistry.ID_PRODUCT, -1);

        FragmentManager fragmentManager = getFragmentManager();
        ReviewListFragment reviewListFragment = (ReviewListFragment)fragmentManager.findFragmentById(R.id.review_list_fragment);
        reviewListFragment.setListAdapter(reviewArrayAdapter);

        Button reviewAdd = (Button)findViewById(R.id.add_review);
        reviewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewListActivity.this, ReviewAddActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler ();
        refreshReviewList();
    }


    public void refreshReviewList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Review> reviews;
                reviews = ReviewRegistry.getInstance().findProduct(idProduct);
                handler.post(
                    new Runnable(){
                        public void run(){
                            reviewArrayList.clear();
                            for (Review review : reviews){
                                reviewArrayList.add(review);
                            }
                            reviewArrayAdapter.notifyDataSetChanged();
                        }
                    }
                );
            }
        };
        thread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
