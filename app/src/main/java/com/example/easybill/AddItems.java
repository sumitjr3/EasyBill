package com.example.easybill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Button button = findViewById(R.id.button);
        TextInputEditText nameInput = findViewById(R.id.name);
        TextInputEditText priceInput = findViewById(R.id.price);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameInput.getText().toString();
                String price = priceInput.getText().toString();

                ContentValues values = new ContentValues();

                values.put(MyDataBaseHelper.COLUMN_NAME, name);
                values.put(MyDataBaseHelper.COLUMN_PRICE, price);

                long result = db.insert(MyDataBaseHelper.TABLE_NAME, null, values);

                if(result==-1){
                    Toast.makeText(AddItems.this, "error adding item", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddItems.this, "item added successfully", Toast.LENGTH_LONG).show();
                    nameInput.setText("");
                    priceInput.setText("");
                }

            }
        });

    }
}