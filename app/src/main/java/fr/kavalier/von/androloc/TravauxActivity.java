package fr.kavalier.von.androloc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.constants.Style;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

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
    private static final String TAG = "LogTravaux";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuestAccountManager.start(getApplicationContext());

        this.setTitle("Travaux en temps réel");

       final  Intent intent = getIntent();

        //Get the bundle
        final Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.activity_recherche);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mMapView = (MapView) findViewById(R.id.search_view);

        checkLocation();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                if (intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
                    request_lieux(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude")), 11));
                } else{
                    mMapView.setStyleUrl(Style.MAPQUEST_STREETS);
                }
            }
        });

        mMapView.onCreate(savedInstanceState);

    }

    private void request_lieux(double latitude, double longitude) {
        double i = latitude + 1;
        double y = longitude + 1;
        double iy = latitude - 2;
        double yi = longitude - 2;
        String search_api_request_url = "http://www.mapquestapi.com/traffic/v2/incidents?key=Nn6yCN1U2vwW4rLQ1XtlNz9qf96cPh7n&boundingBox=" + i + "," + y + "," + iy + "," + yi + "&filters=incidents";

        Log.i(TAG, "DIRECTIONS API URL: " + search_api_request_url);
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
