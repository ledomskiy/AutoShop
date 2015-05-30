package com.lpa.autoshop;

import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lpa.autoshop.entity.ProductType;
import com.lpa.autoshop.entity.ProductTypeRegistry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {
    private ArrayList<ProductType> productTypeArrayList;
    private ArrayAdapter<ProductType> productTypesArrayAdapter;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        ProductTypeListFragment productTypeListFragment = (ProductTypeListFragment)fm.findFragmentById(R.id.product_type_list_fragment);
        productTypeArrayList = new ArrayList<>();
        productTypesArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_list_item_1, productTypeArrayList);
        productTypeListFragment.setListAdapter(productTypesArrayAdapter);
        handler = new Handler();
        refresProductTypeList();
    }

    public void refresProductTypeList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<ProductType> productTypes = ProductTypeRegistry.getInstance().getProductTypes();
                handler.post(
                    new Runnable(){
                        public void run(){
                            productTypeArrayList.clear();
                            for (ProductType productType : productTypes){
                                productTypeArrayList.add(productType);
                            }
                            productTypesArrayAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
