package com.example.pedrobrito.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Splash extends Activity {

    HttpResponse response;
    String responseBody;
    FileOutputStream outputStream;
    String email;
    String[] emailsplit;
    String mac;
    File file = new File("/data/data/com.example.pedrobrito.menu/files/loginData.txt");

    private final int SPLASH_DISPLAY_LENGHT = 1000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);



        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        if(file.exists()){

            try {
                InputStream inputStream = openFileInput("macAddress.txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    mac = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }

            try {
                InputStream inputStream = openFileInput("loginData.txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    email = stringBuilder.toString();
                    emailsplit = email.split(" ");
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }


            Thread sqlPost = new Thread() {

                @Override
                public void run() {

                    try {

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://pedrobrito.net/pesta/php/readandroid.php");
                        // Add your data
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("email", emailsplit[0]));
                        nameValuePairs.add(new BasicNameValuePair("mac_address", mac));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                        // Execute HTTP Post Request
                        response = httpclient.execute(httppost);

                        // Convert from httprequest to string
                        HttpEntity response_entity = response.getEntity();
                        responseBody = EntityUtils.toString(response_entity);

                    } catch (Exception e) {

                    }

                }
            };

            sqlPost.start();

            try {
                sqlPost.join();

                try {
                    if (!responseBody.isEmpty()){
                    outputStream = openFileOutput("applicationData.txt", Context.MODE_PRIVATE);
                    outputStream.write(responseBody.getBytes());
                    outputStream.close();}
                } catch (Exception e) {
                    showToast(getString(R.string.NoInternet));
                }



            } catch (Exception e) {
                showToast(getString(R.string.NoInternet));
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this, MyActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGHT);
        }
        else{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this, loginActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGHT);


        }


    }
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Splash.this, toast, Toast.LENGTH_LONG).show();
            }
        });
    }
}