package com.example.FoodRescueApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.FoodRescueApp.data.DatabaseHelper;
import com.example.FoodRescueApp.model.Food;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFoodActivity extends AppCompatActivity {

    // Declaring Variables
    EditText titleEditText, descriptionEditText, timeEditText, quantityEditText, locationEditText;
    ImageView addImageView;
    Button addImageButton, saveFoodButton;
    CalendarView foodCalendarView;
    Bitmap image;


    public static final int PICK_IMAGE = 1;    // Constant for when the user comes back

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Assigning Variables
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        timeEditText = findViewById(R.id.timeEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        locationEditText = findViewById(R.id.locationEditText);

        addImageView = findViewById(R.id.addImageView);
        addImageButton = findViewById(R.id.addImageButton);
        saveFoodButton = findViewById(R.id.saveFoodButton);
        foodCalendarView = findViewById(R.id.foodCalendarView);

        DatabaseHelper db = new DatabaseHelper(this);

        int currentUserId = getIntent().getIntExtra("currentUserKey", -2);

        // OnClickListener for save food button
        // Will also check if any of the fields are empty
        saveFoodButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkAllInputs() == true)
                {
                    int userId = currentUserId;
                    String title = titleEditText.getText().toString();
                    String desc = descriptionEditText.getText().toString();
                    long date = foodCalendarView.getDate();
                    String pickupTime = timeEditText.getText().toString();
                    int quantity = Integer.parseInt(quantityEditText.getText().toString());
                    String location = locationEditText.getText().toString();



                    DateFormat formatter = new SimpleDateFormat("hh:mm");
                    Date time = new Date(0);

                    try
                    {
                        time = formatter.parse(pickupTime);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AddFoodActivity.this, "Wrong time format (Must be HH:MM)!", Toast.LENGTH_SHORT).show();
                    }


                    // Create a new food object
                    Food food = new Food();

                    food.setUser_id(userId);

                    try{
                        int checkImage = image.getHeight(); // To check if the user has picked an image or not
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(AddFoodActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }

                    food.setImage(image);
                    food.setTitle(title);
                    food.setDescription(desc);
                    food.setDate(date);
                    food.setTime(time);
                    food.setQuantity(quantity);
                    food.setLocation(location);

                long result = db.insertFood(food);

                if (result > 0)
                {
                    Toast.makeText(AddFoodActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                    Intent backIntent = new Intent(AddFoodActivity.this, HomeActivity.class);
                    startActivity(backIntent);
                    finish();
                }

                else Toast.makeText(AddFoodActivity.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // OnClickListener for add image button
        // Will request permission from the user to retrieve image from the gallery
        addImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // If the user still hasn't added permission to read external storage
                    if (ActivityCompat.checkSelfPermission(AddFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(AddFoodActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PICK_IMAGE);
                    }

                    else
                    {
                        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
                        pickImageIntent.setType("image/*");
                        startActivityForResult(pickImageIntent, PICK_IMAGE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    // Overriding method from intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            Uri imageUri = data.getData();
            InputStream inputStream;

            try
            {
                inputStream = getContentResolver().openInputStream(imageUri);
                image = BitmapFactory.decodeStream(inputStream);

                addImageView.setImageURI(imageUri);
            }
            
            catch (Exception e)
            {
                Toast.makeText(this, "Error, file not found!", Toast.LENGTH_SHORT).show();
            }


        }

        else
        {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

    }

    // Overriding method on request permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PICK_IMAGE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
                startActivityForResult(pickImageIntent, PICK_IMAGE);
            }
        }
        else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    // Private method which will be used to get image path
    private String getImagePath(Uri uri)
    {
        if (uri == null) return null;   // No image picked


        String[] x = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, x, null, null, null);

        if (cursor != null)
        {
            // Get the column index of the selected image
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);  // Path of the selected image
        }
        return uri.getPath();
    }
    */

    private boolean checkAllInputs()
    {
        String title = titleEditText.getText().toString();
        String desc = descriptionEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String location = locationEditText.getText().toString();

        if (title.equals("") || desc.equals("") || time.equals("") || quantity.equals("") ||
        location.equals(""))
        {
            Toast.makeText(this, "Please fill all empty fields!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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