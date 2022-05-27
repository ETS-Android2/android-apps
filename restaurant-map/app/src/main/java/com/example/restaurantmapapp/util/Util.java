package com.example.restaurantmapapp.util;

public class Util
{
    // Database Util
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "location_db";
    public static final String TABLE_NAME = "locations";


    // Location table util
    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_NAME = "location_name";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";


    // Creating Table Util

    public static final String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_NAME
            + "("
            + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LOCATION_NAME + " TEXT, "
            + LATITUDE + " REAL, "
            + LONGITUDE + " REAL"
            + ")";
}
