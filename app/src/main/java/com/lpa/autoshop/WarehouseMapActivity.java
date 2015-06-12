package com.lpa.autoshop;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lpa.autoshop.entity.ProductRegistry;
import com.lpa.autoshop.entity.Warehouse;
import com.lpa.autoshop.entity.WarehouseProductRegistry;


public class WarehouseMapActivity extends ActionBarActivity {

    private GoogleMap googleMap;
    private int idProduct;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        idProduct = getIntent().getIntExtra(ProductRegistry.ID_PRODUCT, -1);
        handler = new Handler ();

        setUpMapIfNeeded ();

    }

    private void setUpMapIfNeeded() {
        if (googleMap != null) {
            return;
        }
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view)).getMap();
        if (googleMap != null) {
            LatLng barnaulCenter = new LatLng(53.3486092, 83.7556488);
            CameraUpdate center = CameraUpdateFactory.newLatLng(barnaulCenter);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            Log.v("WarehouseMapActivity", "setUpMapIfNeeded");
            Thread thread = new Thread (){
                @Override
                public void run (){
                    for (Warehouse warehouse : WarehouseProductRegistry.getInstance().findByProduct(idProduct)){
                        final Warehouse warehouseFinal = warehouse;
                        Log.v("WarehouseMapActivity", "add marker " + warehouse.getLatitude() + " " + warehouse.getLongitude());
                        handler.post(
                                new Runnable (){
                                    public void run (){
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(warehouseFinal.getLatitude(), warehouseFinal.getLongitude())).title(warehouseFinal.getName()));
                                    }
                                }
                        );
                    }
                }
            };
            thread.start();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
