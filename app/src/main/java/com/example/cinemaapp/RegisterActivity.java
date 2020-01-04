package com.example.cinemaapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getCanonicalName();


    private Button datePickerButton;
    private TextView birthDateView, birthDateViewL;
    private EditText fnameET;
    private EditText lnameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText confpasswordET;
    private Button registerBtn;
    private String birthDate;
    DatabaseHelper db;
    String fnameS, lnameS, emailS, passwordS, confirmPasswordS, birthDateS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        db = new DatabaseHelper(this);

        fnameET = findViewById(R.id.firstName);
        lnameET = findViewById(R.id.lastName);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        confpasswordET = findViewById(R.id.confirmPassword);
        birthDateView = findViewById(R.id.birthDate);
        registerBtn = findViewById(R.id.registerButton);

        datePickerButton = findViewById(R.id.date_picker);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthDate = year + "." + month + "." + dayOfMonth;
                        Log.d(TAG, "Date: " + birthDate);
                        birthDateView.setText(birthDate);
                    }
                });
                dialog.show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnameS = fnameET.getText().toString();
                lnameS = lnameET.getText().toString();
                emailS = emailET.getText().toString();
                passwordS = passwordET.getText().toString();
                confirmPasswordS = confpasswordET.getText().toString();
                birthDateS = birthDateView.getText().toString();
                if (checkDataEntered() == true) {
                    if (passwordS.equals(confirmPasswordS)) {
                        if (db.checkEmail(emailS) == true) {
                            boolean insert = db.insertUser(fnameS, lnameS, emailS, passwordS, birthDateS);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginFragment.class);
                                //intent.putExtra("email",emailS);
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Enter other email", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "The passwords don't match", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }


    //check data entered
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmptyT(TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    boolean checkDataEntered() {
        boolean dataValidation = true;
        if (isEmpty(fnameET)) {
            fnameET.setError("First name is required!");
            dataValidation = false;
        }

        if (isEmpty(lnameET)) {
            lnameET.setError("Last name is required!");
            dataValidation = false;
        }

        if (isEmail(emailET) == false) {
            emailET.setError("Enter valid email!");
            dataValidation = false;
        }

        if (isEmpty(passwordET)) {
            passwordET.setError("Password is required!");
            dataValidation = false;
        }
        if (isEmpty(confpasswordET)) {
            confpasswordET.setError("Confirm password is required!");
            dataValidation = false;
        }
        if (isEmptyT(birthDateView)) {
            datePickerButton.setError("Birth date is required!");
            dataValidation = false;
        }
        return dataValidation;
    }
}