package com.lpa.autoshop.entity;

/**
 * Created by lpa on 07.06.15.
 */
public class WarehouseProduct {
    private int idWarehouse;
    private int idProduct;
    private int quantity;
    private double price;

    public WarehouseProduct(int idWarehouse, int idProduct, int quantity, double price) {
        this.idWarehouse = idWarehouse;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
    }

    public int getIdWarehouse() {
        return idWarehouse;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
