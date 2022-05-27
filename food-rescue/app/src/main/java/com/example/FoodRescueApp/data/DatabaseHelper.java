package com.example.FoodRescueApp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.example.FoodRescueApp.model.User;
import com.example.FoodRescueApp.model.Food;
import com.example.FoodRescueApp.util.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public DatabaseHelper(@Nullable Context context)
    {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Creating the tables
        db.execSQL(Util.CREATE_USER_TABLE); // Creates the user table
        db.execSQL(Util.CREATE_FOOD_TABLE); // Creates the food table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + Util.USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Util.FOOD_TABLE_NAME);

        onCreate(db);
    }


    // Methods for user database

    // Method to insert user into the database
    public long insertUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USERNAME, user.getUsername());
        contentValues.put(Util.EMAIL, user.getEmail());
        contentValues.put(Util.PHONE, user.getPhone());
        contentValues.put(Util.ADDRESS, user.getAddress());
        contentValues.put(Util.PASSWORD, user.getPassword());

        long newRowId = db.insert(Util.USER_TABLE_NAME, null, contentValues);

        db.close();

        return newRowId;
    }


    // Method to fetch user email and password from database
    public boolean fetchUser(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.USER_TABLE_NAME, new String[]{Util.USER_ID}, Util.EMAIL + "=? and " + Util.PASSWORD + "=?",
                new String[] {email, password}, null, null, null);

        int numberOfRows = cursor.getCount();

        db.close();

        if (numberOfRows > 0) return true;

        else return false;
    }

    // Method to fetch user details from given userId
    public User fetchUserInfo(int userId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String name, email, phone, address, password;

        String selectUserInfo = "SELECT * FROM " + Util.USER_TABLE_NAME + " WHERE " + Util.USER_ID + "= ?";
        String[] param = new String[] {String.valueOf(userId)};

        Cursor cursor = db.rawQuery(selectUserInfo, param);

        if (cursor.moveToFirst())
        {
            do {

                name = cursor.getString(cursor.getColumnIndex(Util.USERNAME));
                email = cursor.getString(cursor.getColumnIndex(Util.EMAIL));
                phone = cursor.getString(cursor.getColumnIndex(Util.PHONE));
                address = cursor.getString(cursor.getColumnIndex(Util.ADDRESS));
                password = cursor.getString(cursor.getColumnIndex(Util.PASSWORD));

            } while (cursor.moveToNext());
        }
        else
        {
            throw new IllegalStateException("User not found");
        }
        db.close();

        return new User(userId, name, email, phone, address, password);
    }


    // Method to fetch user id from given user email
    public int fetchUserId(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int user_id = -1;   // If it returns -1 that means that the user is not there

        String query = "SELECT user_id FROM users WHERE email = ?";
        String[] param = new String[] {email};

        Cursor cursor = db.rawQuery(query, param);

        if (cursor != null && cursor.moveToFirst())
        {
            user_id = cursor.getInt(0);
            return user_id;
        }

        return -1;
    }


    public String fetchUserName(String email)
    {
        String username = "Unknown";

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username FROM users WHERE email = ?";
        String[] param = new String[] {email};

        Cursor cursor = db.rawQuery(query, param);

        if (cursor != null && cursor.moveToFirst())
        {
            username = cursor.getString(cursor.getColumnIndex(Util.USERNAME));
            return username;
        }

        return username;
    }

    // Methods for food database

    // Method to insert food into the database
    public long insertFood(Food food)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.POSTER_ID, food.getUser_id());
        contentValues.put(Util.FOOD_IMAGE, food.getImageBlob());
        contentValues.put(Util.FOOD_TITLE, food.getTitle());
        contentValues.put(Util.FOOD_DESCRIPTION, food.getDescription());
        contentValues.put(Util.FOOD_DATE, food.getDate());
        contentValues.put(Util.FOOD_PICKUP_TIMES, food.getTime());
        contentValues.put(Util.FOOD_QUANTITY, food.getQuantity());
        contentValues.put(Util.FOOD_LOCATION, food.getLocation());

        long newRowId = db.insert(Util.FOOD_TABLE_NAME, null, contentValues);

        db.close();

        return newRowId;
    }


    // Method to fetch all of the foods
    // Will only retrieve 4 data for specific food because we only display
    // The image, title, description, and poster in the recycler view
    public List<Food> fetchAllFoods()
    {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAllFood = "SELECT * FROM " + Util.FOOD_TABLE_NAME;

        Cursor cursor = db.rawQuery(selectAllFood, null);

        if (cursor.moveToFirst())
        {
            do {
                Food food = new Food();
                // We are using getColumnIndex just in case if in the future we moved the columns around
                food.setUser_id(cursor.getInt(cursor.getColumnIndex(Util.POSTER_ID)));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.FOOD_TITLE)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.FOOD_DESCRIPTION)));
                food.setDate(cursor.getLong(cursor.getColumnIndex(Util.FOOD_DATE)));
                food.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.FOOD_QUANTITY)));
                food.setLocation(cursor.getString(cursor.getColumnIndex(Util.FOOD_LOCATION)));

                // Image conversion
                byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(Util.FOOD_IMAGE));

                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                food.setImage(imageBitmap);


                // Fix getting the time

                foodList.add(food);
            } while (cursor.moveToNext());
            db.close();
        }
        return foodList;
    }


    // Method to fetch all foods item from a specified user
    public List<Food> fetchAllUserFoods(int userId)
    {
        List<Food> myFoodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAllFood = "SELECT * FROM " + Util.FOOD_TABLE_NAME  + " WHERE " + Util.USER_ID
                + "= ?";
        String[] param = new String[] {String.valueOf(userId)};

        Cursor cursor = db.rawQuery(selectAllFood, param);

        if (cursor.moveToFirst())
        {
            do {
                Food food = new Food();
                // We are using getColumnIndex just in case if in the future we moved the columns around
                food.setUser_id(cursor.getInt(cursor.getColumnIndex(Util.POSTER_ID)));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.FOOD_TITLE)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.FOOD_DESCRIPTION)));
                food.setDate(cursor.getLong(cursor.getColumnIndex(Util.FOOD_DATE)));
                food.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.FOOD_QUANTITY)));
                food.setLocation(cursor.getString(cursor.getColumnIndex(Util.FOOD_LOCATION)));

                // Fix getting the time

                // Image conversion
                byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(Util.FOOD_IMAGE));

                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                food.setImage(imageBitmap);

                myFoodList.add(food);
            } while (cursor.moveToNext());
        } db.close();

        return myFoodList;
    }

}
