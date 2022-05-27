package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Declaring Variables
    long workoutTime, timeWhenStopped;    // Total time spent working out & the time when its stopped
    String workoutType;     // The type of workout
    Boolean isRunning;

    // Declaring Widgets Variables
    TextView workoutHoursText, workoutTypeText;
    EditText workoutTypeEditText;
    Chronometer workoutTimer;
    Button startButton, pauseButton, stopButton;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the views
        workoutHoursText = findViewById(R.id.workoutHoursText);
        workoutTypeText = findViewById(R.id.workoutTypeText);
        workoutTypeEditText = findViewById(R.id.workoutTypeEditText);
        workoutTimer = findViewById(R.id.workoutTimer);

        isRunning = false;

        sharedPreferences = getSharedPreferences("com.example.workouttimer", MODE_PRIVATE);
        checkSharedPreferences();

        // Check if instance already exist
        if (savedInstanceState != null)
        {
            // Collecting data from last instance
            workoutTime = savedInstanceState.getLong("time");
            isRunning = savedInstanceState.getBoolean("state");

            workoutHoursText.setText(savedInstanceState.getString("text"));

            workoutTimer.setBase(SystemClock.elapsedRealtime() + workoutTime);

            if (isRunning) workoutTimer.start();
        }

        else
        {
            workoutHoursText.setText(R.string.workoutHoursTextString);
            workoutTimer.setBase(SystemClock.elapsedRealtime());
        }

    }


    // This method is used to save the time when instance state is changed
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean("state", isRunning);  // Saves the current state of the timer
        outState.putLong("time", workoutTimer.getBase() - SystemClock.elapsedRealtime());   // Saves the current time
        outState.putString("text", workoutHoursText.getText().toString());  // Saves the text
        super.onSaveInstanceState(outState);
    }


    // This method is for handling the button presses
    // It will handle 3 different buttons:
    // Start, Pause, and Stop
    public void onTimerClick(View view)
    {
        switch (view.getId())
        {
            // This will run if the user presses the start button
            case R.id.startButton:

                if (isRunning)
                {
                    Toast.makeText(this, "Timer is already running!", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(this, "Started the timer", Toast.LENGTH_SHORT).show();

                    workoutTimer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);    // Setting the base so it doesn't continue counting in the background based on real time elapsed and continuing the time since paused
                    workoutTimer.start();   // Starts the timer
                    isRunning = true;
                }
            break;


            // This will run if the user presses the pause button
            case R.id.pauseButton:

                if (isRunning)
                {
                    Toast.makeText(this, "Paused the timer", Toast.LENGTH_SHORT).show();

                    workoutTimer.stop();    // Pauses the timer
                    timeWhenStopped = workoutTimer.getBase() - SystemClock.elapsedRealtime();
                    isRunning = false;
                }

                else
                {
                    Toast.makeText(this, "Timer is already paused!", Toast.LENGTH_SHORT).show();
                }
            break;


            // This will run if the user presses the stop button
            case R.id.stopButton:
                Toast.makeText(this, "Stopped the timer", Toast.LENGTH_SHORT).show();
                editWorkoutHours(workoutTimer.getText());

                if (workoutTypeEditText.getText().toString().equals(""))
                {
                    workoutType = null;
                }

                else
                {
                    workoutType = workoutTypeEditText.getText().toString();
                }

                workoutTimer.stop();
                workoutTimer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped = 0;    // Resets the time when stopped variable
                isRunning = false;
                saveData();

            break;

            // This will run if value other than the 3 button is passed
            default:
                throw new IllegalStateException("Unexpected value" + view.getId());
        }
    }


    // This method is used to edit the workout hours text
    private void editWorkoutHours(CharSequence time)
    {
        // Check the edit text to find if the workout type is empty or not
        if (workoutTypeEditText.getText().toString().equals(""))
        {
            workoutHoursText.setText("You spent " + time.toString()  + " on a workout last time.");
        }
        else
        {
            workoutHoursText.setText("You spent " + time.toString() + " on " + workoutTypeEditText.getText().toString() + " last time.");
        }

    }



    // Method overloading where it accepts type as parameter
    private void editWorkoutHours(String type)
    {
        if (type == null)
        {
            workoutHoursText.setText("You spent " + "00:00" + " on a workout last time.");
        }
        else
        {
            workoutHoursText.setText("You spent " + "00:00" + " on " + type + " last time.");
        }


    }


    // Method to save the data
    private void saveData()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("text", workoutHoursText.getText().toString());
        editor.apply();
    }


    // Method to check the shared preference and set the text accordingly
    public void checkSharedPreferences()
    {
        String text = sharedPreferences.getString("text", "");
        workoutHoursText.setText(text);
    }
}

