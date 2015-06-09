package com.lpa.autoshop.entity;

import android.os.Handler;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lpa on 30.05.15.
 */
public class ProductRegistry {
    public static String ID_PRODUCT = "com.lpa.AutoShop.ID_PRODUCT";
    private static String SERVICE_NAME = "entity.product";
    private static ProductRegistry instance;
    private ArrayList<Product> products;

    private ProductRegistry(){
        products = new ArrayList<>();
        refreshProductList();
    }


    public Product find(int idProduct){
        refreshProductList();
        Product result = null;
        for (Product product : products){
            if (product.getIdProduct() == idProduct){
                result = product;
                break;
            }
        }
        return result;
    }


    public ArrayList<Product> findAll(){
        refreshProductList();
        return products;
    }


    public ArrayList<Product> findByType(String productTypeAlias){
        refreshProductList();
        Log.v("ProductRegistry", "findByType");
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : products){
            if (product.getProductTypeAlias().equals(productTypeAlias)){
                result.add(product);
            }
        }
        return result;
    }

    public boolean containWithType (ArrayList<Product> productList, String productTypeAlias){
        for (Product product : productList){
            if (product.getProductTypeAlias().equals(productTypeAlias)){
                return true;
            }
        }
        return false;
    }


    private void refreshProductList(){
        try {
            //TODO плохой подход
            products.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
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
                        Element ratingElement = (Element)productElement.getElementsByTagName("rating").item(0);

                        final int idProduct = Integer.parseInt(idProductElement.getFirstChild().getNodeValue());
                        final String name = nameElement.getFirstChild().getNodeValue();
                        final String productType = productTypeAliasElement.getFirstChild().getNodeValue();
                        final String description = descriptionElement.getFirstChild().getNodeValue();
                        final double price = Double.parseDouble(priceElement.getFirstChild().getNodeValue());
                        final float rating = Float.parseFloat(ratingElement.getFirstChild().getNodeValue());

                        Log.v("ProductRegistry", idProduct + " " + name + " " + productType + " " + description + " " + price + " " + rating);

                        addProduct(new Product(idProduct, name, productType, price, description, rating));
                    }
                }
                Log.v ("ProductRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("ProductRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("ProductRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addProduct (Product product){
        products.add(product);
    }

    public static ProductRegistry getInstance(){
        if (instance == null){
            instance = new ProductRegistry();
        }
        return instance;
    }
}
