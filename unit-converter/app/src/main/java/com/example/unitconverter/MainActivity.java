package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Instance creation
    TextView titleText;
    TextView firstResultText;
    TextView secondResultText;
    TextView thirdResultText;

    TextView firstUnitText;
    TextView secondUnitText;
    TextView thirdUnitText;

    Spinner dataSpinner;
    EditText userEditText;


    // The arrays
    String[] mainUnit = {"Meter", "Celsius", "Kilogram"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the views
        titleText = findViewById(R.id.titleText);

        firstResultText = findViewById(R.id.firstResultText);
        secondResultText = findViewById(R.id.secondResultText);
        thirdResultText = findViewById(R.id.thirdResultText);

        firstUnitText = findViewById(R.id.firstUnitText);
        secondUnitText = findViewById(R.id.secondUnitText);
        thirdUnitText = findViewById(R.id.thirdUnitText);

        userEditText = findViewById(R.id.userEditText);
        dataSpinner = findViewById(R.id.dataSpinner);

        dataSpinner.setOnItemSelectedListener(this);

        // Creating adapter for the spinner
        ArrayAdapter mainUnitsAA = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, mainUnit);

        // Set the layout style
        mainUnitsAA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Attaching the data adapter to the spinner
        dataSpinner.setAdapter(mainUnitsAA);

    }

    // Function for length calculation
    public void LengthButton(View view)
    {
        int number = dataSpinner.getSelectedItemPosition();

        if (number == 0)
        {
            titleText.setText("Length Converter");
            String checkInput = userEditText.getText().toString();

            if (checkInput.matches(""))   // If user did not put any input
            {
                Toast.makeText(this, "You did not enter any number", Toast.LENGTH_SHORT).show();
                firstResultText.setText("");
                secondResultText.setText("");
                thirdResultText.setText("");
                firstUnitText.setText("");
                secondUnitText.setText("");
                thirdUnitText.setText("");
            }

            else
            {
                double result = Double.parseDouble(userEditText.getText().toString());

                double firstResult = result * 100;
                firstResult = Math.round(firstResult * 100.0) / 100.0; // Rounding off to 2 decimal
                firstResultText.setText(Double.toString(firstResult));
                firstUnitText.setText("Centimeter");

                double secondResult = result * 3.281;
                secondResult = Math.round(secondResult * 100.0) / 100.0;
                secondResultText.setText(Double.toString(secondResult));
                secondUnitText.setText("Foot");

                double thirdResult = result * 39.37;
                thirdResult = Math.round(thirdResult * 100.0) / 100.0;
                thirdResultText.setText(Double.toString(thirdResult));
                thirdUnitText.setText("Inch");

            }
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Please select correct conversion icon", Toast.LENGTH_LONG).show();
            firstResultText.setText("");
            secondResultText.setText("");
            thirdResultText.setText("");
            firstUnitText.setText("");
            secondUnitText.setText("");
            thirdUnitText.setText("");
        }
    }

    public void TempButton(View view)
    {
        int number = dataSpinner.getSelectedItemPosition();

        if (number == 1)
        {
            titleText.setText("Temperature Converter");
            String checkInput = userEditText.getText().toString();
            thirdResultText.setText("");
            thirdUnitText.setText("");

            if (checkInput.matches(""))
            {
                Toast.makeText(this, "You did not enter any number", Toast.LENGTH_SHORT).show();
                firstResultText.setText("");
                secondResultText.setText("");
                firstUnitText.setText("");
                secondUnitText.setText("");
            }

            else
            {
                double result = Double.parseDouble(userEditText.getText().toString());

                double firstResult = (result * 1.8) + 32;
                firstResult = Math.round(firstResult * 100.0) / 100.0;
                firstResultText.setText(Double.toString(firstResult));
                firstUnitText.setText("Fahrenheit");

                double secondResult = result + 273.15;
                secondResult = Math.round(secondResult * 100.0) / 100.0;
                secondResultText.setText(Double.toString(secondResult));
                secondUnitText.setText("Kelvin");
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Please select correct conversion icon", Toast.LENGTH_LONG).show();
            firstResultText.setText("");
            secondResultText.setText("");
            thirdResultText.setText("");
            firstUnitText.setText("");
            secondUnitText.setText("");
            thirdUnitText.setText("");
        }
    }

    public void WeightButton(View view)
    {
        int number = dataSpinner.getSelectedItemPosition();

        if (number == 2)
        {
            titleText.setText("Weight Converter");
            String checkInput = userEditText.getText().toString();

            if (checkInput.matches(""))
            {
                Toast.makeText(this, "You did not enter any number", Toast.LENGTH_SHORT).show();
                firstResultText.setText("");
                secondResultText.setText("");
                thirdResultText.setText("");
                firstUnitText.setText("");
                secondUnitText.setText("");
                thirdUnitText.setText("");
            }

            else
            {
                double result = Double.parseDouble(userEditText.getText().toString());

                double firstResult = result * 1000;
                firstResult = Math.round(firstResult * 100.0) / 100.0;
                firstResultText.setText(Double.toString(firstResult));
                firstUnitText.setText("Gram");

                double secondResult = result * 35.274;
                secondResult = Math.round(secondResult * 100.0) / 100.0;
                secondResultText.setText(Double.toString(secondResult));
                secondUnitText.setText("Ounce(Oz)");

                double thirdResult = result * 2.205;
                thirdResult = Math.round(thirdResult * 100.0) / 100.0;
                thirdResultText.setText(Double.toString(thirdResult));
                thirdUnitText.setText("Pound(lb)");

            }

        }

        else
        {
            Toast.makeText(getApplicationContext(), "Please select correct conversion icon", Toast.LENGTH_LONG).show();
            firstResultText.setText("");
            secondResultText.setText("");
            thirdResultText.setText("");
            firstUnitText.setText("");
            secondUnitText.setText("");
            thirdUnitText.setText("");
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Showing toast on the selected spinner item
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}