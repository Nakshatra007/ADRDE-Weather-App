package com.example.adrdeweatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class WeatherDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather_db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_WEATHER = "weather";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TIMESTAMP = "Timestamp";

    private static final String COLUMN_LATITUDE = "Latitude";

    private static final String COLUMN_LONGITUDE = "Longitude";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TIME = "Time";
    private static final String COLUMN_LEVEL = "Level";
    private static final String COLUMN_WIND_SPEED = "WindSpeed";
    public static final String COLUMN_WIND_DIR = "WindDirection";
    private static final String COLUMN_TEMP = "Temperature";
    public static final String COLUMN_DEWPOINT = "Dewpoint";
    public static final String COLUMN_PRESSURE = "Pressure";
    public static final String COLUMN_RH = "RelativeHumidity";
    public static final String COLUMN_PTYPE = "PrecipitationType";

//    private static final String COLUMN_VALUE = "value";



    // SQL statement for creating the weather table
    private static final String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + COLUMN_TIMESTAMP + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_TIME + " TEXT, "
            + COLUMN_LEVEL + " TEXT, "
            + COLUMN_WIND_SPEED + " REAL, "
            + COLUMN_WIND_DIR + " REAL, "
            + COLUMN_TEMP + " REAL, "
            + COLUMN_DEWPOINT + " REAL, "
            + COLUMN_PRESSURE + " REAL, "
            + COLUMN_RH+ " REAL, "
            + COLUMN_PTYPE + " INTEGER )";

    public WeatherDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // deleteData();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WEATHER);
        onCreate(db);
    }




    public void insertData(String latitude, String longitude, String date, String time, String level, double windSpeed, double windDir,
                           double temp, double dew, double pressure, int rh, int ptype) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LEVEL, level);
        values.put(COLUMN_WIND_SPEED, windSpeed);
        values.put(COLUMN_WIND_DIR, windDir);
        values.put(COLUMN_TEMP, temp);
        values.put(COLUMN_DEWPOINT, dew);
        values.put(COLUMN_PRESSURE, pressure);
        values.put(COLUMN_RH, rh);
        values.put(COLUMN_PTYPE, ptype);


        long newRowId = database.insert(WeatherDatabaseHelper.TABLE_WEATHER, null, values);
//        Log.d("MySQL", "insertData: Data Added Successfully" + level);

        // Check if the insertion was successful
        if (newRowId == -1) {
            // Handle the failure scenario
            Log.d("SQLData", "insertData: Error Occured.");
        }

        // Close the database connection
        // database.close();
    }


    public String[] fetchDate()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT DISTINCT "+COLUMN_DATE+" FROM "+TABLE_WEATHER,null);

        String[] dates = new String[11];
        int i =0;
        while(cursor.moveToNext())
        {
            String date = cursor.getString(0);
            dates[i++] = date;
            Log.d("DatabaseDate", "fetchDate:"+date);
        }
        return dates;

    }

    public String fetchLatitude()
    {
        String latitude = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT DISTINCT "+COLUMN_LATITUDE+" FROM "+TABLE_WEATHER,null);

        while(cursor.moveToNext())
            latitude = cursor.getString(0);

        Log.d("DatabaseLatitude", "fetchedLatitude:"+latitude);
        return latitude;

    }

    public String fetchLongitude()
    {
        String longitude = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT DISTINCT "+COLUMN_LONGITUDE+" FROM "+TABLE_WEATHER,null);

        while(cursor.moveToNext())
            longitude = cursor.getString(0);

        Log.d("DatabaseLongitude", "fetchedLongitude:"+longitude);
        return longitude;

    }

    public String[] fetchTime()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT DISTINCT "+COLUMN_TIME+" FROM "+TABLE_WEATHER,null);

        String[] times = new String[8];
        int i =0;
        while(cursor.moveToNext())
        {
            String time = cursor.getString(0);
            times[i++] = time;
            Log.d("DatabaseTime", "fetchDate:"+time);
        }
        return times;

    }

    public ArrayList<DataModel> fetchData(String date, String time)//String ts)
    {
        //SELECT DISTINCT column1, column2, ...FROM table_name;      for removing duplicate values
        //WHERE TIMESTAMP == DATE & TIME
        // * represents all ROWS
        SQLiteDatabase db = this.getReadableDatabase();
        //SELECT column1, column2, ...FROM table_name WHERE condition;
//        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_WEATHER + " WHERE " + COLUMN_TIMESTAMP + " = 'July 6, 2023 at 2:30 PM'",null);


        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_WEATHER + " WHERE " + COLUMN_DATE + " = " + date +
                " AND " + COLUMN_TIME + " = "+ time,null);
        //Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_WEATHER, null);

        ArrayList<DataModel> arrData = new ArrayList<>();
        while(cursor.moveToNext())
        {
            DataModel data = new DataModel();
            data.id = cursor.getInt(0);
            //data.timestamp = cursor.getString(1);
            data.date = cursor.getString(3);
            data.time = cursor.getString(4);
            data.level = cursor.getString(5);
            data.windSpeed = cursor.getDouble(6);
            data.windDir = cursor.getDouble(7);
            data.temp = cursor.getDouble(8);
            data.dew = cursor.getDouble(9);
            data.pressure = cursor.getDouble(10);
            data.rh = cursor.getInt(11);
            data.ptype = cursor.getInt(12);

            arrData.add(data);
        }
        return arrData;
    }

    /*public void updateWeatherData(DataModel dataModel){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PRESSURE, 545.5);

        database.update(TABLE_WEATHER,contentValues, COLUMN_LEVEL + " = " + dataModel.level, null);


    }*/

    public void deleteData()//int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("delete from "+ TABLE_WEATHER);

//        database.delete(TABLE_WEATHER, null, null);

       // database.delete(TABLE_WEATHER, COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});
    }

}
