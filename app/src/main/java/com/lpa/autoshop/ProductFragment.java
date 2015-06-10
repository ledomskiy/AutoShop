package com.lpa.autoshop;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lpa.autoshop.entity.EAN13CodeBuilder;
import com.lpa.autoshop.entity.Product;
import com.lpa.autoshop.entity.ProductRegistry;

/**
 * Created by lpa on 30.05.15.
 */
public class ProductFragment extends Fragment{
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView productCodeTextView;
    private int idProduct;
    private Product product;
    Handler handler;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        idProduct = activity.getIntent().getIntExtra(ProductRegistry.ID_PRODUCT, 0);
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container);

        productNameTextView=(TextView)view.findViewById(R.id.product_name);
        productDescriptionTextView=(TextView)view.findViewById(R.id.product_description);
        productCodeTextView=(TextView)view.findViewById(R.id.product_code);

        loadProduct();

        return view;
    }

    private void loadProduct(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final Product product = ProductRegistry.getInstance().find(idProduct);
                handler.post(
                    new Runnable(){
                        public void run(){
                            setProduct(product);
                        }
                    }
                );
            }
        };
        thread.start();
    }

    private void setProduct(Product product){
        this.product = product;
        refreshUI();
    }

    private void refreshUI(){
        productNameTextView.setText(product.getName());
        productDescriptionTextView.setText(product.getDescription());


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                "EanP72Tt.ttf");
        productCodeTextView.setTypeface(font);

        // generate barcode string
        EAN13CodeBuilder bb = new EAN13CodeBuilder(product.getCode());
        productCodeTextView.setText(bb.getCode());
    }
}
