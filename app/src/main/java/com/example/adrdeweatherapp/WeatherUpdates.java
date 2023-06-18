package com.example.adrdeweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class WeatherUpdates extends AppCompatActivity {

    private static final String API_KEY = "ZHSPL4MaF9muR1LVetttfR51i6OduFz5";
    private static final String API_URL = "https://api.windy.com/api/point-forecast/v2";

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    private TextView weatherData;

    private TextView textViewLatitude;

    private TextView textViewLongitude;

    String latitude1, longitude1;

    private static String data ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_updates);

        weatherData = findViewById(R.id.weather_data);

        textViewLatitude = findViewById(R.id.textView_latitude);

        textViewLongitude = findViewById(R.id.textView_longitude);

        RequestQueue queue = Volley.newRequestQueue(WeatherUpdates.this);



        Intent intent = getIntent();
        //if(intent.getStringExtra("LocationType") == 2) {
            String latitude1 = intent.getStringExtra("Latitude");
            String longitude1 = intent.getStringExtra("Longitude");

            textViewLatitude.setText("Latitude: " + latitude1);
            textViewLongitude.setText("Longitude: " + longitude1);
//        }
        String url = API_URL;

        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("lat", latitude1);
            requestParams.put("lon", longitude1);
            requestParams.put("model", "gfs");
            JSONArray array = new JSONArray();
            array.put("wind");
            array.put("temp");
            array.put("dewpoint");
            array.put("rh");
            array.put("pressure");
            array.put("ptype");
            requestParams.put("parameters", array);
            JSONArray array1 = new JSONArray();
            array1.put("surface");
            array1.put("1000h");
            array1.put("950h");
            array1.put("925h");
            array1.put("900h");
            array1.put("850h");
            array1.put("800h");
            array1.put("700h");
            array1.put("600h");
            array1.put("500h");
            array1.put("400h");
            array1.put("300h");
          //array1.put("250h");
            array1.put("200h");
            array1.put("150h");
            requestParams.put("levels", array1);
            requestParams.put("key", API_KEY);

            //weatherData.setText("Request : " + requestParams.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       //weatherData.setText("Response : " + response.toString());

                        try {
                            JSONArray tsArray = response.getJSONArray("ts");
                            JSONObject units = response.getJSONObject("units");

                            for(int i = 0; i<tsArray.length();i++)
                            {
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                                String date = sdf.format(tsArray.get(i));
                                data = data +date+"\n\n";
                                Iterator<String> unitKeys = units.keys();
                                for(int j = 0; j<units.length(); j++)
                                {
                                    String parameterLevel = unitKeys.next();
                                    String unitValue = units.getString(parameterLevel);

                                    double value1 = (double) response.getJSONArray(parameterLevel).getDouble(i);
                                    double value = Double.parseDouble(decfor.format(value1));
                                    //String value = response.getJSONArray(parameterLevel).get(i).toString();
                                    data = data + parameterLevel + ":   "+ value + " " + unitValue +"\n";
                                }
                                    data = data +"-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-"+"\n\n\n";
                            }
                            weatherData.setText(data);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Weather Forecast", "Error while retrieving weather forecast", error);
                    }
                });
        queue.add(jsonObjectRequest);
    }
}