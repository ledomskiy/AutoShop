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
 * Created by lpa on 30.05.15.
 */
public class ReviewRegistry {
    public static String ID_REVIEW = "com.lpa.AutoShop.ID_REVIEW";
    private static String SERVICE_NAME = "entity.review";
    private static ReviewRegistry instance;
    private ArrayList<Review> reviews;

    private ReviewRegistry(){
        reviews = new ArrayList<>();
        refreshReviewList();
    }

    public ArrayList<Review> findProduct(int idProduct){
        refreshReviewList();
        ArrayList<Review> result = new ArrayList<>();
        for (Review review : reviews){
            if (review.getIdProduct() == idProduct){
                result.add(review);
            }
        }
        return result;
    }

    private void refreshReviewList(){
        try {
            //TODO плохой подход
            reviews.clear();
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(inputStream);

                Element parentElement = document.getDocumentElement();
                NodeList nodeList = parentElement.getElementsByTagName("review");
                if (nodeList != null && nodeList.getLength() > 0){
                    for (int i = 0; i < nodeList.getLength(); i++){
                        Element productElement = (Element)nodeList.item(i);
                        Element idReviewElement = (Element)productElement.getElementsByTagName("idReview").item(0);
                        Element idProductElement = (Element)productElement.getElementsByTagName("idProduct").item(0);
                        Element nameElement = (Element)productElement.getElementsByTagName("name").item(0);
                        Element textElement = (Element)productElement.getElementsByTagName("text").item(0);
                        Element ratingElement = (Element)productElement.getElementsByTagName("rating").item(0);

                        final int idReview = Integer.parseInt(idReviewElement.getFirstChild().getNodeValue());
                        final int idProduct = Integer.parseInt(idProductElement.getFirstChild().getNodeValue());
                        final String name = nameElement.getFirstChild().getNodeValue();
                        final String text = textElement.getFirstChild().getNodeValue();
                        final float rating = Float.parseFloat(ratingElement.getFirstChild().getNodeValue());

                        Log.v("ReviewRegistry", idReview + " " + idProduct + " " + name + " " + text + " " + rating);

                        addReview(new Review(idReview, idProduct, name, text, rating));
                    }
                }
                Log.v ("ReviewRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("ReviewRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("ProductRegistry", "EXCEPTION!!! " + e.getMessage());
        }
    }

    private void addReview (Review review){
        reviews.add(review);
    }

    public static ReviewRegistry getInstance(){
        if (instance == null){
            instance = new ReviewRegistry();
        }
        return instance;
    }
}
