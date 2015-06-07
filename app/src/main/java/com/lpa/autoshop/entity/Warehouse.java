package com.lpa.autoshop.entity;

/**
 * Created by lpa on 07.06.15.
 */
public class Warehouse {
    private int idWarehouse;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public Warehouse(int idWarehouse, String name, String address, double latitude, double longitude) {
        this.idWarehouse = idWarehouse;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIdWarehouse() {
        return idWarehouse;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
