package com.example.restaurantmapapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.restaurantmapapp.data.DatabaseHelper;
import com.example.restaurantmapapp.model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.restaurantmapapp.databinding.ActivityRestaurantMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityRestaurantMapsBinding binding;
    DatabaseHelper db;
    List<Location> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRestaurantMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        db = new DatabaseHelper(this);

        // Getting the data
        locationList = db.fetchAllLocations();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Loop for each entry in location table
        for (int i = 0; i < locationList.size(); i++)
        {
            // Retrieving the data
            String name = locationList.get(i).getName();
            double latitude = locationList.get(i).getLatitude();
            double longitude = locationList.get(i).getLongitude();


            // Setting up the marker
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8));

        }
    }
}