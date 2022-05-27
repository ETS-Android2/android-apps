package com.example.FoodRescueApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.Food;
import com.example.FoodRescueApp.util.PaypalUtil;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartActivity extends AppCompatActivity {

    // Variables
    TextView cartFoodTotalPrice;
    ImageButton paypalButton;

    RecyclerView cartRecyclerView;
    CartRowAdapter cartRowAdapter;

    DatabaseHelper db;
    ArrayList<Integer> cartList = new ArrayList<>();
    List<Food> foodList = new ArrayList<>();

    // Paypal Util
    String testPrice = "20";
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)   // Using sandbox because we're testing
            .clientId(PaypalUtil.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Assigning Views
        cartFoodTotalPrice = findViewById(R.id.cartFoodTotalPrice);
        paypalButton = findViewById(R.id.paypalButton2);

        // Getting the cart data
        cartList = getIntent().getIntegerArrayListExtra("cartListKey");

        // Initializing Database
        db = new DatabaseHelper(this);
        foodList = db.fetchAllCartFood(cartList);

        // RecyclerView Setup
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRowAdapter = new CartRowAdapter(this, foodList);
        cartRecyclerView.setAdapter(cartRowAdapter);

        // Changing Recycler View Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(layoutManager);


        // Paypal Service Setup
        Intent paypalIntent = new Intent(this, PayPalService.class);
        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(paypalIntent);


        // OnClickListener for PayPal Button
        paypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    // Will run when activity is destroyed
    @Override
    protected void onDestroy()
    {
        stopService(new Intent( CartActivity.this, PayPalService.class));
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

                        startActivity(new Intent(CartActivity.this, PayPalPaymentDetails.class)
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

        Intent paymentIntent = new Intent(CartActivity.this, PaymentActivity.class);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(paymentIntent, PAYPAL_REQUEST_CODE);
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