package com.example.FoodRescueApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.Food;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class AddFoodActivity extends AppCompatActivity {

    // Declaring Variables
    EditText titleEditText, descriptionEditText, timeEditText, quantityEditText, locationEditText;
    ImageView addImageView;
    Button addImageButton, saveFoodButton;
    CalendarView foodCalendarView;
    Bitmap image;

    double latitude;
    double longitude;

    LocationManager locationManager;
    LocationListener locationListener;
    AutocompleteSupportFragment autocompleteSupportFragment;

    private static final String TAG = "Running";

    public static final int PICK_IMAGE = 1;    // Constant for when the user comes back

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Assigning Variables
        assignAllViews();

        // New database instance
        DatabaseHelper db = new DatabaseHelper(this);

        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);

        // Initialize Variables
        latitude = 0;
        longitude = 0;


        // Setting up Auto Complete
        setupPlaces();

        // Setting up Location Manager
        setupLocationManager();


        // OnClickListener for save food button
        // Will also check if any of the fields are empty
        saveFoodButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkAllInputs() == true)
                {
                    int userId = currentUserId;
                    String title = titleEditText.getText().toString();
                    String desc = descriptionEditText.getText().toString();
                    long date = foodCalendarView.getDate();
                    String pickupTime = timeEditText.getText().toString();
                    int quantity = Integer.parseInt(quantityEditText.getText().toString());
                    String location = locationEditText.getText().toString();


                    // Create a new food object
                    Food food = new Food();

                    food.setUser_id(userId);

                    try{
                        int checkImage = image.getHeight(); // To check if the user has picked an image or not
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(AddFoodActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }

                    food.setImage(image);
                    food.setTitle(title);
                    food.setDescription(desc);
                    food.setDate(date);
                    food.setTime(pickupTime);
                    food.setQuantity(quantity);
                    food.setLocation(location);
                    food.setLatitude(latitude);
                    food.setLongitude(longitude);

                long result = db.insertFood(food);

                if (result > 0)
                {
                    Toast.makeText(AddFoodActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                    Intent backIntent = new Intent(AddFoodActivity.this, HomeActivity.class);
                    startActivity(backIntent);
                    finish();
                }

                else Toast.makeText(AddFoodActivity.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // OnClickListener for add image button
        // Will request permission from the user to retrieve image from the gallery
        addImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // If the user still hasn't added permission to read external storage
                    if (ActivityCompat.checkSelfPermission(AddFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(AddFoodActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PICK_IMAGE);
                    }

                    else
                    {
                        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
                        pickImageIntent.setType("image/*");
                        startActivityForResult(pickImageIntent, PICK_IMAGE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    // Overriding method from intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            Uri imageUri = data.getData();
            InputStream inputStream;

            try
            {
                inputStream = getContentResolver().openInputStream(imageUri);
                image = BitmapFactory.decodeStream(inputStream);

                addImageView.setImageURI(imageUri);
            }
            
            catch (Exception e)
            {
                Toast.makeText(this, "Error, file not found!", Toast.LENGTH_SHORT).show();
            }


        }



    }

    // Overriding method on request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PICK_IMAGE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
                startActivityForResult(pickImageIntent, PICK_IMAGE);
            }
        }
        else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


        // Permission for location
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0 ,0, locationListener);
            }
        }
    }

    private boolean checkAllInputs()
    {
        String title = titleEditText.getText().toString();
        String desc = descriptionEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String location = locationEditText.getText().toString();

        if (title.equals("") || desc.equals("") || time.equals("") || quantity.equals("") ||
        location.equals(""))
        {
            Toast.makeText(this, "Please fill all empty fields!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (longitude == 0 && latitude == 0)
        {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }


    private void assignAllViews()
    {
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        timeEditText = findViewById(R.id.timeEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        locationEditText = findViewById(R.id.locationEditText);

        addImageView = findViewById(R.id.addImageView);
        addImageButton = findViewById(R.id.addImageButton);
        saveFoodButton = findViewById(R.id.saveFoodButton);
        foodCalendarView = findViewById(R.id.foodCalendarView);

    }


    // Methods for place API

    // Method to setup the places for AutoComplete
    private void setupPlaces()
    {
        // Initialize the SDK
        Places.initialize(getApplicationContext(),getString(R.string.Places_API));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));


        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                latitude = place.getLatLng().latitude;  // Set the latitude from chosen location
                longitude = place.getLatLng().longitude;    // Set the longitude from chosen location

                // Debugging
                //Toast.makeText(AddFoodActivity.this, "Place: " + place.getLatLng().latitude + " L " + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    // Method to setup the location manager
    private void setupLocationManager() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

            }
        };

        // Checking Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        else locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }


    // To return to home screen
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}