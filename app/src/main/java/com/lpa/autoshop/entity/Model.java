package com.lpa.autoshop.entity;

/**
 * Created by lpa on 08.06.15.
 */
public class Model {
    private int idModel;
    private String name;
    private int idBrand;

    public Model(int idModel, String name, int idBrand) {
        this.idModel = idModel;
        this.name = name;
        this.idBrand = idBrand;
    }

    public int getIdModel() {
        return idModel;
    }

    public String getName() {
        return name;
    }

    public int getIdBrand() {
        return idBrand;
    }

    @Override
    public String toString(){
        return name;
    }
}
