package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class QuizActivity extends AppCompatActivity {

    // Variables
    static final int TOTAL_QUESTION = 5;    // Constant of the total question available
    String username;
    int userScore, selectedAnswerButton, questionIndex;
    ProgressBar quizProgressBar;
    Button firstAnswerButton, secondAnswerButton, thirdAnswerButton, submitButton;
    TextView welcomeUserText, progressBarText, quizQuestionTitle, quizQuestionDesc;

    // Arrays of data
    String[] questionTitles, questionDescriptions;
    String[][] quizQuestionAnswers; // To contain multiple arrays of answers for different questions
    int[] correctAnswers = {3, 1, 1, 2, 3}; // Answers equivalent to corresponding buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Getting intent from MainActivity for the user's name
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Finding widgets view
        quizProgressBar = findViewById(R.id.quizProgressBar);
        firstAnswerButton = findViewById(R.id.firstAnswerButton);
        secondAnswerButton = findViewById(R.id.secondAnswerButton);
        thirdAnswerButton = findViewById(R.id.thirdAnswerButton);
        submitButton = findViewById(R.id.submitButton);

        welcomeUserText = findViewById(R.id.welcomeUserText);
        progressBarText = findViewById(R.id.progressBarText);
        quizQuestionTitle = findViewById(R.id.quizQuestionTitle);
        quizQuestionDesc = findViewById(R.id.quizQuestionDesc);

        // Setting up variables
        userScore = 0;  // Score starts at 0 points
        selectedAnswerButton = -1;    // -1 because no answer is selected
        questionTitles = getResources().getStringArray(R.array.questionTitles); // Get the question titles
        questionDescriptions = getResources().getStringArray(R.array.questionDescriptions); // Get the question descriptions
        questionIndex = 0;  // Starts at question 1

        // Setting the progress bar
        progressBarText.setText("1/" + TOTAL_QUESTION);

        // Get the array of answers from strings.xml
        TypedArray arraysOfAnswers = getResources().obtainTypedArray(R.array.questionAnswers);
        quizQuestionAnswers = new String[arraysOfAnswers.length()][3];  // First index is for how many arrays (In this case 5) and second index is for the element inside it (Which is 3 answers)

        for (int i = 0; i < arraysOfAnswers.length(); ++i)  // Loop to fill the array
        {
            int ID = arraysOfAnswers.getResourceId(i, 0);

            if (ID == 0) Toast.makeText(this, "No answers available!", Toast.LENGTH_SHORT); // Error catching if the array of answers are empty

            quizQuestionAnswers[i] = getResources().getStringArray(ID);
        }

        arraysOfAnswers.recycle();  // To clear the pointer since it won't be used anymore

        welcomeUserText.setText("Welcome " + username + "!");

        // Setting up the first question
        quizQuestionTitle.setText(questionTitles[quizProgressBar.getProgress() - 1]);   // Subtract 1 because the progress bar starts at 1 (Min 1, Max 5)
        quizQuestionDesc.setText(questionDescriptions[quizProgressBar.getProgress() - 1]);
        firstAnswerButton.setText(quizQuestionAnswers[quizProgressBar.getProgress() - 1][0]);   // 0 Means the first index / answer of the answers array
        secondAnswerButton.setText(quizQuestionAnswers[quizProgressBar.getProgress() - 1][1]);
        thirdAnswerButton.setText(quizQuestionAnswers[quizProgressBar.getProgress() - 1][2]);

        //Testing purposes
        firstAnswerButton.setBackgroundColor(0xFFFF0000);
        //quizProgressBar.setProgress(3);
    }


    // Public Methods

    // Main method which the quiz revolves around
    // This method will run after user presses submit on the first question
    // It will then run every time user presses next / submit button


    // This method is used to highlight the answer button that the user chose
    public void firstAnswerButtonSelected(View view)
    {
        // Make the selected button blue and the rest gray
        firstAnswerButton.setBackgroundColor(Color.BLUE);
        secondAnswerButton.setBackgroundColor(Color.GRAY);
        thirdAnswerButton.setBackgroundColor(Color.GRAY);
        selectedAnswerButton = 1;   // User selected the first button
    }






    // Private Methods

    // This method will be used to reset the answer buttons after user changes quiz question
    // The colors of the buttons will be set back to its normal color which is gray
    private void resetAnswerButtons()
    {

        firstAnswerButton.setBackgroundColor(Color.GRAY);
        secondAnswerButton.setBackgroundColor(Color.GRAY);
        thirdAnswerButton.setBackgroundColor(Color.GRAY);
    }


    // This method is used to make the button clickable and non-clickable
    // It is used so the user can't click the button during specified state which can cause error
    private void toggleButtonClick()
    {
        if (firstAnswerButton.isClickable()) // Only check one, because if one is clickable the rest are too
        {
            firstAnswerButton.setClickable(false);
            secondAnswerButton.setClickable(false);
            thirdAnswerButton.setClickable(false);
        }
        else
        {
            firstAnswerButton.setClickable(true);
            secondAnswerButton.setClickable(true);
            thirdAnswerButton.setClickable(true);
        }
    }


    // Method to change the quiz question
    // This method will take the questions title and desc from their array in strings.xml
    // It will also take the answers from array of arrays which is created at OnCreate()
    private void changeQuizQuestion()
    {
        // Changes the question title and desc
        quizQuestionTitle.setText(questionTitles[quizProgressBar.getProgress() - 1]);
        quizQuestionDesc.setText(questionDescriptions[quizProgressBar.getProgress() - 1]);

        // Changes the answer on the buttons
        firstAnswerButton.setText(quizQuestionAnswers[quizProgressBar.getProgress() - 1][0]);
        secondAnswerButton.setText(quizQuestionAnswers[quizProgressBar.getProgress() - 1][1]);
        thirdAnswerButton.setText(quizQuestionAnswers[quizProgressBar.getProgress() - 1][2]);
    }


    //






}