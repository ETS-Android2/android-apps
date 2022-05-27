package com.example.FoodRescueApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    // Declaring Variables
    ImageView profileImageView; // TO DO
    TextView fullNameTextView, emailTextView, phoneTextView, addressTextView;
    User user;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Assigning Views
        profileImageView = findViewById(R.id.profileImageView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        addressTextView = findViewById(R.id.addressTextView);

        db = new DatabaseHelper(this);

        // Getting the intent
        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);

        user = db.fetchUserInfo(currentUserId);

        fullNameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
        phoneTextView.setText(user.getPhone());
        addressTextView.setText(user.getAddress());

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