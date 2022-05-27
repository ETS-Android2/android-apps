package com.example.FoodRescueApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.User;

public class SignupActivity extends AppCompatActivity {

    // Variables
    EditText fullnameEditText, emailEditText, phoneEditText, addressEditText, signupPasswordEditText, signupconfirmPassEditText;
    Button saveButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Assigning Variables
        fullnameEditText = findViewById(R.id.fullnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        signupPasswordEditText = findViewById(R.id.signupPasswordEditText);
        signupconfirmPassEditText = findViewById(R.id.signupConfirmPassEditText);
        saveButton = findViewById(R.id.saveButton);

        db = new DatabaseHelper(this);


        // OnClickListener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = fullnameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String password = signupPasswordEditText.getText().toString();
                String confirmPassword = signupconfirmPassEditText.getText().toString();

                if (password.equals(confirmPassword))   // If both pass is the same
                {
                    long result = db.insertUser(new User(username, email, phone, address, password));

                    if (result > 0)
                    {
                        Toast.makeText(SignupActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        Toast.makeText(SignupActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(SignupActivity.this, "The two password does not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // To return to login screen
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}