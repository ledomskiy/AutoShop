package com.lpa.autoshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lpa.autoshop.entity.Product;
import com.lpa.autoshop.entity.WarehouseProductDescription;

import java.util.List;

/**
 * Created by lpa on 10.06.15.
 */
public class WarehouseProductItemAdapter extends ArrayAdapter<WarehouseProductDescription> {
    int resource;

    public WarehouseProductItemAdapter(Context context, int resource, List<WarehouseProductDescription> items){
        super (context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LinearLayout warehouseProductView;
        WarehouseProductDescription item = getItem(position);

        if (convertView == null){
            warehouseProductView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate (resource, warehouseProductView, true);
        } else {
            warehouseProductView = (LinearLayout)convertView;
        }

        TextView nameItem = (TextView)warehouseProductView.findViewById(R.id.warehouse_name_item);
        TextView addressItem = (TextView)warehouseProductView.findViewById(R.id.warehouse_address_item);
        TextView priceItem = (TextView)warehouseProductView.findViewById(R.id.warehouse_price_item);
        TextView quantityItem = (TextView)warehouseProductView.findViewById(R.id.warehouse_quantity_item);


        nameItem.setText(item.getWarehouse().getName());
        addressItem.setText(item.getWarehouse().getAddress());
        priceItem.setText(item.getWarehouseProduct().getPrice()+"");
        quantityItem.setText(item.getWarehouseProduct().getQuantity()+"");
        //productItemRating.setEnabled(false);

        return warehouseProductView;
    }
}
