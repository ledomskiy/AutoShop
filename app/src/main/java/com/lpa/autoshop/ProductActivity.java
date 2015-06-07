package com.lpa.autoshop;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.lpa.autoshop.entity.ProductRegistry;
import com.lpa.autoshop.entity.Warehouse;
import com.lpa.autoshop.entity.WarehouseProductRegistry;
import com.lpa.autoshop.entity.WarehouseRegistry;

import java.util.ArrayList;


public class ProductActivity extends ActionBarActivity {
    private ArrayList<Warehouse> warehouseArrayList;
    private ArrayAdapter<Warehouse> warehouseArrayAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        warehouseArrayList = new ArrayList<>();
        warehouseArrayAdapter = new ArrayAdapter<Warehouse>(this, android.R.layout.simple_list_item_1, warehouseArrayList);

        FragmentManager fragmentManager = getFragmentManager();
        WarehouseListFragment warehouseListFragment = (WarehouseListFragment)fragmentManager.findFragmentById(R.id.warehouse_list_fragment);
        warehouseListFragment.setListAdapter(warehouseArrayAdapter);

        handler = new Handler();
        refreshWarehouseList();
    }

    public void refreshWarehouseList(){
        final int idProduct = getIntent().getIntExtra(ProductRegistry.ID_PRODUCT, 0);
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Warehouse> warehouses = WarehouseProductRegistry.getInstance().findByProduct(idProduct);
                handler.post(
                        new Runnable() {
                            public void run() {
                                warehouseArrayList.clear();
                                for (Warehouse warehouse : warehouses) {
                                    warehouseArrayList.add(warehouse);
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
