package com.example.adrdeweatherapp;

import static java.lang.Math.atan;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

public class WeatherUpdates extends AppCompatActivity {

    private static final String API_KEY = "ZHSPL4MaF9muR1LVetttfR51i6OduFz5";
    private static final String API_URL = "https://api.windy.com/api/point-forecast/v2";

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    private TextView weatherData,textViewLatitude,textViewLongitude;

    String[] dates, times;
    ArrayList<Pair<String, String>> mapHeight = new ArrayList<>();


    double windU = 0.0, windV = 0.0, temp = 0.0, pressure = 0.0, dewPoint = 0.0;
    int rh = 0, ptype = 0;

    WeatherDatabaseHelper dbHelper = new WeatherDatabaseHelper(this);

    String latitude1, longitude1;

    String data = "", parameterLevel = "", unitValue = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_updates);

        weatherData = findViewById(R.id.weather_data);

        textViewLatitude = findViewById(R.id.textView_latitude);

        textViewLongitude = findViewById(R.id.textView_longitude);

        RequestQueue queue = Volley.newRequestQueue(WeatherUpdates.this);




        Intent intent = getIntent();
        latitude1 = intent.getStringExtra("Latitude");
        longitude1 = intent.getStringExtra("Longitude");

        textViewLatitude.setText("Latitude: " + latitude1);
        textViewLongitude.setText("Longitude: " + longitude1);

        String url = API_URL;


        if (!isNetworkConnected()) {
            Toast.makeText(this, "Internet Unavailable! Showing saved data", Toast.LENGTH_SHORT).show();
            Intent weatherIntent = new Intent(WeatherUpdates.this, SQLData.class);
            weatherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(weatherIntent);
            finish();

        }

        mapHeight.add(new Pair<>("surface", "0km"));
        mapHeight.add(new Pair<>("1000h", "0.1km"));
        mapHeight.add(new Pair<>("950h", "0.6km"));
        mapHeight.add(new Pair<>("925h", "0.75km"));
        mapHeight.add(new Pair<>("900h", "0.9km"));
        mapHeight.add(new Pair<>("850h", "1.5km"));
        mapHeight.add(new Pair<>("800h", "2.0km"));
        mapHeight.add(new Pair<>("700h", "3.0km"));
        mapHeight.add(new Pair<>("600h", "4.2km"));
        mapHeight.add(new Pair<>("500h", "5.5km"));
        mapHeight.add(new Pair<>("400h", "7.0km"));
        mapHeight.add(new Pair<>("300h", "9.0km"));
        mapHeight.add(new Pair<>("200h", "11.7km"));
        mapHeight.add(new Pair<>("150h", "13.5km"));


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
                            // Perform other database operations
                            dbHelper.deleteData();
                            parseAndStoreData(response);

                            weatherData.setText("Ready");

                            Intent weatherIntent = new Intent(WeatherUpdates.this, SQLData.class);
                            weatherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(weatherIntent);
                            finish();


                        } catch (Exception e) {
                            throw new RuntimeException(e);
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


//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestParams,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                       //weatherData.setText("Response : " + response.toString());
//
//                        try {
//                            JSONArray tsArray = response.getJSONArray("ts");
//                            JSONObject units = response.getJSONObject("units");
//
//                            for(int i = 0; i<tsArray.length();i++)
//                            {
//                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
//                                String date = sdf.format(tsArray.get(i));
//                                data = data +date+"\n\n";
//                                Iterator<String> unitKeys = units.keys();
//                                for(int j = 0; j<units.length(); j++)
//                                {
//                                    String parameterLevel = unitKeys.next();
//                                    String unitValue = units.getString(parameterLevel);
//
//                                    double value1 = (double) response.getJSONArray(parameterLevel).getDouble(i);
//                                    double value = Double.parseDouble(decfor.format(value1));
//                                    //String value = response.getJSONArray(parameterLevel).get(i).toString();
//                                    data = data + parameterLevel + ":   "+ value + " " + unitValue +"\n";
//                                }
//                                    data = data +"-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-"+"\n\n\n";
//                            }
//                            weatherData.setText(data);
//
//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Weather Forecast", "Error while retrieving weather forecast", error);
//                    }
//                });
//        queue.add(jsonObjectRequest);
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



    public void parseAndStoreData(JSONObject response) {

        int ptrDates =0, ptrTimes = 0;
        try {


            JSONArray tsArray = response.getJSONArray("ts");
            JSONObject units = response.getJSONObject("units");

            for(int i = 0; i<tsArray.length();i++) {

                for(int itr = 0; itr< mapHeight.size(); itr++)
                { String key = mapHeight.get(itr).first;
                    windU = 0.0; windV = 0.0; temp = 0.0; pressure = 0.0; rh = 0; dewPoint = 0.0; ptype = 0;
                    Iterator<String> unitKeys = units.keys();
                    for (int j = 0; j < units.length(); j++)
                    {
                        parameterLevel = unitKeys.next();
                        unitValue = units.getString(parameterLevel);

                        if(parameterLevel.contains(key))
                        {
                            if(parameterLevel.contains("temp"))
                                temp =  response.getJSONArray(parameterLevel).getDouble(i);

                            else if (parameterLevel.contains("wind_u"))
                                windU =  response.getJSONArray(parameterLevel).getDouble(i);

                            else if (parameterLevel.contains("wind_v"))
                                windV =  response.getJSONArray(parameterLevel).getDouble(i);

                            else if (parameterLevel.contains("pressure"))
                                pressure =  response.getJSONArray(parameterLevel).getDouble(i);

                            else if (parameterLevel.contains("dew"))
                                dewPoint = response.getJSONArray(parameterLevel).getDouble(i);

                            else if (parameterLevel.contains("rh"))
                                rh =  response.getJSONArray(parameterLevel).getInt(i);

                            else if (parameterLevel.contains("ptype"))
                                ptype =  response.getJSONArray(parameterLevel).getInt(i);

                        }

                    }

                    Date date1 = new Date( tsArray.getLong(i));


                    // Set the time zone to Indian Standard Time (GMT + 5:30)
                    TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");

                    // Create a SimpleDateFormat object for formatting
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                    // Set the time zone for the date and time formats
                    dateFormat.setTimeZone(timeZone);
                    timeFormat.setTimeZone(timeZone);

                    // Format the date and time
                    String formattedDate = dateFormat.format(date1);
                    String formattedTime = timeFormat.format(date1);

//                    if( ptrDates == 0)
//                    {
//                        dates[ptrDates++] = ""+formattedDate;
////                        Log.d("Date", "parseAndStoreData: Date added successfully  " + formattedDate);
//                    }
//                    else
//                    if(ptrDates<10 && !dates[ptrDates-1].equals(formattedDate)){
//                        dates[ptrDates++] = formattedDate;
////                        Log.d("Date", "parseAndStoreData: Date added successfully  " + formattedDate);
//                    }
//
//                    if( ptrTimes == 0)
//                    {
//                        times[ptrTimes++]=formattedTime;
////                        Log.d("Time", "parseAndStoreData: Time added successfully  " + formattedTime + " "+ptrTimes);
//                    }
//                    else if(ptrTimes<8 && !times[ptrTimes - 1].equals(formattedTime)){
//                        times[ptrTimes++] = formattedTime;
//                        //Log.d("TIME", times[ptrTimes-2] +"   "+formattedTime);
////                        Log.d("Time", "parseAndStoreData: Time added successfully  " + formattedTime + " "+ptrTimes);
//                    }


                    //SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                    //String date = sdf.format(tsArray.get(i));
                    double windSpeed = sqrt(windU*windU + windV*windV);
                    windSpeed = round(windSpeed*100.00)/100.00;
                    double windDir = atan(windV/windU);
                    windDir = round(windDir*10000.00)/10000.00;
                    pressure = round(pressure*100.00)/100.00;
                    dewPoint = round((dewPoint-273.15)*100.0)/100.0;
                    temp = round((temp-273.15)*100.0)/100.0;

                    String level = mapHeight.get(itr).second;


                    insertWeatherData(formattedDate, formattedTime ,level ,windSpeed,windDir,temp,dewPoint,pressure,rh,ptype);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void insertWeatherData(String date, String time, String level, double windSpeed, double windDir, double temp, double dew, double pressure, int rh, int ptype) {


        WeatherDatabaseHelper dbHelper = new WeatherDatabaseHelper(this);

        dbHelper.insertData(latitude1,longitude1, date, time, level, windSpeed, windDir, temp, dew, pressure, rh, ptype);
    }
}