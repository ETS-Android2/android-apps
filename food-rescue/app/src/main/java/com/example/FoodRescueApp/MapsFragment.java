package com.example.FoodRescueApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    // Variables
    DatabaseHelper db;
    GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Initialize view to inflate the fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Initialize map fragments
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap)
            {
                mMap = googleMap;

                if (getArguments() != null)
                {
                    // Get the latitude and longitude
                    double latitude = getArguments().getDouble("latitude");
                    double longitude = getArguments().getDouble("longitude");
                    String name = getArguments().getString("location");

                    LatLng location = new LatLng(latitude, longitude);


                    mMap.addMarker(new MarkerOptions().position(location).title(name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                }
            }
        });

        return view;
    }


}