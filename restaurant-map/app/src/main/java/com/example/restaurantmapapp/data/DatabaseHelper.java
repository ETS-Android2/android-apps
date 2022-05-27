package com.example.restaurantmapapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.restaurantmapapp.model.Location;
import com.example.restaurantmapapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{

    // Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Util.CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);  // Drop if table exists

        onCreate(db);
    }


    // Public Methods

    public long insertLocation(Location location)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.LOCATION_NAME, location.getName());
        contentValues.put(Util.LATITUDE, location.getLatitude());
        contentValues.put(Util.LONGITUDE, location.getLongitude());

        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);

        db.close();

        return newRowId;
    }

    // Method to fetch all the available locations
    public List<Location> fetchAllLocations()
    {
        List<Location> locationList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selectAllLocations = "SELECT * FROM " + Util.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectAllLocations, null);

        if (cursor.moveToFirst())
        {
            do {
                Location location = new Location();

                location.setName(cursor.getString(cursor.getColumnIndex(Util.LOCATION_NAME)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(Util.LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(Util.LONGITUDE)));

                locationList.add(location);

            } while (cursor.moveToNext());
        }

        db.close();;

        return locationList;
    }


    // Method to count data inside the table
    public boolean checkData()
    {
        SQLiteDatabase db = getReadableDatabase();

        String countData = "SELECT count(*) FROM " + Util.TABLE_NAME;

        Cursor cursor = db.rawQuery(countData, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);

        if (count > 0) return true;

        else return false;
    }
}
