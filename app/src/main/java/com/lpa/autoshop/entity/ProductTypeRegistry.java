package com.lpa.autoshop.entity;

import android.os.Handler;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lpa on 30.05.15.
 */
public class ProductTypeRegistry {
    public static final String PRODUCT_TYPE_ALIAS = "com.lpa.autoshop.PRODUCT_TYPE_ALIAS";
    private static String SERVICE_NAME = "entity.producttype";
    private static ProductTypeRegistry instance;
    private ArrayList<ProductType> productTypes;


    private ProductTypeRegistry (){
        productTypes = new ArrayList<>();
        loadProductTypes();
    }


    public static ProductTypeRegistry getInstance(){
        if (instance == null){
            instance = new ProductTypeRegistry();
        }
        return instance;
    }


    public ArrayList<ProductType> getProductTypes() {
        return productTypes;
    }


    public void addProductType(ProductType productType){
        productTypes.add(productType);
    }


    public void loadProductTypes() {
        try {
            URL url = new URL(Constants.SERVER_URL + SERVICE_NAME);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(inputStream);
                Element productTypesElement = dom.getDocumentElement();
                NodeList nodeList = productTypesElement.getElementsByTagName("productType");
                if (nodeList != null && nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element productTypeElement = (Element) nodeList.item(i);
                        Element aliasElement = (Element) productTypeElement.getElementsByTagName("alias").item(0);
                        Element nameElement = (Element) productTypeElement.getElementsByTagName("name").item(0);
                        final String alias = aliasElement.getFirstChild().getNodeValue();
                        final String name = nameElement.getFirstChild().getNodeValue();
                        Log.v("MainActivity", "alias = " + alias + " name = " + name);
                        addProductType(new ProductType(alias, name));
                    }
                }
                Log.v("ProductTypeRegistry", "HTTP_SUCCESS");
            } else {
                Log.v("ProductTypeRegistry", "HTTP_ERROR");
            }
        } catch (Exception e) {
            Log.v("ProductTypeRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }
}
