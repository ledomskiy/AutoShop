package com.lpa.autoshop.entity;

/**
 * Created by lpa on 10.06.15.
 */
public class WarehouseProductDescription {
    private WarehouseProduct warehouseProduct;
    private Warehouse warehouse;

    public WarehouseProductDescription(WarehouseProduct warehouseProduct, Warehouse warehouse) {
        this.warehouseProduct = warehouseProduct;
        this.warehouse = warehouse;
    }

    public WarehouseProduct getWarehouseProduct() {
        return warehouseProduct;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public String toString (){
        return warehouse.getName();
    }
}
