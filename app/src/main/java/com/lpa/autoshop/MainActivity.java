package com.lpa.autoshop;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lpa.autoshop.entity.Body;
import com.lpa.autoshop.entity.BodyProductRegistry;
import com.lpa.autoshop.entity.BodyRegistry;
import com.lpa.autoshop.entity.Brand;
import com.lpa.autoshop.entity.BrandRegistry;
import com.lpa.autoshop.entity.Model;
import com.lpa.autoshop.entity.ModelRegistry;
import com.lpa.autoshop.entity.ProductType;
import com.lpa.autoshop.entity.ProductTypeRegistry;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private Spinner productTypeSpinner;
    private ArrayList<ProductType> productTypeArrayList;
    private ArrayAdapter<ProductType> productTypesArrayAdapter;

    private Spinner brandSpinner;
    private ArrayList<Brand> brandArrayList;
    private ArrayAdapter<Brand> brandArrayAdapter;

    private Spinner modelSpinner;
    private ArrayList<Model> modelArrayList;
    private ArrayAdapter<Model> modelArrayAdapter;

    private Spinner bodySpinner;
    private ArrayList<Body> bodyArrayList;
    private ArrayAdapter<Body> bodyArrayAdapter;

    private ProductType selectedProductType;
    private Brand selectedBrand;
    private Model selectedModel;
    private Body selectedBody;

    private Button findButton;

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        FragmentManager fm = getFragmentManager();
        ProductTypeListFragment productTypeListFragment = (ProductTypeListFragment)fm.findFragmentById(R.id.product_type_list_fragment);
        productTypeArrayList = new ArrayList<>();
        productTypesArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_list_item_1, productTypeArrayList);
        productTypeListFragment.setListAdapter(productTypesArrayAdapter);
        */

        productTypeSpinner = (Spinner)findViewById(R.id.product_type_spinner);
        productTypeArrayList = new ArrayList<>();
        productTypesArrayAdapter = new ArrayAdapter<ProductType>(this, android.R.layout.simple_spinner_item, productTypeArrayList);
        productTypesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeSpinner.setAdapter(productTypesArrayAdapter);

        productTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProductType = ((ArrayAdapter<ProductType>) parent.getAdapter()).getItem(position);
                refreshBrandList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        brandSpinner = (Spinner)findViewById(R.id.brand_spinner);
        brandArrayList = new ArrayList<>();
        brandArrayAdapter = new ArrayAdapter<Brand>(this, android.R.layout.simple_spinner_item, brandArrayList);
        brandArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandArrayAdapter);

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrand = ((ArrayAdapter<Brand>) parent.getAdapter()).getItem(position);
                refreshModelList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        modelSpinner = (Spinner)findViewById(R.id.model_spinner);
        modelArrayList = new ArrayList<>();
        modelArrayAdapter = new ArrayAdapter<Model>(this, android.R.layout.simple_spinner_item, modelArrayList);
        modelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelArrayAdapter);

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = ((ArrayAdapter<Model>) parent.getAdapter()).getItem(position);
                refreshBodyList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bodySpinner = (Spinner)findViewById(R.id.body_spinner);
        bodyArrayList = new ArrayList<>();
        bodyArrayAdapter = new ArrayAdapter<Body>(this, android.R.layout.simple_spinner_item, bodyArrayList);
        bodyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bodySpinner.setAdapter(bodyArrayAdapter);

        bodySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBody = ((ArrayAdapter<Body>) parent.getAdapter()).getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findButton = (Button)findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(ProductTypeRegistry.PRODUCT_TYPE_ALIAS, selectedProductType.getAlias());
                startActivity(intent);
            }
        });

        handler = new Handler();
        refreshProductTypeList();

    }

    public void refreshProductTypeList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                // TODO Remove
                BodyProductRegistry.getInstance().refreshBodyProductList();
                BodyRegistry.getInstance().refreshBodiesList();
                ModelRegistry.getInstance().refreshModelList();
                BrandRegistry.getInstance().refreshBrandList();

                //BrandRegistry.getInstance().findProduct(1);

                final ArrayList<ProductType> productTypes = ProductTypeRegistry.getInstance().getProductTypes();
                handler.post(
                    new Runnable(){
                        public void run(){
                            productTypeArrayList.clear();
                            for (ProductType productType : productTypes){
                                productTypeArrayList.add(productType);
                            }
                            productTypesArrayAdapter.notifyDataSetChanged();
                        }
                    }
                );
            }
        };
        thread.start();
    }

    public void refreshBrandList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Brand> brands = BrandRegistry.getInstance().findByProductType(selectedProductType.getAlias());
                handler.post(
                        new Runnable(){
                            public void run(){
                                brandArrayList.clear();
                                for (Brand brand : brands){
                                    brandArrayList.add(brand);
                                }
                                brandArrayAdapter.notifyDataSetChanged();
                            }
                        }
                );
            }
        };
        thread.start();
    }

    public void refreshModelList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Model> models = ModelRegistry.getInstance().findByProductType(selectedBrand.getIdBrand(), selectedProductType.getAlias());
                handler.post(
                        new Runnable(){
                            public void run(){
                                modelArrayList.clear();
                                for (Model model : models){
                                    modelArrayList.add(model);
                                }
                                modelArrayAdapter.notifyDataSetChanged();
                            }
                        }
                );
            }
        };
        thread.start();
    }

    public void refreshBodyList(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                final ArrayList<Body> bodies = BodyRegistry.getInstance().findByProductType(selectedModel.getIdModel(), selectedProductType.getAlias());
                handler.post(
                        new Runnable(){
                            public void run(){
                                bodyArrayList.clear();
                                for (Body body : bodies){
                                    bodyArrayList.add(body);
                                }
                                bodyArrayAdapter.notifyDataSetChanged();
                            }
                        }
                );
            }
        };
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
