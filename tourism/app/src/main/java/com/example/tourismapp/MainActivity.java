package com.example.tourismapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlaceTogoAdapter.OnRowClickListener
{

    // Declaring Variables

    // Declaring Recycler view with the adapter
    RecyclerView topRecyclerView, placeRecyclerView;
    TopDestinationAdapter topDestinationAdapter;
    PlaceTogoAdapter placeTogoAdapter;

    // The data list
    List<TopDestination> topDestinationList = new ArrayList<>();
    List<PlaceTogo> placeTogoList = new ArrayList<>();

    // Datasets
    int[] topImageList = {R.drawable.borobudur, R.drawable.mount_bromo, R.drawable.lake_toba};
    int[] placeImageList = {R.drawable.borobudur, R.drawable.mount_bromo, R.drawable.komodo_national_park, R.drawable.lake_toba, R.drawable.ubud_monkey, R.drawable.gili_island};

    String[] placeNameList, placeLocationList, placeDescList;


    // Widgets
    TextView topDestinationTextView, placesToGoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up variables

        placeNameList = getResources().getStringArray(R.array.placenamelist);   // Gets the name of places
        placeLocationList = getResources().getStringArray(R.array.placelocationlist);   // Gets the location of the places
        placeDescList = getResources().getStringArray(R.array.placedesclist);

        // Finding views
        topDestinationTextView = findViewById(R.id.topDestinationTextView);
        placesToGoTextView = findViewById(R.id.placesToGoTextView);

        topRecyclerView = findViewById(R.id.topRecyclerView);
        placeRecyclerView = findViewById(R.id.bottomRecyclerView);

        // Setting up the Adapter for top view
        topDestinationAdapter = new TopDestinationAdapter(topDestinationList, this);
        topRecyclerView.setAdapter(topDestinationAdapter);

        // Setting up the layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);  // New instance of layout Manager
        topRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);  // Sets the orientation to horizontal

        for (int i = 0; i < topImageList.length; i++)
        {
            TopDestination topDestination = new TopDestination(i, topImageList[i]);
            topDestinationList.add(topDestination);
        }


        // Setting up Adapter for place view
        placeTogoAdapter = new PlaceTogoAdapter(placeTogoList, this, this);
        placeRecyclerView.setAdapter(placeTogoAdapter);

        // Setting up layout
        placeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < placeImageList.length; i++)
        {
            PlaceTogo placeTogo = new PlaceTogo(i, placeImageList[i], placeNameList[i], placeLocationList[i], placeDescList[i]);
            placeTogoList.add(placeTogo);
        }
    }


    // On click method

    @Override
    public void onItemClick(int position)
    {
        Fragment fragment;

        switch (position)
        {
            // Will run the first place fragment
            case 0:
                fragment = new Borobudur1Fragment();
                break;

            case 1:
                fragment = new BromoFragment();
                break;

            case 2:
                fragment = new KomodoFragment();
                break;

            case 3:
                fragment = new TobaFragment();
                break;

            case 4:
                fragment = new UbudFragment();
                break;

            case 5:
                fragment = new GiliFragment();
                break;

            default:
                throw new IllegalStateException("Unexpected Value");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment).commit();
    }

}