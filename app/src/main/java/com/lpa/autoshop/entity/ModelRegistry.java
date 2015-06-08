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
public class ModelRegistry {
    public static String ID_MODEL = "com.lpa.AutoShop.ID_MODEL";
    private static String SERVICE_NAME = "entity.model";
    private static ModelRegistry instance;
    private ArrayList<Model> models;

    private ModelRegistry(){
        models = new ArrayList<>();
        refreshModelList();
    }

    public ArrayList<Model> findByBrand (int idBrand){
        ArrayList<Model> result = new ArrayList<>();
        for (Model model : models){
            if (model.getIdBrand() == idBrand){
                result.add(model);
            }
        }
        return result;
    }


    public ArrayList<Product> findProduct (int idModel){
        ArrayList<Product> result = new ArrayList<>();
        for (Body body : BodyRegistry.getInstance().findByModel(idModel)){
            ArrayList<Product> productsBody = BodyProductRegistry.getInstance().findProductByBody(body.getIdBody());
            for (Product product : productsBody){
                if (!result.contains(product)){
                    result.add(product);
                }
            }
        }
        return result;
    }


    public ArrayList<Model> findByProductType (int idBrand, String productTypeAlias){
        ArrayList<Model> result = new ArrayList<>();
        for (Model model : models){
            ArrayList<Product> productsModel = findProduct(model.getIdModel());
            if (model.getIdBrand() == idBrand && ProductRegistry.getInstance().containWithType(productsModel, productTypeAlias))
            {
                if (!result.contains(model)){
                    result.add(model);
                }
            }
        }
        return models;
    }


    public void refreshModelList(){
        try {
            //TODO плохой подход
            models.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("model");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element modelElement = (Element)nodeList.item(i);
                        Element idModelElement = (Element)modelElement.getElementsByTagName("idModel").item(0);
                        Element nameElement = (Element)modelElement.getElementsByTagName("name").item(1);
                        Element idBrandElement = (Element)modelElement.getElementsByTagName("idBrand").item(1);

                        final int idModel = Integer.parseInt(idModelElement.getFirstChild().getNodeValue());
                        final String name = nameElement.getFirstChild().getNodeValue();
                        final int idBrand = Integer.parseInt(idBrandElement.getFirstChild().getNodeValue());

                        Log.v("ModelRegistry", idModel + " " + name + " " + idBrand);

                        addModel(new Model(idModel, name, idBrand));
                    }
                }
                Log.v ("ModelRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("ModelRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("ModelRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addModel(Model model){
        models.add(model);
    }

    public static ModelRegistry getInstance(){
        if (instance == null){
            instance = new ModelRegistry();
        }
        return instance;
    }
}
