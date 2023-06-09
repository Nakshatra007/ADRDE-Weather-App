package com.example.adrdeweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OtherLocation extends AppCompatActivity {

    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button doneButton;

    public String latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_location);

        latitudeEditText = findViewById(R.id.EditText_latitude);
        longitudeEditText = findViewById(R.id.EditText_longitude);
        doneButton = findViewById(R.id.button_done);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              latitude = latitudeEditText.getText().toString();
              longitude = longitudeEditText.getText().toString();

                FetchData(latitude, longitude);
            }
        });
    }

    private void FetchData(String latitude, String longitude) {

        if(TextUtils.isEmpty(latitude))
        {
            Toast.makeText(this, "Please enter the Latitude.", Toast.LENGTH_SHORT).show();
        }
        else
        if(TextUtils.isEmpty(longitude))
        {
            Toast.makeText(this, "Please enter the Longitude.", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "Fetching data. Please wait for a while.", Toast.LENGTH_SHORT).show();

            Intent weatherIntent = new Intent(OtherLocation.this, WeatherUpdates.class);
            weatherIntent.putExtra("Latitude", latitude);
            weatherIntent.putExtra("Longitude", longitude);
            weatherIntent.putExtra("LocationType", 2);
            weatherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(weatherIntent);
            finish();
        }
    }
}