package com.lpa.autoshop.entity;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by lpa on 14.06.15.
 */
public class UserRegistry {
    private static String SERVICE_NAME = "entity.user";

    private static UserRegistry instance;
    private UserRegistry (){
    }

    public static final String getMD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean createUser (String login, String password){
        boolean isCreated = false;

        String postXML =
                "<user>" +
                "    <login>"+login+"</login>" +
                "    <password>"+getMD5 (password)+"</password>" +
                "</user>";
        byte[] postData = postXML.getBytes( StandardCharsets.UTF_8);
        int    postDataLength = postData.length;
        String request = Constants.SERVER_URL+SERVICE_NAME;

        try {
            URL url = new URL(request);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/xml");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }
            Log.v ("UserRegistry", "ResponseCode = " + conn.getResponseCode());
            isCreated = conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("UserRegistry", "EXCEPTION!!! " + e.getMessage());
        }

        return isCreated;
    }

    public boolean isPasswordCorrect (String login, String password){
        boolean isPasswordCorrect = false;

        try {
            URL url = new URL(Constants.SERVER_URL+SERVICE_NAME+"/password/"+login+"/"+getMD5(password));
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                byte [] response = new byte [bufferedInputStream.available()];
                bufferedInputStream.read(response);

                if (new String (response).equals("1")){
                    isPasswordCorrect = true;
                }else{
                    isPasswordCorrect = false;
                }

                Log.v ("UserRegistry", "HTTP_SUCCESS");
            }else{
                Log.v ("UserRegistry", "HTTP_ERROR");
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.v ("ProductRegistry", "EXCEPTION!!! " + e.getMessage());
        }

        return isPasswordCorrect;
    }

    public static UserRegistry getInstance (){
        if (instance == null){
            instance = new UserRegistry ();
        }
        return instance;
    }

}
