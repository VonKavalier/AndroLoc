package fr.kavalier.von.androloc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.constants.Style;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TravauxActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    LocationManager locationManager;
    private Bundle bundle;
    // Mapquest API KEY
    private String MAPQUEST_API_KEY;
    private static final String TAG = "LogTravaux";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuestAccountManager.start(getApplicationContext());

        MAPQUEST_API_KEY = getResources().getString(R.string.mapquest_key);

        this.setTitle("Travaux en temps réel");

        final  Intent intent = getIntent();

        //Get the bundle
        bundle = getIntent().getExtras();

        setContentView(R.layout.activity_recherche);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mMapView = (MapView) findViewById(R.id.search_view);

        checkLocation();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                if (intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
                    TravauxActivity.RetrieveTrafficTask rtt = new TravauxActivity.RetrieveTrafficTask();
                    rtt.execute();
                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude")), 9));
                } else{
                    mMapView.setStyleUrl(Style.MAPQUEST_STREETS);
                }
            }
        });

        mMapView.onCreate(savedInstanceState);

    }

    private String request_lieux(double latitude, double longitude) {
        double i = latitude - 0.5;
        double y = longitude - 0.50;
        double iy = latitude + 0.50;
        double yi = longitude + 0.50;
        String filtres = "event,incidents,congestion,construction";
        String search_api_request_url = "http://www.mapquestapi.com/traffic/v2/incidents?key=Nn6yCN1U2vwW4rLQ1XtlNz9qf96cPh7n&boundingBox=" + i + "," + y + "," + iy + "," + yi + "&filters=" + filtres;
        Log.i(TAG, "DIRECTIONS API URL: " + search_api_request_url);
        return search_api_request_url;
    }

    class RetrieveTrafficTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL(request_lieux(bundle.getDouble("latitude"), bundle.getDouble("longitude")));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.i("TEST", url.toString());
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            try {
                JSONObject o = new JSONObject(response);
                JSONArray a = o.getJSONArray("incidents");
                for (int i = 0; i < a.length(); i++) {
                    JSONArray fields = null;
                    o = a.getJSONObject(i);
                    double lat = (double) o.get("lat");
                    double lng = (double) o.get("lng");
                    String desc = o.get("fullDesc").toString();
                    String bmp = o.get("iconURL").toString();
                    addMarker(mMapboxMap, lat, lng, desc, bmp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMarker(MapboxMap mapboxMap, double latitude, double longitude, String desc, String bmp) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.setSnippet(desc);
        mapboxMap.addMarker(markerOptions);
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.alert_location_settings_title)
                .setMessage(getString(R.string.alert_location_settings1) + "\n" +
                        getString(R.string.alert_location_settings2))
                .setPositiveButton(R.string.button_location_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main_intent);
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onResume()
    { super.onResume(); mMapView.onResume(); }

    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    protected void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState); }
}
