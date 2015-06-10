package com.lpa.autoshop;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.lpa.autoshop.entity.ProductRegistry;
import com.lpa.autoshop.entity.Warehouse;
import com.lpa.autoshop.entity.WarehouseProductRegistry;
import com.lpa.autoshop.entity.WarehouseRegistry;
import com.onbarcode.barcode.EAN13;
import com.onbarcode.barcode.IBarcode;

import java.util.ArrayList;


public class ProductActivity extends ActionBarActivity {
    private int idProduct;
    private Button findInWarehouses;
    private Button showReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        idProduct = getIntent().getIntExtra(ProductRegistry.ID_PRODUCT, -1);

        findInWarehouses = (Button)findViewById(R.id.find_in_warehouse);
        findInWarehouses.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (ProductActivity.this, WarehouseListActivity.class);
                        intent.putExtra(ProductRegistry.ID_PRODUCT, idProduct);
                        startActivity(intent);
                    }
                }
        );

        showReview = (Button)findViewById(R.id.show_review);
        showReview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (ProductActivity.this, ReviewListActivity.class);
                        intent.putExtra(ProductRegistry.ID_PRODUCT, idProduct);
                        startActivity(intent);
                    }
                }
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
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
