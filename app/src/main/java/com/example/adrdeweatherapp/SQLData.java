package com.example.adrdeweatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;


public class SQLData extends AppCompatActivity {

    Spinner dateSpin, timeSpin;
    Button showDataBtn;
    TextView longitude, latitude, pressure, precipitation;


    String[] dates, times;
    String selectedDate, selectedTime;


    private String[][] fetchedData;
    WeatherDatabaseHelper dbHelper = new WeatherDatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqldata);

        dateSpin = findViewById(R.id.set_date_spinner);
        timeSpin = findViewById(R.id.set_time_spinner);
        showDataBtn = findViewById(R.id.show_data_btn);

       //dates = new String[]{"02/07/2023", "03/07/2023", "04/07/2023", "05/07/2023", "06/07/2023", "07/07/2023", "08/07/2023", "09/07/2023", "10/07/2023", "11/07/2023"};
//       times = new String[]{"08:30:00", "11:30:00", "14:30:00", "17:30:00", "20:30:00", "23:30:00", "02:30:00", "05:30:00"};

        dates = dbHelper.fetchDate();

        times = dbHelper.fetchTime();


        latitude = findViewById(R.id.textView_latitude);
        longitude = findViewById(R.id.textView_longitude);
        pressure = findViewById(R.id.surface_pressure);
        precipitation = findViewById(R.id.p_type);



        latitude.setText("Latitude: "+dbHelper.fetchLatitude());

        longitude.setText("Longitude: "+dbHelper.fetchLongitude());
        // 10days * 8timestamps * 14levels = 1120

        /*JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("lat", 27.1767);
            requestParams.put("lon", 78.0081);
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
            array1.put("200h");
            array1.put("150h");
            requestParams.put("levels", array1);
            requestParams.put("key", API_KEY);

            //weatherData.setText("Request : " + requestParams.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestParams,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        //weatherData.setText("Response : " + response.toString());
//
//                        try {
//                            // Perform other database operations
//                            dbHelper.deleteData();
//                            parseAndStoreData(response);
//
//                            weatherData.setText("Ready");
//
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Weather Forecast", "Error while retrieving weather forecast", error);
//                    }
//                });
//        queue.add(jsonObjectRequest);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SQLData.this, android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpin.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SQLData.this, android.R.layout.simple_spinner_item, times);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpin.setAdapter(adapter2);


        dateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                //selectedDate = parent.getItemAtPosition(i).toString();
                selectedDate = dates[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedDate = dates[0];
            }
        });

        timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTime = times[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedTime = times[0];
            }
        });

        showDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String surfacePressure="",pType="";
                // Fetch data from the database
                ArrayList<DataModel> arrData = dbHelper.fetchData("'" + selectedDate + "'", "'" + selectedTime + "'");
                fetchedData = new String[arrData.size() + 1][6];
                fetchedData[0][0] = "Levels";
                fetchedData[0][1] = "Wind\nSpeed\n(m/s)";
                fetchedData[0][2] = "Wind\nDirection\n(degree)";
                fetchedData[0][3] = "Temperature\n(C)";
                fetchedData[0][4] = "Dew\nPoint\n(C)";
                fetchedData[0][5] = "Relative\nHumidity\n(%)";
                for (int z = 0; z < arrData.size(); z++) {
                    fetchedData[z + 1][0] = arrData.get(z).level;
                    if(Objects.equals(arrData.get(z).level, "0km"))
                    {
                        surfacePressure = ""+arrData.get(z).pressure;
                        pType = ""+arrData.get(z).ptype;
                    }

                    fetchedData[z + 1][1] = "" + arrData.get(z).windSpeed;
                    fetchedData[z + 1][2] = "" + arrData.get(z).windDir;
                    fetchedData[z + 1][3] = "" + arrData.get(z).temp;
                    fetchedData[z + 1][4] = "" + arrData.get(z).dew;
                    fetchedData[z + 1][5] = "" + arrData.get(z).rh;
                }

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(SQLData.this));
                recyclerView.setAdapter(new TableAdapter(fetchedData));
                pressure.setText("Surface Pressure: "+surfacePressure);

                /*0 - No precipitation
                1 - Rain
                3 - Freezing rain
                5 - Snow
                7 - Mixture of rain and snow
                8 - Ice pellets*/

                switch (pType) {
                    case "0":
                        precipitation.setText("No Rain");
                        break;
                    case "1":
                        precipitation.setText("Likely to Rain");
                        break;
                    case "3":
                        precipitation.setText("Freezing Rain");
                        break;
                    case "5":
                        precipitation.setText("Chances of Snowfall");
                        break;
                    case "7":
                        precipitation.setText("Rain and Snow");
                        break;
                    case "8":
                        precipitation.setText("Ice Pallets");
                        break;
                }
            }
        });

    }
}