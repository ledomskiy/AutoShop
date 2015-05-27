package com.lpa.autoshop;

import android.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.lpa.autoshop.entity.ProductType;

/**
 * Created by lpa on 26.05.15.
 */
public class ProductTypeListFragment extends ListFragment {
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.v("ProductTypeListFragment", "" + ((ProductType)getListAdapter().getItem(position)).getAlias());
        super.onListItemClick(l, v, position, id);
    }
}
