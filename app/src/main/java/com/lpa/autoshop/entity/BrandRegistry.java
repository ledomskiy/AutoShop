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
public class BrandRegistry {
    public static String ID_MODEL = "com.lpa.AutoShop.ID_BRAND";
    private static String SERVICE_NAME = "entity.brand";
    private static BrandRegistry instance;
    private ArrayList<Brand> brands;

    private BrandRegistry(){
        brands = new ArrayList<>();
        refreshBrandList();
    }

    public ArrayList<Product> findProduct (int idBrand){
        ArrayList<Product> result = new ArrayList<>();
        for (Model model : ModelRegistry.getInstance().findByBrand(idBrand)){
            ArrayList<Product> productsModel = ModelRegistry.getInstance().findProduct(model.getIdModel());
            for (Product product : productsModel){
                if (!result.contains(product)){
                    result.add(product);
                }
            }
        }
        return result;
    }

    public ArrayList<Brand> findByProductType (String productTypeAlias){
        ArrayList<Brand> result = new ArrayList<>();
        for (Brand brand : brands){
            ArrayList<Product> productsBrand = findProduct(brand.getIdBrand());
            if (ProductRegistry.getInstance().containWithType(productsBrand, productTypeAlias)){
                if (!result.contains(brand)){
                    result.add(brand);
                }
            }
        }
        return brands;
    }


    public void refreshBrandList(){
        try {
            //TODO плохой подход
            brands.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("brand");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element brandElement = (Element)nodeList.item(i);
                        Element idBrandElement = (Element)brandElement.getElementsByTagName("idBrand").item(0);
                        Element nameElement = (Element)brandElement.getElementsByTagName("name").item(0);

                        final int idBrand = Integer.parseInt(idBrandElement.getFirstChild().getNodeValue());
                        final String name = nameElement.getFirstChild().getNodeValue();

                        Log.v("BrandRegistry", idBrand + " " + name + " " + idBrand);

                        addBrand(new Brand(idBrand, name));
                    }
                }
                Log.v ("BrandRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("BrandRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("BrandRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addBrand(Brand brand){
        brands.add(brand);
    }

    public static BrandRegistry getInstance(){
        if (instance == null){
            instance = new BrandRegistry();
        }
        return instance;
    }
}
