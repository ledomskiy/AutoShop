package com.lpa.autoshop;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lpa.autoshop.entity.Product;
import com.lpa.autoshop.entity.ProductRegistry;
import com.lpa.autoshop.entity.ProductTypeRegistry;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import android.os.Handler;
import android.widget.ArrayAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ProductListActivity extends ActionBarActivity {
    private String productTypeAlias;

    private ArrayList<Product> productArrayList;
    private ProductItemAdapter productArrayAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Log.v("ProductListActivity", getIntent().getStringExtra(ProductTypeRegistry.PRODUCT_TYPE_ALIAS));
        productArrayList = new ArrayList<>();
        productArrayAdapter = new ProductItemAdapter(this, R.layout.product_item, productArrayList);
        productTypeAlias = getIntent().getStringExtra(ProductTypeRegistry.PRODUCT_TYPE_ALIAS);

        FragmentManager fragmentManager = getFragmentManager();
        ProductListFragment productListFragment = (ProductListFragment)fragmentManager.findFragmentById(R.id.product_list_fragment);
        productListFragment.setListAdapter(productArrayAdapter);

        handler = new Handler ();
        refreshProductList();
    }


    public void refreshProductList(){
        final String typeAlias = productTypeAlias;
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Product> products = ProductRegistry.getInstance().findByType(typeAlias);
                handler.post(
                    new Runnable(){
                        public void run(){
                            productArrayList.clear();
                            for (Product product : products){
                                productArrayList.add(product);
                            }
                            productArrayAdapter.notifyDataSetChanged();
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
