package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    TextView txtCoordiantes,txtAddress;
    private Context context;
    Button btnMap;
    private String lat=null,longt=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        txtCoordiantes = findViewById(R.id.txtCoordiantes);
        txtAddress = findViewById(R.id.txtAddress);
        btnMap = findViewById(R.id.btnMap);
        context = LocationActivity.this;

        /*
         * 1.Add location to manifest - course and fine
         * 2,Request
         *
         * */
        Toast.makeText(LocationActivity.this,"Hello this is a toast",Toast.LENGTH_LONG).show();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//initi
        checkPermission();

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if lat and longitude are not null
                if (lat!=null && longt!=null){
                    Intent intent = new Intent(context,MapsActivity.class);
                    //pass latitude and longitude
                    intent.putExtra("latitude",lat);
                    intent.putExtra("longitude",longt);
                    startActivity(intent);
                }

            }
        });
    }


    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            //request for the user to give the consent to access
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
            }
        }
    }

    @SuppressLint("MissingPermission")
    void getLocation() {

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null){
                            //check the cordantes
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();
                            //toast
                            Toast.makeText(context,
                                    "The longitude is "+longitude+" latitude is "+latitude,Toast.LENGTH_LONG).show();

                            lat = String.valueOf(latitude);
                            longt = String.valueOf(longitude);
                            //call
                            String address = getAddress(latitude,longitude);

                            txtCoordiantes.setText("The longitude is "+longitude+" latitude is "+latitude);
                            txtAddress.setText("You are at "+address);

                        }else{
                            //its null no location
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //gps is off
                //gps
                showToast("Ooops! something went wrong "+e.getMessage());
            }
        });

    }
    public void showToast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                getLocation();
            }  else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
               // checkPermission();
            }
        }
    }
    String getAddress(double latitude,double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Oops!Something went wrong",Toast.LENGTH_LONG).show();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();
        String subAdminArea = addresses.get(0).getFeatureName();

        String myaddress = country+","+city+" "+subAdminArea;
        return myaddress;
    }
}