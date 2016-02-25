package com.example.pedrobrito.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class myLocation extends Activity implements SensorEventListener {

    int once = 1;

    GoogleMap googleMap;
    TextView distanceInfo;
    TextView bearingInfo;
    TextView applicationTitle;
    TextView distanceLabel;
    TextView distanceText;
    ImageView rightArrow;
    ImageView leftArrow;
    HttpResponse response;
    String responseBody;
    String email;
    String[] emailsplit;
    Button btnHome;
    double lon1;
    double lat1;
    double lon2;
    double lat2;

    int azimuthRef;
    int azimuthSen;
    int azimuthSum;
    char home;

    int vibrate;

    Sensor s;
    SensorManager sm;

    LocationManager lm;
    LocationListener ll;

    @Override
    public void onPause() {
        super.onPause();

        sm.unregisterListener(this);
        lm.removeUpdates(ll);


    }

    public void onResume() {
        super.onResume();
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new mylocationlistener();
        createMapView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
       createMapView();
        Typeface robotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

        distanceInfo = (TextView) findViewById(R.id.distanceInfo);
        bearingInfo = (TextView) findViewById(R.id.bearingInfo);
        distanceLabel = (TextView) findViewById(R.id.distanceLabel);
        applicationTitle = (TextView) findViewById(R.id.applicationTitle);
        distanceText = (TextView) findViewById(R.id.distanceText);

        rightArrow = (ImageView) findViewById(R.id.imageViewRight);
        leftArrow = (ImageView) findViewById(R.id.imageViewLeft);

       distanceInfo.setTypeface(robotoThin);
       applicationTitle.setTypeface(robotoThin);
       distanceLabel.setTypeface(robotoLight);
       distanceText.setTypeface(robotoLight);
       bearingInfo.setTypeface(robotoLight);


        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new mylocationlistener();

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Choosing Sensor Type
        s = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //Activating The Sensor Listener
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);

        if (mWifi.isConnected())
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
        else
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Thread sqlPost = new Thread() {

                    @Override
                    public void run() {

                        try {

                            HttpClient httpclient = new DefaultHttpClient();
                            //  HttpPost httppost = new HttpPost("http://ave.dee.isep.ipp.pt/~1100471/updateSettings.php");
                            HttpPost httppost = new HttpPost("http://pedrobrito.net/pesta/php/postlocation.php");
                            // Add your data
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("email", emailsplit[0]));
                            nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat2)));
                            nameValuePairs.add(new BasicNameValuePair("lon", Double.toString(lon2)));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                            // Execute HTTP Post Request
                            response = httpclient.execute(httppost);
                            showToast(getString(R.string.LocationUpdated));

                        } catch (Exception e) {

                            showToast(getString(R.string.NoInternetL));
                            //e.printStackTrace();


                        }

                    }
                };

                sqlPost.start();


            }
        });
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
                if (!responseSplit[6].isEmpty()){
                    lat1 = Double.parseDouble(responseSplit[6]);
                    lon1 = Double.parseDouble(responseSplit[7]);
                }
                else{
                     lon1 = 0.0000000;
                     lat1 = 0.0000000;
                    showToast(getString(R.string.LocationNotSet));
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if (home == 0) {

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            azimuthSen = Math.round(sensorEvent.values[0]);

            azimuthSum = azimuthRef - azimuthSen;

            if (azimuthSum < 0)
                azimuthSum = azimuthSum + 360;

            if (azimuthSum > 180)
                azimuthSum = azimuthSum - 360;

            if (azimuthSum > -5 && azimuthSum < 5) {
                //  bearingInfo.setTextColor(0xFFFFFFFF);
                bearingInfo.setAlpha(1F);
                leftArrow.setAlpha(0.2F);
                rightArrow.setAlpha(0.2F);

                if (vibrate == 1) {
                    v.vibrate(20);
                    vibrate = 0;
                }

            } else {
                bearingInfo.setAlpha(0.2F);
                vibrate = 1;

                if (azimuthSum < 5) {
                    leftArrow.setAlpha(1F);
                    rightArrow.setAlpha(0.2F);
                } else {
                    leftArrow.setAlpha(0.2F);
                    rightArrow.setAlpha(1F);
                }


            }
        }

    }

    private void addMarker(){

        LatLng home = new LatLng(lat1,lon1);
        LatLng current = new LatLng(lat2,lon2);
        /** Make sure that the map has been initialised **/
        if(null != googleMap){
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(home).title("House").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_casa)));
            googleMap.addMarker(new MarkerOptions().position(current).title("You Are Here").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_android)));

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(home);
            boundsBuilder.include(current);

            LatLngBounds bounds = boundsBuilder.build();
            if(once == 1){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
            once = 0;}
        }
    }

    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    class mylocationlistener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {

                lon2 = location.getLongitude();
                lat2 = location.getLatitude();

                //Casa Bento
                // double lon1 = -8.581369;
                // double lat1 = 41.172890;

                //Casa Brito
                // double lon1 = -8.59425260;
                // double lat1 = 41.1533801;

                Location loc1 = new Location("");
                loc1.setLatitude(lat1);
                loc1.setLongitude(lon1);

                Location loc2 = new Location("");
                loc2.setLatitude(lat2);
                loc2.setLongitude(lon2);

                double distance = loc2.distanceTo(loc1);
                int bearingInDegrees = Math.round(loc2.bearingTo(loc1));
                int distanceInMeters;

                if (distance < 15) {

                    home = 1;

                    distanceLabel.setVisibility(View.INVISIBLE);
                    rightArrow.setVisibility(View.INVISIBLE);
                    leftArrow.setVisibility(View.INVISIBLE);

                    bearingInfo.setAlpha(1F);
                    distanceInfo.setText("@");

                }

                else {

                    home = 0;

                    distanceLabel.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.VISIBLE);

                    if (distance < 1000) {
                        distanceInMeters = (int) distance;
                        distanceLabel.setText("meters away");
                        distanceInfo.setText("" + String.valueOf(distanceInMeters));

                    } else if (distance > 1000 && distance < 100000) {
                        distance = Math.round(distance / 100) / 10.0;
                        distanceLabel.setText("kilometers away");
                        distanceInfo.setText("" + String.valueOf(distance));

                    } else {
                        distance = distance / 1000;
                        distanceInMeters = (int) distance;
                        distanceInfo.setText("" + String.valueOf(distanceInMeters));
                        distanceLabel.setText("kilometers away");
                    }
                }

                //bearingInDegrees from [-180, 180] to [0, 360]

                if (bearingInDegrees < 0)
                    azimuthRef = 360 + bearingInDegrees;
                else
                    azimuthRef = bearingInDegrees;
            }

            addMarker();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(myLocation.this, toast, Toast.LENGTH_LONG).show();
            }
        });
    }
}