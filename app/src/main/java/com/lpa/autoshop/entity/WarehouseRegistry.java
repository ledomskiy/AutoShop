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
public class WarehouseRegistry {
    public static String ID_WAREHOUSE = "com.lpa.AutoShop.ID_WAREHOUSE";
    private static String SERVICE_NAME = "entity.warehouse";
    private static WarehouseRegistry instance;
    private ArrayList<Warehouse> warehouses;

    private WarehouseRegistry(){
        warehouses = new ArrayList<>();
        refreshWarehouseList();
    }


    public Warehouse find(int idWarehouse){
        refreshWarehouseList();
        Warehouse result = null;
        for (Warehouse warehouse : warehouses){
            if (warehouse.getIdWarehouse() == idWarehouse){
                result = warehouse;
                break;
            }
        }
        return result;
    }


    public ArrayList<Warehouse> findAll(){
        refreshWarehouseList();
        return warehouses;
    }


    private void refreshWarehouseList(){
        try {
            //TODO плохой подход
            warehouses.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("warehouse");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element warehouseElement = (Element)nodeList.item(i);
                        Element idWarehouseElement = (Element)warehouseElement.getElementsByTagName("idWarehouse").item(0);
                        Element nameElement = (Element)warehouseElement.getElementsByTagName("name").item(0);
                        Element addressElement = (Element)warehouseElement.getElementsByTagName("address").item(0);
                        Element latitudeElement = (Element)warehouseElement.getElementsByTagName("latitude").item(0);
                        Element longitudeElement = (Element)warehouseElement.getElementsByTagName("longitude").item(0);

                        final int idWarehouse = Integer.parseInt(idWarehouseElement.getFirstChild().getNodeValue());
                        final String name = nameElement.getFirstChild().getNodeValue();
                        final String address = addressElement.getFirstChild().getNodeValue();
                        final double latitude = Double.parseDouble(latitudeElement.getFirstChild().getNodeValue());
                        final double longitude = Double.parseDouble(longitudeElement.getFirstChild().getNodeValue());

                        Log.v("WarehouseRegistry", idWarehouse + " " + name + " " + address + " " + latitude + " " + longitude);

                        addWarehouse(new Warehouse(idWarehouse, name, address, latitude, longitude));
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

    private void addWarehouse (Warehouse warehouse){
        warehouses.add(warehouse);
    }

    public static WarehouseRegistry getInstance(){
        if (instance == null){
            instance = new WarehouseRegistry();
        }
        return instance;
    }
}
