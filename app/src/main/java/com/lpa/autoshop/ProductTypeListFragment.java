package com.lpa.autoshop;

import android.app.ListFragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lpa.autoshop.entity.ProductType;

/**
 * Created by lpa on 26.05.15.
 */
public class ProductTypeListFragment extends ListFragment {
    public static final String PRODUCT_TYPE_ALIAS = "com.lpa.autoshop.PRODUCT_TYPE_ALIAS";
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.v("ProductTypeListFragment", "" + ((ProductType)getListAdapter().getItem(position)).getAlias());

        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(PRODUCT_TYPE_ALIAS, ((ProductType)getListAdapter().getItem(position)).getAlias());
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }
}
