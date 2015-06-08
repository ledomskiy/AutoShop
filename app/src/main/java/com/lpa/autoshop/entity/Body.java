package com.lpa.autoshop.entity;

/**
 * Created by lpa on 08.06.15.
 */
public class Body {
    private int idBody;
    private String name;
    private int idModel;

    public Body(int idBody, String name, int idModel) {
        this.idBody = idBody;
        this.name = name;
        this.idModel = idModel;
    }

    public int getIdBody() {
        return idBody;
    }

    public String getName() {
        return name;
    }

    public int getIdModel() {
        return idModel;
    }

    @Override
    public String toString() {
        return name;
    }
}
