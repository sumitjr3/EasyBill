package com.example.easybill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button addButton, totalButton, newButton, prevButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.button);
        totalButton = findViewById(R.id.button2);
        newButton = findViewById(R.id.button3);
        prevButton = findViewById(R.id.button4);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean initialSetupCompleted = sharedPreferences.getBoolean("initial_setup_completed", false);

        if (initialSetupCompleted) {

        } else {
            Intent intent = new Intent(MainActivity.this,usersInfo.class);
            startActivity(intent);
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this,AddItems.class);
                startActivity(addIntent);
            }
        });

        totalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent totalIntent = new Intent(MainActivity.this,TotalItems.class);
                startActivity(totalIntent);
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this,NewBill.class);
                startActivity(newIntent);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prevIntent = new Intent(MainActivity.this,TotalItems.class);
                startActivity(prevIntent);
            }
        });




    }


}