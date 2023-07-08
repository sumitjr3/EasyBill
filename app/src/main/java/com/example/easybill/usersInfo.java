package com.example.easybill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class usersInfo extends AppCompatActivity {


    EditText shopNameText, shopAddressText, ownerNameText, phoneNumberText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);


        submitButton = findViewById(R.id.userInfoButton);
        shopNameText = findViewById(R.id.editTextText);
        shopAddressText = findViewById(R.id.editTextTextPostalAddress);
        ownerNameText = findViewById(R.id.editTextTextPostalAddress);
        phoneNumberText = findViewById(R.id.editTextPhone);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean initialSetupCompleted = sharedPreferences.getBoolean("initial_setup_completed", false);


        if (initialSetupCompleted) {
            Intent intent = new Intent(usersInfo.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "please enter details", Toast.LENGTH_LONG).show();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameZ = shopNameText.getText().toString();
                String addressZ = shopAddressText.getText().toString();
                String oNameZ = ownerNameText.getText().toString();
                String numberZ = phoneNumberText.getText().toString();


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.name), nameZ);
                editor.putString(getString(R.string.address),addressZ);
                editor.putString(getString(R.string.ownerName),oNameZ);
                editor.putString(getString(R.string.phoneNumber),numberZ);


                editor.putBoolean("initial_setup_completed", true);
                editor.apply();


                Intent intentF = new Intent(usersInfo.this,MainActivity.class);
                startActivity(intentF);
                finish();
            }
        });



    }




}