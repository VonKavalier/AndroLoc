package fr.kavalier.von.androloc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.mapquest.mapping.constants.Style;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import com.mapquest.mapping.maps.MapboxMap;

import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.maps.MapView;

public class RechercheActivity extends AppCompatActivity {

    private MapView mMapView;
    private MapboxMap mMapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuestAccountManager.start(getApplicationContext());

        setContentView(R.layout.activity_recherche);

        mMapView = (MapView) findViewById(R.id.search_view);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapView.setStyleUrl(Style.MAPQUEST_STREETS);
            }
        });

        mMapView.onCreate(savedInstanceState);
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
