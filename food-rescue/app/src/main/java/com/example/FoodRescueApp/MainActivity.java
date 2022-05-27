package com.example.FoodRescueApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;

public class    MainActivity extends AppCompatActivity {

    // Declaring Variables
    Button loginButton, signupButton;
    EditText usernameEditText, passwordEditText;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning Variables
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        db = new DatabaseHelper(this);
    }

    // When the button is clicked
    public void buttonClick(View view)
    {
        switch (view.getId())
        {
            case R.id.loginButton:
                if (checkText(view))
                {
                    boolean result = db.fetchUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());

                    if (result == true)
                    {
                        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

                        int currentUserId = db.fetchUserId(usernameEditText.getText().toString());

                        Intent loginIntent = new Intent(this, HomeActivity.class);
                        loginIntent.putExtra("currentUserKey", currentUserId);
                        loginIntent.putExtra("currentUserEmail", usernameEditText.getText().toString());
                        startActivity(loginIntent);
                    }
                    else
                    {
                        Toast.makeText(this, "Username not found / password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.signupButton:
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
                break;

            default:
                throw new IllegalStateException("Unexpected Value!" + view.getId());
        }
    }


    // Method to check the edit text input
    public boolean checkText(View view)
    {
        String usernameText = usernameEditText.getText().toString();
        String passwordText = passwordEditText.getText().toString();

        if (usernameText.matches(""))
        {
            if (passwordText.matches(""))
            {
                Toast.makeText(this, "Username and Password can't be empty!", Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(this, "Username can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passwordText.matches(""))
        {
            Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}