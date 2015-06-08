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
public class BodyProductRegistry {
    private static String SERVICE_NAME = "entity.bodyproduct";
    private static BodyProductRegistry instance;
    private ArrayList<BodyProduct> bodyProducts;

    private BodyProductRegistry(){
        bodyProducts = new ArrayList<>();
        refreshBodyProductList();
    }

    private ArrayList<BodyProduct> findAll (){
        refreshBodyProductList();
        return bodyProducts;
    }

    public ArrayList<Product> findProductByBody (int idBody){
        ArrayList<Product> result = new ArrayList<>();
        for (BodyProduct bodyProduct : bodyProducts){
            if (bodyProduct.getIdBody() == idBody){
                result.add(ProductRegistry.getInstance().find(bodyProduct.getIdProduct()));
            }
        }
        return result;
    }

    public void refreshBodyProductList(){
        try {
            //TODO плохой подход
            bodyProducts.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("bodyProduct");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element bodyProductElement = (Element)nodeList.item(i);
                        Element idBodyElement = (Element)((Element)bodyProductElement.getElementsByTagName("bodyProductPK").item(0)).getElementsByTagName("idBody").item(0);
                        Element idProductElement = (Element)((Element)bodyProductElement.getElementsByTagName("bodyProductPK").item(0)).getElementsByTagName("idProduct").item(0);

                        final int idBody = Integer.parseInt(idBodyElement.getFirstChild().getNodeValue());
                        final int idProduct = Integer.parseInt(idProductElement.getFirstChild().getNodeValue());

                        Log.v("BodyProductRegistry", idBody + " " + idProduct);

                        addBodyProduct(new BodyProduct(idBody, idProduct));
                    }
                }
                Log.v ("BodyProductRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("BodyProductRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("BodyProductRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addBodyProduct (BodyProduct bodyProduct){
        bodyProducts.add(bodyProduct);
    }

    public static BodyProductRegistry getInstance(){
        if (instance == null){
            instance = new BodyProductRegistry();
        }
        return instance;
    }
}
