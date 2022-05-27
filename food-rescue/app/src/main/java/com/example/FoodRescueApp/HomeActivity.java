package com.example.FoodRescueApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements FoodRowAdapter.OnFoodClickListener{

    // Declaring Variables
    TextView homeTextView;
    ImageButton menuImageButton;
    FloatingActionButton addFoodButton;

    List<Food> foodList = new ArrayList<>();
    RecyclerView foodRecyclerView;
    FoodRowAdapter foodRowAdapter;
    ArrayList<Integer> cartList = new ArrayList<>();

    // Intent Variables
    static int LAUNCH_FOOD_ACTIVITY = 1;
    static int RESULT_ADD_CART = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Assigning Variables
        homeTextView = findViewById(R.id.homeTextView);
        menuImageButton = findViewById(R.id.menuImageButton);
        addFoodButton = findViewById(R.id.addFoodButton);

        // Getting the user id
        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);

        // Initializing database and filling the food list
        DatabaseHelper db = new DatabaseHelper(this);
        foodList = db.fetchAllFoods();

        // Recycler View setup
        foodRecyclerView = findViewById(R.id.foodRecyclerVIew);
        foodRowAdapter = new FoodRowAdapter(foodList, this, this);
        foodRecyclerView.setAdapter(foodRowAdapter);

        // Layout for recycler view to make it vertical
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        foodRecyclerView.setLayoutManager(layoutManager);
    }


    // Method to handle the add food button
    public void addFoodButtonClick(View view)
    {
        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);

        Intent addFoodIntent = new Intent(this, AddFoodActivity.class);
        addFoodIntent.putExtra("currentUserKey", currentUserId);
        startActivity(addFoodIntent);
    }


    // Method to handle the popup menu button
    public void showPopUp(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();

        inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

        // OnClickListener for the pop menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.Home:
                        changeToHome();
                        return true;

                    case R.id.Account:
                        Intent accountIntent = new Intent(HomeActivity.this, AccountActivity.class);
                        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);
                        accountIntent.putExtra("currentUserKey", currentUserId);
                        startActivity(accountIntent);
                        return true;

                    case R.id.MyList:
                        changeToUserList();
                        return true;

                    case R.id.Cart:
                        Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
                        cartIntent.putIntegerArrayListExtra("cartListKey", cartList);
                        startActivity(cartIntent);
                        return true;

                    default:
                        Toast.makeText(HomeActivity.this, "Unknown View!", Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        });

        popupMenu.show();

    }

    // Method for changing the home view to home
    private void changeToHome()
    {
        homeTextView.setText("DISCOVER FREE FOOD");
        DatabaseHelper db = new DatabaseHelper(this);
        foodList = db.fetchAllFoods();

        // Recycler View setup
        foodRecyclerView = findViewById(R.id.foodRecyclerVIew);
        foodRowAdapter = new FoodRowAdapter(foodList, this, this);
        foodRecyclerView.setAdapter(foodRowAdapter);

        // Layout for recycler view to make it vertical
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        foodRecyclerView.setLayoutManager(layoutManager);
    }

    // Method to change the home view to user's list
    private void changeToUserList()
    {
        DatabaseHelper db = new DatabaseHelper(this);

        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);
        String currentUserEmail = getIntent().getStringExtra("currentUserEmail");

        String name = db.fetchUserName(currentUserEmail);
        homeTextView.setText(name + "'s Foods");

        foodList = db.fetchAllUserFoods(currentUserId);

        // Recycler View setup
        foodRecyclerView = findViewById(R.id.foodRecyclerVIew);
        foodRowAdapter = new FoodRowAdapter(foodList, this, this);
        foodRecyclerView.setAdapter(foodRowAdapter);

        // Layout for recycler view to make it vertical
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        foodRecyclerView.setLayoutManager(layoutManager);
    }

    // To return to login screen
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFoodClick(int position)
    {
        Intent foodIntent = new Intent(HomeActivity.this, FoodActivity.class);

        // Create new food object
        Food clickedFood = foodList.get(position);

        // Get data and pass it to the intent
        int selectedID = clickedFood.getFood_id() + 1;

        foodIntent.putExtra("selectedFoodID", selectedID);

        // Start activity for result to check if user added the food to cart or not
        startActivityForResult(foodIntent, LAUNCH_FOOD_ACTIVITY);
    }

    @Override
    public void onShareClick(int position)
    {
        Food clickedFood = foodList.get(position);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String shareHead = "Check out this food - " + clickedFood.getTitle();
        String shareBody = clickedFood.getShortDescription();

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareHead);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(shareIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // If the activity result is for the selected food activity
        if (requestCode == LAUNCH_FOOD_ACTIVITY)
        {
            if (resultCode == RESULT_ADD_CART)
            {
                // Get the food ID
                int currentFoodID = data.getIntExtra("addedFoodID", -1);
                if (currentFoodID == -1) throw new IllegalStateException("Unknown food ID detected");

                if (cartList.contains(currentFoodID))   // If the cart already have the ID
                {
                    Toast.makeText(this, "Food already in the cart!", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    // Add the food ID to cart
                    cartList.add(currentFoodID);
                    Toast.makeText(this, "Added food to cart", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}