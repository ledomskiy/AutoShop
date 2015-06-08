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
public class BodyRegistry {
    public static String ID_BODY = "com.lpa.AutoShop.ID_BODY";
    private static String SERVICE_NAME = "entity.body";
    private static BodyRegistry instance;
    private ArrayList<Body> bodies;

    private BodyRegistry(){
        bodies = new ArrayList<>();
        refreshBodiesList();
    }

    public ArrayList<Body> findByModel (int idModel){
        ArrayList<Body> result = new ArrayList<>();
        for (Body body : bodies){
            if (body.getIdModel() == idModel){
                result.add(body);
            }
        }
        return result;
    }

    public ArrayList<Body> findByProductType (int idModel, String productTypeAlias){
        ArrayList<Body> result = new ArrayList<>();
        for (Body body : bodies){
            ArrayList<Product> productsBody = BodyProductRegistry.getInstance().findProductByBody(body.getIdBody());
            if (body.getIdModel() == idModel && ProductRegistry.getInstance().containWithType(productsBody, productTypeAlias))
            {
                if (!result.contains(body)){
                    result.add(body);
                }
            }
        }
        return bodies;
    }


    public void refreshBodiesList(){
        try {
            //TODO плохой подход
            bodies.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("body");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element bodyElement = (Element)nodeList.item(i);
                        Element idBodyElement = (Element)bodyElement.getElementsByTagName("idBody").item(0);
                        Element nameElement = (Element)bodyElement.getElementsByTagName("name").item(2);
                        Element idModelElement = (Element)bodyElement.getElementsByTagName("idModel").item(1);

                        final int idBody = Integer.parseInt(idBodyElement.getFirstChild().getNodeValue());
                        final String name = nameElement.getFirstChild().getNodeValue();
                        final int idModel = Integer.parseInt(idModelElement.getFirstChild().getNodeValue());

                        Log.v("BodyRegistry", idBody + " " + name + " " + idModel);

                        addBody(new Body(idBody, name, idModel));
                    }
                }
                Log.v ("BodyRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("BodyRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("BodyRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addBody(Body body){
        bodies.add(body);
    }

    public static BodyRegistry getInstance(){
        if (instance == null){
            instance = new BodyRegistry();
        }
        return instance;
    }
}
