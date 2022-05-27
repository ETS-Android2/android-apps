package com.example.FoodRescueApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailsActivity extends AppCompatActivity {

    // Variables
    TextView paymentID, paymentAmount, paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        // Initializing the views
        initializeViews();

        // Getting the intent
        Intent intent = getIntent();

        // Attempting to receive the Intent
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    // Private method to show details
    private void showDetails(JSONObject response, String amount)
    {
        try {
            paymentID.setText(response.getString("id"));
            paymentAmount.setText(response.getString("$" + amount));
            paymentStatus.setText(response.getString("state"));
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    // Private method for assigning all the views
    private void initializeViews()
    {
        paymentID = findViewById(R.id.paymentID);
        paymentAmount = findViewById(R.id.paymentAmount);
        paymentStatus = findViewById(R.id.paymentStatus);
    }

}