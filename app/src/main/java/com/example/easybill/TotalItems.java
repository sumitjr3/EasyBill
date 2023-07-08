package com.example.easybill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TotalItems extends AppCompatActivity {

    private ListView listView;
    private SimpleCursorAdapter adapter;
    private MyDataBaseHelper myDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_items);



        listView = findViewById(R.id.listView);

        myDataBaseHelper = new MyDataBaseHelper(this);


        MyDataBaseHelper myDbHelper = new MyDataBaseHelper(this);
        Cursor cursor = myDbHelper.getAllItems();

        String[] fromColumns = {MyDataBaseHelper.COLUMN_NAME, MyDataBaseHelper.COLUMN_PRICE};
        int[] toViews = {R.id.text1, R.id.text2};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_layout, cursor, fromColumns, toViews, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor selectedCursor = (Cursor) adapter.getItem(position);
                @SuppressLint("Range") String selectedItem = selectedCursor.getString(selectedCursor.getColumnIndex(MyDataBaseHelper.COLUMN_NAME));
                @SuppressLint("Range") String selectedPrice = selectedCursor.getString(selectedCursor.getColumnIndex(MyDataBaseHelper.COLUMN_PRICE));

                // Create an AlertDialog to prompt for the quantity
                AlertDialog.Builder builder = new AlertDialog.Builder(TotalItems.this);
                builder.setTitle("Quantity and Discount");

                View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
                builder.setView(dialogView);

                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText quantityInput = dialogView.findViewById(R.id.quantity);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText discountInput = dialogView.findViewById(R.id.discount);

                // Set input types for quantity and discount EditText fields
                quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                discountInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                builder.setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String quantity = quantityInput.getText().toString();
                                String discount = discountInput.getText().toString();

                                // Create an intent to pass the selected item, price, and quantity back to the NewBill activity
                                Intent intent = new Intent();

                                int pp = Integer.parseInt(selectedPrice);
                                int pq = Integer.parseInt(quantity);
                                int d = Integer.parseInt(discount);

                                //calculating price after discount applied
                                float priceNow = pp - ((d * pp) / 100);

                                /* just converting String values into a integer value for
                                 calculation and then converting it back to String value */
                                String total = String.valueOf(priceNow * pq);
                                String price = String.valueOf(priceNow);


                                intent.putExtra("selectedItem", selectedItem);
                                intent.putExtra("selectedPrice", price);
                                intent.putExtra("selectedQuantity", quantity);
                                intent.putExtra("selectedTotal", total);

                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                });

                dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showDeleteConfirmationDialog(id);

                return true;
            }
        });



    }//on create



    private void showDeleteConfirmationDialog(final long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(TotalItems.this);
        builder.setTitle("Delete Item");
        builder.setMessage("Delete Item?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(id); // Call the method to delete the item
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();

    }

    private void deleteItem(long id) {

        MyDataBaseHelper myDbHelper = new MyDataBaseHelper(this);
        boolean isDeleted = myDbHelper.deleteItem(id);

        if (isDeleted) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            refreshListView(); // Call a method to refresh the ListView after deletion
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }

    }


    private void refreshListView() {
        Cursor cursor = myDataBaseHelper.getAllItems();
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
    }


}