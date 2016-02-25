package com.example.pedrobrito.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class loginActivity extends Activity {


    Button btnLogin;
    EditText email;
    EditText password;
    HttpResponse response;
    String responseBody;
    FileOutputStream outputStream;
    String mac;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.textEmail);
        password = (EditText) findViewById(R.id.textPassword);

        Typeface robotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        btnLogin.setTypeface(robotoLight);
        email.setTypeface(robotoLight);
        password.setTypeface(robotoLight);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Thread sqlPost = new Thread() {

                    @Override
                    public void run() {

                        try {

                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost("http://pedrobrito.net/pesta/php/androidlogin.php");
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
                            nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                            // Execute HTTP Post Request
                            response = httpclient.execute(httppost);

                            // Convert from httprequest to string
                            HttpEntity response_entity = response.getEntity();
                            responseBody = EntityUtils.toString(response_entity);


                        } catch (Exception e) {

                            showToast(getString(R.string.MyMessage));

                        }

                    }
                };

                sqlPost.start();

                try {
                    sqlPost.join();

                    if (!responseBody.isEmpty()){
                        //status.setText(responseBody);
                        mac = responseBody;

                        try {
                            outputStream = openFileOutput("macAddress.txt", Context.MODE_PRIVATE);
                            outputStream.write(mac.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            outputStream = openFileOutput("loginData.txt", Context.MODE_PRIVATE);
                            outputStream.write(email.getText().toString().getBytes());
                            outputStream.write(" ".getBytes());
                            outputStream.write(password.getText().toString().getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Intent mainIntent = new Intent(loginActivity.this, Splash.class);
                        loginActivity.this.startActivity(mainIntent);
                        loginActivity.this.finish();

                    }

                    else{

                        showToast(getString(R.string.LoginInvalid));

                    }


                    //String[] responseSplit = responseBody.split();
                    //menuName.setText(responseSplit[5]);

                } catch (Exception e) {
                    showToast(getString(R.string.MyMessage));
                    //  e.printStackTrace();
                }


            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
                Toast.makeText(loginActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
