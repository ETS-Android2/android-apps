package com.example.FoodRescueApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.Food;
import com.example.FoodRescueApp.util.PaypalUtil;
import com.google.android.gms.maps.GoogleMap;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodActivity extends AppCompatActivity {

    // Variables
    ImageView selectedFoodImageView;
    TextView selectedFoodTitle, selectedFoodDesc, selectedFoodDate;
    TextView selectedFoodTime, selectedFoodQuantity, selectedFoodLocation;
    Button addToCartButton;
    ImageButton paypalButton;

    private GoogleMap mMap;

    DatabaseHelper db;
    Food food;
    int currentFoodID;

    // Paypal Configuration
    String testPrice = "20";
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)   // Using sandbox because we're testing
            .clientId(PaypalUtil.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Initialize Views
        InitializeFoodViews();

        // Initializing Variables;
        db = new DatabaseHelper(this);

        // Get Intent
        int currentFoodID = getIntent().getIntExtra("selectedFoodID", -1);
        if (currentFoodID == -1) throw new IllegalStateException("Unknown value detected");

        // Get food details
        food = db.fetchFoodInfo(currentFoodID);

        // Update the food information
        UpdateInformation();

        // Location setup
        setupLocation();

        // Paypal Service Setup
        Intent paypalIntent = new Intent(this, PayPalService.class);
        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(paypalIntent);

        // OnClickListener for the add to cart button
        addToCartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("addedFoodID", currentFoodID);
                setResult(2, intent);
                finish();
            }
        });


        // OnClickListener for Paypal Button
        paypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                processPayment();
            }
        });
    }


    // Will run when activity is destroyed
    @Override
    protected void onDestroy()
    {
        stopService(new Intent(FoodActivity.this, PayPalService.class));
        super.onDestroy();
    }

    // Will run after running PayPal Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (paymentConfirmation != null)
                {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString(4);

                        startActivity(new Intent(FoodActivity.this, PayPalPaymentDetails.class)
                        .putExtra("PaymentDetails", paymentDetails)
                        .putExtra("PaymentAmount", testPrice));
                    }

                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            
            // If the result is not ok
            else
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }

        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(this, "Invalid Result Detected!", Toast.LENGTH_SHORT).show();
        }

    }

    // Private method for processing payment
    private void processPayment()
    {
        String testPrice = "20";

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(testPrice)), "AUD",
                "Pay the food", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent paymentIntent = new Intent(FoodActivity.this, PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(paymentIntent, PAYPAL_REQUEST_CODE);
    }

    // Private method for initializing all the views
    private void InitializeFoodViews()
    {
        selectedFoodImageView = findViewById(R.id.selectedFoodImageView);
        selectedFoodTitle = findViewById(R.id.selectedFoodTitle);
        selectedFoodDesc = findViewById(R.id.selectedFoodDescTextView);
        selectedFoodDate = findViewById(R.id.selectedFoodDateTextView);
        selectedFoodTime = findViewById(R.id.selectedFoodTimeTextView);
        selectedFoodQuantity = findViewById(R.id.selectedFoodQuantityTextView);
        selectedFoodLocation = findViewById(R.id.selectedFoodLocationTextView);
        addToCartButton = findViewById(R.id.addToCartButton);
        paypalButton = findViewById(R.id.paypalButton);
    }

    // Private method to update all of the food information
    private void UpdateInformation()
    {
        // Setting up the date
        Date date = new Date(food.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String pickup_date = formatter.format(date);

        selectedFoodImageView.setImageBitmap(food.getImage());
        selectedFoodTitle.setText(food.getTitle());
        selectedFoodDesc.setText(food.getDescription());
        selectedFoodDate.setText(pickup_date);
        selectedFoodTime.setText(food.getTime());
        selectedFoodQuantity.setText(String.valueOf(food.getQuantity()));
        selectedFoodLocation.setText(food.getLocation());
    }


    private void setupLocation()
    {
        // Initialize Fragment
        Fragment fragment = new MapsFragment();

        // Data bundle for map
        Bundle mapBundle = new Bundle();

        mapBundle.putDouble("latitude", food.getLatitude());
        mapBundle.putDouble("longitude", food.getLongitude());
        mapBundle.putString("location", food.getLocation());

        // Get the bundle
        fragment.setArguments(mapBundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
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