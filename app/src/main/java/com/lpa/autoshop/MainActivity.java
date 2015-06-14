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
import android.widget.EditText;
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
import com.lpa.autoshop.entity.UserRegistry;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button login_button;
    private Button registration_button;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler ();

        loginEditText = (EditText)findViewById(R.id.login_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);

        login_button = (Button)findViewById(R.id.login_button);
        registration_button = (Button)findViewById(R.id.registration_button);

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPassword ();
                    }
                }
        );

        registration_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createUser ();
                    }
                }
        );
    }


    public void createUser (){
        Thread thread = new Thread (){
            @Override
            public void run (){
                final String login = loginEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final boolean isCreated = UserRegistry.getInstance().createUser(login, password);
                handler.post(
                        new Runnable (){
                            public void run (){
                                if (isCreated){
                                    setUserAndStartFindProduct ();
                                }else{
                                    showToast ("User already exists!");
                                }
                            }
                        }
                );
            }
        };
        thread.start();
    }

    public void checkPassword (){
        Thread thread = new Thread (){
            @Override
            public void run (){
                final String login = loginEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final boolean isPasswordCorrect = UserRegistry.getInstance().isPasswordCorrect(login, password);
                handler.post(
                        new Runnable (){
                            public void run (){
                                if (isPasswordCorrect){
                                    setUserAndStartFindProduct ();
                                }else{
                                    showToast ("Autentifacation failed!");
                                }
                            }
                        }
                );
            }
        };
        thread.start();
    }

    public void showToast (String text){
        Toast toast = Toast.makeText (getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show ();
    }

    public void setUserAndStartFindProduct (){
        Intent intent = new Intent (MainActivity.this, ProductFindActivity.class);
        startActivity(intent);
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
