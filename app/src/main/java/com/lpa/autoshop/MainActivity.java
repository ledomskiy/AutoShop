package com.lpa.autoshop;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity {
    //private static final String SHOP_URL = "http://100.112.39.188:8080/AutoShop/webresources/testpackage.test";
    private static final String SHOP_URL = "http://100.112.39.188:8080/AutoShop/webresources/entity.product";

    private TextView tv;

    private Handler handler;
    public void updateText (String text){
        tv.setText(text);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler ();
        tv = (TextView)findViewById (R.id.hello_world);

        Thread networkThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(SHOP_URL);
                    URLConnection urlConnection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;

                    int responseCode = httpConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = httpConnection.getInputStream();

                        /*
                        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                        parserFactory.setNamespaceAware(true);
                        XmlPullParser parser = parserFactory.newPullParser();
                        parser.setInput(inputStream, null);

                        while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                            switch (parser.getEventType()){
                                case XmlPullParser.TEXT:
                                    Log.v("ParseXML", "Text = " + parser.getText()  );
                                    break;
                                default :
                                    break;
                            }
                            parser.next();
                        }
                        */

                        byte[] resultArray = new byte[inputStream.available()];
                        inputStream.read(resultArray);
                        final String resultString = new String (resultArray);


                        handler.post(
                            new Runnable (){
                                public void run(){
                                    updateText(resultString);
                                }
                            }
                        );
                        Log.v("MainActivity", resultString);


                        Log.v("MainActivity","HTTP_SUCCESS");
                    }else{
                        Log.v("MainActivity","HTTP_ERROR");
                    }

                }
                catch (Exception e) {
                    Log.v("MainActivity", "EXCEPTION!!! " + e.getMessage());
                }
            }
        };
        networkThread.start();

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
