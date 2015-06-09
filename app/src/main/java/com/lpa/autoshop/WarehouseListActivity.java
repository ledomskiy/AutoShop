package com.lpa.autoshop;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lpa.autoshop.entity.ProductRegistry;
import com.lpa.autoshop.entity.Warehouse;
import com.lpa.autoshop.entity.WarehouseProductDescription;
import com.lpa.autoshop.entity.WarehouseProductRegistry;

import java.util.ArrayList;


public class WarehouseListActivity extends ActionBarActivity {
    private int idProduct;
    private ArrayList<WarehouseProductDescription> warehouseArrayList;
    private WarehouseProductItemAdapter warehouseArrayAdapter;
    private Handler handler;

    private Button showOnMapButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_list);

        idProduct = getIntent().getIntExtra(ProductRegistry.ID_PRODUCT, -1);

        warehouseArrayList = new ArrayList<>();
        warehouseArrayAdapter = new WarehouseProductItemAdapter(this, R.layout.warehouse_product_item, warehouseArrayList);

        FragmentManager fragmentManager = getFragmentManager();
        WarehouseListFragment warehouseListFragment = (WarehouseListFragment)fragmentManager.findFragmentById(R.id.warehouse_list_fragment);
        warehouseListFragment.setListAdapter(warehouseArrayAdapter);

        handler = new Handler();
        refreshWarehouseList();

        showOnMapButton = (Button)findViewById(R.id.show_on_map);
        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehouseListActivity.this, WarehouseMapActivity.class);
                startActivity(intent);
            }
        });
    }

    public void refreshWarehouseList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Warehouse> warehouses = WarehouseProductRegistry.getInstance().findByProduct(idProduct);
                handler.post(
                        new Runnable() {
                            public void run() {
                                warehouseArrayList.clear();
                                for (Warehouse warehouse : warehouses) {
                                    warehouseArrayList.add(new WarehouseProductDescription(
                                            WarehouseProductRegistry.getInstance().find(idProduct, warehouse.getIdWarehouse()), warehouse));
                                }
                                warehouseArrayAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.menu_warehouse_list, menu);
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
