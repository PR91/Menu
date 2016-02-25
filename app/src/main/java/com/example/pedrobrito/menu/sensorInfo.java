package com.example.pedrobrito.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class sensorInfo extends Activity {

    //Define all TextView's
    TextView labelSensor1;
    TextView unitSensor1;

    TextView labelSensor2;
    TextView unitSensor2;

    TextView labelSensor3;
    TextView unitSensor3;

    TextView labelSensor4;
    TextView unitSensor4;

    TextView updateLabel;
    TextView updateData;

    TextView homeTitle;
    HttpResponse response;
    String responseBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_info);


        labelSensor1 = (TextView) findViewById(R.id.labelSensor1);
        unitSensor1 = (TextView) findViewById(R.id.unitSensor1);


        labelSensor2 = (TextView) findViewById(R.id.labelSensor2);
        unitSensor2 = (TextView) findViewById(R.id.unitSensor2);

        labelSensor3 = (TextView) findViewById(R.id.labelSensor3);
        unitSensor3 = (TextView) findViewById(R.id.unitSensor3);

        labelSensor4 = (TextView) findViewById(R.id.labelSensor4);
        unitSensor4 = (TextView) findViewById(R.id.unitSensor4);

        updateData = (TextView) findViewById(R.id.updateData);
        updateLabel = (TextView) findViewById(R.id.updateLabel);

        homeTitle = (TextView) findViewById(R.id.homeTitle);

        Typeface robotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        labelSensor1.setTypeface(robotoThin);
        unitSensor1.setTypeface(robotoLight);

        labelSensor2.setTypeface(robotoThin);
        unitSensor2.setTypeface(robotoLight);

        labelSensor3.setTypeface(robotoThin);
        unitSensor3.setTypeface(robotoLight);

        labelSensor4.setTypeface(robotoThin);
        unitSensor4.setTypeface(robotoLight);

        updateLabel.setTypeface(robotoThin);
        updateData.setTypeface(robotoLight);

        homeTitle.setTypeface(robotoThin);


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
                String[] responseSplit = responseBody.split("<br>");
                updateData.setText(responseSplit[0]);
                if (!responseSplit[8].equals("#")){
                    labelSensor1.setText("1 - " + responseSplit[8]);
                    unitSensor1.setText(responseSplit[9]); }
                else{
                    labelSensor1.setText("");
                    unitSensor1.setText("");

                }
                if (!responseSplit[11].equals("#")){
                    labelSensor2.setText("2 - " + responseSplit[11]);
                    unitSensor2.setText(responseSplit[12]); }
                else{
                    labelSensor2.setText("");
                    unitSensor2.setText("");}

                if (!responseSplit[14].equals("#")){
                    labelSensor3.setText("3 - " + responseSplit[14]);
                    unitSensor3.setText(responseSplit[15]); }
                else{
                    labelSensor3.setText("");
                    unitSensor3.setText("");}

                if (!responseSplit[17].equals("#")){
                    labelSensor4.setText("4 - " + responseSplit[17]);
                    unitSensor4.setText(responseSplit[18]); }
                else{
                    labelSensor4.setText("");
                    unitSensor4.setText("");}
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }



    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(sensorInfo.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

}


