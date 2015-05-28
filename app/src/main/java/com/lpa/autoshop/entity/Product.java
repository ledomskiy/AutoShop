package com.lpa.autoshop.entity;

/**
 * Created by lpa on 28.05.15.
 */
public class Product {
    /*<product>
           <description>Мегакрутой движок для крузака:)</description>
           <idProduct>1</idProduct>
           <name>Двигатель для тойоты</name>
           <price>100000.0</price>
           <productTypeAlias>Engine</productTypeAlias>
       </product> */
    private int idProduct;
    private String name;
    private String productTypeAlias;
    private double price;
    private String description;

    public Product(int idProduct, String name, String productTypeAlias, double price, String description) {
        this.idProduct = idProduct;
        this.name = name;
        this.productTypeAlias = productTypeAlias;
        this.price = price;
        this.description = description;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductTypeAlias() {
        return productTypeAlias;
    }

    public void setProductTypeAlias(String productTypeAlias) {
        this.productTypeAlias = productTypeAlias;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return name + " : " + price;
    }
}
