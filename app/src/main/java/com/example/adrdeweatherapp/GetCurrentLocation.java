package com.example.adrdeweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetCurrentLocation extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView address, city, country, latitude, longitude;
    Button getLocation;

    private final static  int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_current_location);

        city = findViewById(R.id.city);
        address = findViewById(R.id.addressView);
        country = findViewById(R.id.country);
        getLocation = findViewById(R.id.get_location_btn);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
    }

    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(GetCurrentLocation.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);

                            latitude.setText("Latitude : " +addresses.get(0).getLatitude());
                            longitude.setText("Longitude : " +addresses.get(0).getLongitude());
                            address.setText("Address : " +addresses.get(0).getAddressLine(0));
                            city.setText("City : " +addresses.get(0).getLocality());
                            country.setText("Country : " +addresses.get(0).getCountryName());

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }
        else {
            askPermission();
        }
    }

    private void askPermission()
    {
        ActivityCompat.requestPermissions(GetCurrentLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else
            {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}