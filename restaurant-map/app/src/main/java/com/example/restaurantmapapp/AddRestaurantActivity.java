package com.example.restaurantmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restaurantmapapp.data.DatabaseHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class AddRestaurantActivity extends AppCompatActivity {

    // Declaring Variables
    EditText placeNameEditText, placeLocationEditText;
    Button getLocationButton, showMapButton, saveButton;
    double latitude, longitude;
    String placeName;

    LocationManager locationManager;
    LocationListener locationListener;
    AutocompleteSupportFragment autocompleteSupportFragment;
    DatabaseHelper db;

    private static final String TAG = "Running";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        // Finding Views
        placeNameEditText = findViewById(R.id.placeNameEditText);
        placeLocationEditText = findViewById(R.id.placeLocationEditText);
        getLocationButton = findViewById(R.id.getLocationButton);
        showMapButton = findViewById(R.id.showMapButton);
        saveButton = findViewById(R.id.saveButton);

        // Initializing Variable
        latitude = 0;
        longitude = 0;

        // Initialize the database
        db = new DatabaseHelper(this);

        // Setting up Auto Complete
        setupPlaces();

        // Setting up Location Manager
        setupLocationManager();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0 ,0, locationListener);
            }
        }
    }


    // Handler for get current location button click
    public void getLocationButtonClick(View view)
    {
        // Checking Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        else locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // Creating the string of latitude and longitude
        StringBuilder string = new StringBuilder();
        string.append(latitude);
        string.append(" , ");
        string.append(longitude);

        // Changing the location edit text
        placeLocationEditText.setText(string);
    }


    // Handler for show on map button click
    public void showMapButtonClick(View view)
    {
        if (latitude != 0 && longitude != 0)  // If the user gave the location
        {
            placeName = placeNameEditText.getText().toString();
            
            if (placeName.matches("")) Toast.makeText(this, "Please enter the location's name", Toast.LENGTH_SHORT).show();

            else
            {
                Intent showMapIntent = new Intent(this, ShowMapActivity.class);
                showMapIntent.putExtra("lat", latitude);
                showMapIntent.putExtra("lon", longitude);
                showMapIntent.putExtra("placeName", placeName);
                startActivity(showMapIntent);
            }
        }
        else
        {
            Toast.makeText(this, "You must choose a location!", Toast.LENGTH_SHORT).show();
        }
    }


    // Handler for save button click
    public void saveButtonClick(View view)
    {

        if (latitude != 0 && longitude != 0)  // If the user gave the location
        {
            placeName = placeNameEditText.getText().toString();

            if (placeName.matches(""))  // If the place's name is empty
            {
                Toast.makeText(this, "Please enter the location's name", Toast.LENGTH_SHORT).show();
            }

            else
            {
                // Creating a new location object
                com.example.restaurantmapapp.model.Location location = new com.example.restaurantmapapp.model.Location();

                location.setName(placeName);
                location.setLatitude(latitude);
                location.setLongitude(longitude);

                long result = db.insertLocation(location);

                if (result > 0)
                {
                    Toast.makeText(this, "Location added successfully!", Toast.LENGTH_SHORT).show();
                    Intent finishedIntent = new Intent(AddRestaurantActivity.this, MainActivity.class);
                    startActivity(finishedIntent);
                    finish();
                }
                
                else
                {
                    Toast.makeText(this, "Insertion failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        
        else
        {
            Toast.makeText(this, "You must choose a location", Toast.LENGTH_SHORT).show();
        }

    }


    // To return to Main Activity / Home
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(AddRestaurantActivity.this, MainActivity.class);
        startActivity(backIntent);
        finish();
    }


    // Private Methods

    // Method to setup the places for Auto Complete
    private void setupPlaces() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.PLACES_API_KEY));

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
                // Toast.makeText(AddRestaurantActivity.this, "Place: " + place.getLatLng().latitude + " L " + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
                // Toast.makeText(AddRestaurantActivity.this, "Place:" + place.getName() + "," + place.getId(), Toast.LENGTH_SHORT).show();
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

}