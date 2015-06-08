package com.lpa.autoshop.entity;

/**
 * Created by lpa on 08.06.15.
 */
public class BodyProduct {
    private int idBody;
    private int idProduct;

    public BodyProduct(int idBody, int idProduct) {
        this.idBody = idBody;
        this.idProduct = idProduct;
    }

    public int getIdBody() {
        return idBody;
    }

    public int getIdProduct() {
        return idProduct;
    }
}
