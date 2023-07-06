package com.example.adrdeweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button currentLocationButton;
    private Button otherLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocationButton = findViewById(R.id.button_current_location);
        otherLocationButton = findViewById(R.id.button_manual_location);



        /*Intent currentIntent = new Intent(MainActivity.this, SQLData.class);
        currentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(currentIntent);
        finish();*/


        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Fetching Current Location....", Toast.LENGTH_SHORT).show();
                Intent currentIntent = new Intent(MainActivity.this, GetCurrentLocation.class);
                currentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(currentIntent);
                finish();
            }
        });

        otherLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OtherLocation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}

//API KEY:  ZHSPL4MaF9muR1LVetttfR51i6OduFz5