package com.lpa.autoshop.entity;

/**
 * Created by lpa on 26.05.15.
 */
public class ProductType {
    private String alias;
    private String name;

    public ProductType (String alias, String name){
        this.alias = alias;
        this.name = name;
    }

    public String getAlias (){
        return alias;
    }

    public String getName (){
        return name;
    }

    public void setAlias (String alias){
        this.alias = alias;
    }

    public void setName (String name){
        this.name = name;
    }

    @Override
    public String toString (){
        return name;
    }
}
