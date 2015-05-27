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
    //private static final String SHOP_URL = "http://100.112.39.188:8080/AutoShop/webresources/testpackage.test";
    private static final String SHOP_URL = "http://100.112.39.188:8080/AutoShop/webresources/entity.producttype";

    private ArrayList<ProductType> productTypes;
    private ArrayAdapter<ProductType> productTypesArrayAdapter;


    private Handler handler;

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

        refreshProductTypeList();

    }

    private void refreshProductTypeList() {
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

                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();

                        Document dom = db.parse(inputStream);
                        Element productTypesElement = dom.getDocumentElement();
                        NodeList nodeList = productTypesElement.getElementsByTagName("productType");
                        if (nodeList != null && nodeList.getLength() > 0){
                            for (int i = 0; i < nodeList.getLength(); i++){
                                Element productTypeElement = (Element)nodeList.item(i);
                                Element aliasElement = (Element)productTypeElement.getElementsByTagName("alias").item(0);
                                Element nameElement = (Element)productTypeElement.getElementsByTagName("name").item(0);
                                final String alias = aliasElement.getFirstChild().getNodeValue();
                                final String name = nameElement.getFirstChild().getNodeValue();
                                Log.v("MainActivity", "alias = " + alias + " name = " + name);
                                handler.post (
                                    new Runnable(){
                                        public void run(){
                                            addProductType(new ProductType(alias, name));
                                        }
                                    }
                                );
                            }
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

    public void addProductType (ProductType productType){
        productTypes.add(productType);
        productTypesArrayAdapter.notifyDataSetChanged();
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
