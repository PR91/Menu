package com.example.pedrobrito.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {


    TextView menuTitle;
    TextView resumoTitle;
    TextView gpsTitle;
    TextView opcoesTitle;
    TextView sobreTitle;
    TextView menuName;
    Button btnAbout;
    Button btnSummary;
    Button btnGps;
    Button btnSensors;
    Button btnLogout;
    String[] responseSplit;
    String responseBody;

  /*  Context myContext = getApplicationContext();

    File mac = new File(myContext.getFilesDir().getPath() + "/macAddress.txt");
    File login = new File(myContext.getFilesDir().getPath() + "/loginData.txt");
    File data = new File(myContext.getFilesDir().getPath() + "/applicationData.txt"); */

    File mac = new File("/data/data/com.example.pedrobrito.menu/files/macAddress.txt");
    File login = new File("/data/data/com.example.pedrobrito.menu/files/loginData.txt");
    File data = new File("/data/data/com.example.pedrobrito.menu/files/applicationData.txt");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        menuTitle = (TextView) findViewById(R.id.menuTitle);
        resumoTitle = (TextView) findViewById(R.id.resumoTitle);
        gpsTitle = (TextView) findViewById(R.id.gpsTitle);
        sobreTitle = (TextView) findViewById(R.id.sobreTitle);
        opcoesTitle = (TextView) findViewById(R.id.opcoesTitle);
        menuName = (TextView) findViewById(R.id.menuName);

        Typeface robotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        menuTitle.setTypeface(robotoThin);
        resumoTitle.setTypeface(robotoThin);
        gpsTitle.setTypeface(robotoThin);
        sobreTitle.setTypeface(robotoThin);
        opcoesTitle.setTypeface(robotoThin);
        menuName.setTypeface(robotoLight);

        btnLogout = (Button) findViewById(R.id.logoutBt);
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mac.delete();
                login.delete();
                data.delete();

                Intent mainIntent = new Intent(MyActivity.this, Splash.class);
                MyActivity.this.startActivity(mainIntent);
                MyActivity.this.finish();

            }
        });

        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyActivity.this, about.class));

            }
        });

        btnSummary = (Button) findViewById(R.id.btnSummary);
        btnSummary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyActivity.this, summary.class));

            }
        });

        btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyActivity.this, myLocation.class));

            }
        });

        btnSensors = (Button) findViewById(R.id.btnSensors);
        btnSensors.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyActivity.this, sensorInfo.class));

            }
        });

        try {
            InputStream inputStream = openFileInput("applicationData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                responseBody = stringBuilder.toString();
                responseSplit = responseBody.split("<br>");
                menuName.setText(responseSplit[5]);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MyActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
