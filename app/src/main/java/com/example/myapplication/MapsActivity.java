package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;//map object mMap
    private int type=1;/* type - 1-normal,2-hybrid,3 terrain*/
    private Context context;
    private SupportMapFragment mapFragment;

    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = MapsActivity.this;

        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        /*
        enables to actually display - view
        * */
        Button hybridButton = findViewById(R.id.btnHybrid);
        Button terrainButton = findViewById(R.id.btnTerrain);
        Button normalButton = findViewById(R.id.btnNormal);

        hybridButton.setOnClickListener(this);
        terrainButton.setOnClickListener(this);
        normalButton.setOnClickListener(this);
       /* hybridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        terrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type =3;
                mapFragment.getMapAsync((OnMapReadyCallback) context);
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=1;
                mapFragment.getMapAsync((OnMapReadyCallback) context);
            }
        });*/

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        switch (type){
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }

        // Add a marker in Sydney and move the camera
        LatLng mylocation = new LatLng(latitude, longitude);//cordinates - show actual location
        /**/
        /*showing the marker*/
        mMap.addMarker(new MarkerOptions().position(mylocation).title("My Location")).showInfoWindow();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,18.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation,18));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnHybrid:
                type=2;
                mapFragment.getMapAsync((OnMapReadyCallback) context);
                break;
                case R.id.btnNormal:
                type=1;
                mapFragment.getMapAsync((OnMapReadyCallback) context);
                break; case R.id.btnTerrain:
                type=3;
                mapFragment.getMapAsync((OnMapReadyCallback) context);
                break;
        }
    }
}