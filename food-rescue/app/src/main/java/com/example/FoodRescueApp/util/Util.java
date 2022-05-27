package com.example.FoodRescueApp.util;

public class Util
{
    // Contains database constants

    // Util for database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "food_rescue_db";
    public static final String USER_TABLE_NAME = "users";
    public static final String FOOD_TABLE_NAME = "foods";


    // Util for user
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";   // Will be only the full name
    public static final String EMAIL = "email";  // Email will be for user login
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASSWORD = "password";

    // Util for food
    public static final String FOOD_ID = "food_id";
    public static final String POSTER_ID = "user_id";
    public static final String FOOD_IMAGE = "image";
    public static final String FOOD_TITLE = "title";
    public static final String FOOD_DESCRIPTION = "description";
    public static final String FOOD_DATE = "date";
    public static final String FOOD_PICKUP_TIMES = "pickup_times";
    public static final String FOOD_QUANTITY = "quantity";
    public static final String FOOD_LOCATION = "location";


    // Queries for database creation

    public static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + "(" + USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + " TEXT," + EMAIL + " TEXT,"
            + PHONE + " TEXT," + ADDRESS + " TEXT," + PASSWORD + " TEXT)";

    public static final String CREATE_FOOD_TABLE = "CREATE TABLE " + FOOD_TABLE_NAME + "(" + FOOD_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + POSTER_ID + " INTEGER," + FOOD_IMAGE + " BLOB,"
            + FOOD_TITLE + " TEXT," + FOOD_DESCRIPTION + " TEXT," + FOOD_DATE + " INTEGER," +
            FOOD_PICKUP_TIMES + " INTEGER," + FOOD_QUANTITY + " INTEGER," + FOOD_LOCATION + " TEXT)";

}
