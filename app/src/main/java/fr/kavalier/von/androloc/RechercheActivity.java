package fr.kavalier.von.androloc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.constants.Style;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.maps.MapView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RechercheActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    LocationManager locationManager;
    private Bundle bundle;

    // Directions API URL query
    private String search_api_request_url;

    // Mapquest API KEY
    private String MAPQUEST_API_KEY;

    // TAG
    private static final String TAG = "MapFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Recherche de bâtiments");
        MapQuestAccountManager.start(getApplicationContext());

        final Intent intent = getIntent();

        //Get the bundle
        bundle = getIntent().getExtras();

        MAPQUEST_API_KEY = getResources().getString(R.string.mapquest_key);

        setContentView(R.layout.activity_recherche);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mMapView = (MapView) findViewById(R.id.search_view);

        checkLocation();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                if (intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
                    RetrieveSearchTask rst = new RetrieveSearchTask();
                    rst.execute();
                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude")), 11));
                    // request_lieux(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
                } else{
                    mMapView.setStyleUrl(Style.MAPQUEST_STREETS);
                }
            }
        });

        mMapView.onCreate(savedInstanceState);
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

    private void request_lieux(double latitude, double longitude){
        search_api_request_url = "http://www.mapquestapi.com/search/v2/radius?key=" + MAPQUEST_API_KEY +
                "&maxMatches=15&shapePoints=" + latitude + "," + longitude;

        Log.i(TAG, "DIRECTIONS API URL: " + search_api_request_url);
    }

    class RetrieveSearchTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            try {
                URL url = new URL("http://www.mapquestapi.com/search/v2/search?key=" + MAPQUEST_API_KEY +
                        "&maxMatches=100&shapePoints=" + bundle.getDouble("latitude") + "," + bundle.getDouble("longitude")+"&radius=3");
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
                JSONArray a = o.getJSONArray("searchResults");
                for (int i = 0; i < a.length(); i++) {
                    o = a.getJSONObject(i);
                    fields = (JSONArray) o.get("shapePoints");
                    double lat = (double) fields.get(0);
                    double lon = (double) fields.get(1);
                    String name = o.get("name").toString();
                    addMarker(mMapboxMap, lat, lon, name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addMarker(MapboxMap mapboxMap, double latitude, double longitude, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.title(title);
        mapboxMap.addMarker(markerOptions);
    }

    //TODO: Voir JSONArray et faire une boucle sur le tableau de lieu et créer les pointeurs

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
