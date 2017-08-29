package fr.kavalier.von.androloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapquest.mapping.MapQuestAccountManager;

/**
 * Created by Geoffrey on 28/08/2017.
 */

public class TravauxActivity extends AppCompatActivity {

    private MapboxMap boxmap_travaux;
    private MapView map_travaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        MapQuestAccountManager.start(getApplicationContext());

        setContentView(R.layout.activity_travaux);

 /*       map_travaux = (MapView) findViewById((R.id.mapquestMapView));
        map_travaux.onCreate(savedInstanceState);

        map_travaux.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                boxmap_travaux = mapboxMap;
            }
        });*/
    }
/*
    @Override
    public void onResume(){
        super.onResume();
        map_travaux.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        map_travaux.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        map_travaux.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        map_travaux.onSaveInstanceState(outState);
    }*/
}
