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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    //private static final String SHOP_URL = "http://100.112.39.188:8080/AutoShop/webresources/testpackage.test";
    private static final String SHOP_URL = "http://100.112.39.188:8080/AutoShop/webresources/entity.producttype";

    private ArrayList<ProductType> productTypes;
    private ArrayAdapter<ProductType> productTypesArrayAdapter;


    private Handler handler;
    public void addProductType (ProductType productType){
        productTypes.add(productType);
        productTypesArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        ProductTypeListFragment productTypeListFragment = (ProductTypeListFragment)fm.findFragmentById(R.id.product_type_list_fragment);
        productTypes = new ArrayList<ProductType>();
        productTypesArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_list_item_1, productTypes);
        productTypeListFragment.setListAdapter(productTypesArrayAdapter);


        handler = new Handler ();

        Thread networkThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SHOP_URL);
                    URLConnection urlConnection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;

                    int responseCode = httpConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = httpConnection.getInputStream();

                        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                        parserFactory.setNamespaceAware(true);
                        XmlPullParser parser = parserFactory.newPullParser();
                        parser.setInput(inputStream, null);

                        while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("alias")){
                                // Переходим к значению алиаса
                                parser.next ();
                                final String alias = parser.getText();
                                // Переходим к значению name
                                parser.next();
                                parser.next();
                                parser.next();
                                final String name = parser.getText();

                                handler.post(new Runnable (){
                                    public void run(){
                                        addProductType(new ProductType(alias, name));
                                    }
                                });

                                Log.v ("MainActivity", "alias = " + alias + " name = " + name);

                            }
                            parser.next();
                        }

                        Log.v("MainActivity","HTTP_SUCCESS");
                    }else{
                        Log.v("MainActivity","HTTP_ERROR");
                    }

                }
                catch (Exception e) {
                    Log.v("MainActivity", "EXCEPTION!!! " + e.getMessage());
                }
            }
        };
        networkThread.start();

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
