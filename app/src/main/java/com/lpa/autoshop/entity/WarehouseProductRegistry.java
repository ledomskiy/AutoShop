package com.lpa.autoshop.entity;

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
 * Created by lpa on 07.06.15.
 */
public class WarehouseProductRegistry {
    private static String SERVICE_NAME = "entity.warehouseproduct";
    private static WarehouseProductRegistry instance;
    private ArrayList<WarehouseProduct> warehouseProducts;

    private WarehouseProductRegistry(){
        warehouseProducts = new ArrayList<>();
        refreshWarehouseProductList();
    }


    public ArrayList<Warehouse> findByProduct(int idProduct){
        refreshWarehouseProductList();
        ArrayList <Warehouse> result = new ArrayList<>();
        for (WarehouseProduct warehouseProduct : warehouseProducts){
            if (warehouseProduct.getIdProduct() == idProduct &&
                warehouseProduct.getQuantity() > 0)
            {
                result.add(WarehouseRegistry.getInstance().find(warehouseProduct.getIdWarehouse()));
            }
        }
        return result;
    }


    private void refreshWarehouseProductList(){
        try {
            //TODO плохой подход
            warehouseProducts.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("warehouseProduct");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element warehouseProductElement = (Element)nodeList.item(i);
                        Element idWarehouseElement = (Element)((Element)warehouseProductElement.getElementsByTagName("warehouse").item(0)).getElementsByTagName("idWarehouse").item(0);
                        Element idProductElement = (Element)((Element)warehouseProductElement.getElementsByTagName("product").item(0)).getElementsByTagName("idProduct").item(0);
                        Element priceElement = (Element)warehouseProductElement.getElementsByTagName("price").item(0);
                        Element quantityElement = (Element)warehouseProductElement.getElementsByTagName("quantity").item(0);

                        final int idWarehouse = Integer.parseInt(idWarehouseElement.getFirstChild().getNodeValue());
                        final int idProduct = Integer.parseInt(idProductElement.getFirstChild().getNodeValue());
                        final int quantity = Integer.parseInt(quantityElement.getFirstChild().getNodeValue());
                        final double price = Double.parseDouble(priceElement.getFirstChild().getNodeValue());

                        Log.v("WarehouseRegistry", idWarehouse + " " + idProduct + " " + quantity + " " + price);

                        addWarehouseProduct(new WarehouseProduct (idWarehouse, idProduct, quantity, price));
                    }
                }
                Log.v ("WarehouseRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("WarehouseRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("WarehouseRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addWarehouseProduct (WarehouseProduct warehouseProduct){
        warehouseProducts.add(warehouseProduct);
    }

    public static WarehouseProductRegistry getInstance(){
        if (instance == null){
            instance = new WarehouseProductRegistry();
        }
        return instance;
    }
}
