package com.lpa.autoshop;

import android.app.ListFragment;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.lpa.autoshop.entity.Product;
import com.lpa.autoshop.entity.ProductRegistry;

/**
 * Created by lpa on 28.05.15.
 */
public class ProductListFragment extends ListFragment {
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Intent intent = new Intent (getActivity(), ProductActivity.class);
        intent.putExtra(ProductRegistry.ID_PRODUCT, ((Product)getListAdapter().getItem(position)).getIdProduct());
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }
}
