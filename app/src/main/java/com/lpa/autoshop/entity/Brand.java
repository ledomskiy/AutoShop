package com.lpa.autoshop.entity;

/**
 * Created by lpa on 09.06.15.
 */
public class Brand {
    private int idBrand;
    private String name;

    public Brand(int idBrand, String name) {
        this.idBrand = idBrand;
        this.name = name;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name;
    }
}
