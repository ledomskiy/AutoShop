package com.lpa.autoshop;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lpa.autoshop.entity.Product;

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
    public static final String PRODUCT_BY_TYPE_URL = "http://100.112.39.188:8080/AutoShop/webresources/entity.product/type/";
    private String productTypeAlias;

    private ArrayList<Product> productArrayList;
    private ArrayAdapter<Product> productArrayAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Log.v("ProductListActivity", getIntent().getStringExtra(ProductTypeListFragment.PRODUCT_TYPE_ALIAS));
        productTypeAlias = getIntent().getStringExtra(ProductTypeListFragment.PRODUCT_TYPE_ALIAS);
        productArrayList = new ArrayList<>();
        productArrayAdapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, productArrayList);

        FragmentManager fragmentManager = getFragmentManager();
        ProductListFragment productListFragment = (ProductListFragment)fragmentManager.findFragmentById(R.id.product_list_fragment);
        productListFragment.setListAdapter(productArrayAdapter);

        handler = new Handler ();
        refreshProductList();
    }

    private void refreshProductList(){
        Thread networkThread = new Thread (){
            @Override
            public void run(){
                try {
                    URL url = new URL(PRODUCT_BY_TYPE_URL+productTypeAlias);
                    HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
                    int responseCode = httpConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = httpConnection.getInputStream();
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document document = db.parse(inputStream);

                        Element parentElement = document.getDocumentElement();
                        NodeList nodeList = parentElement.getElementsByTagName("product");
                        if (nodeList != null && nodeList.getLength() > 0){
                            for (int i = 0; i < nodeList.getLength(); i++){
                                Element productElement = (Element)nodeList.item(i);
                                Element idProductElement = (Element)productElement.getElementsByTagName("idProduct").item(0);
                                Element nameElement = (Element)productElement.getElementsByTagName("name").item(0);
                                Element productTypeAliasElement = (Element)productElement.getElementsByTagName("productTypeAlias").item(0);
                                Element descriptionElement = (Element)productElement.getElementsByTagName("description").item(0);
                                Element priceElement = (Element)productElement.getElementsByTagName("price").item(0);

                                final int idProduct = Integer.parseInt(idProductElement.getFirstChild().getNodeValue());
                                final String name = nameElement.getFirstChild().getNodeValue();
                                final String productType = productTypeAliasElement.getFirstChild().getNodeValue();
                                final String description = descriptionElement.getFirstChild().getNodeValue();
                                final double price = Double.parseDouble(priceElement.getFirstChild().getNodeValue());

                                Log.v ("ProductListActivity", idProduct + " " + name + " " + productType + " " + description + " " + price);

                                handler.post(
                                    new Runnable(){
                                        public void run(){
                                            addProduct(new Product(idProduct, name, productType, price, description));
                                        }
                                    }
                                );
                                /*
                                <product>
                                   <description>Мегакрутой движок для крузака:)</description>
                                   <idProduct>1</idProduct>
                                   <name>Двигатель для тойоты</name>
                                   <price>100000.0</price>
                                   <productTypeAlias>Engine</productTypeAlias>
                               </product>
                                 */
                            }
                        }


                        Log.v ("HttpUrlConnection", "HTTP_SUCCESS");
                    }else{
                        Log.v ("HttpUrlConnection", "HTTP_ERROR");
                    }
                } catch (Exception e){
                    Log.v ("Exception", "EXCEPTION!!! " + e.getMessage());
                }

            }
        };
        networkThread.start();
    }

    private void addProduct (Product product){
        productArrayList.add(product);
        productArrayAdapter.notifyDataSetChanged();
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
