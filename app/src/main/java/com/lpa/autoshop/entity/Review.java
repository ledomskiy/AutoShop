package com.lpa.autoshop.entity;

/**
 * Created by lpa on 10.06.15.
 */
public class Review {
    private int idReview;
    private int idProduct;
    private String name;
    private String text;
    private float rating;

    public Review(int idReview, int idProduct, String name, String text, float rating) {
        this.idReview = idReview;
        this.idProduct = idProduct;
        this.name = name;
        this.text = text;
        this.rating = rating;
    }

    public int getIdReview() {
        return idReview;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public float getRating() {
        return rating;
    }
}
