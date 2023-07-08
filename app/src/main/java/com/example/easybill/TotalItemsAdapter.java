package com.example.easybill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TotalItemsAdapter extends CursorAdapter {

    public TotalItemsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the layout for each item in the ListView
        return LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Set the text for each item in the ListView
        TextView text1 = view.findViewById(R.id.text1);
        TextView text2 = view.findViewById(R.id.text2);

        @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex(MyDataBaseHelper.COLUMN_NAME));
        @SuppressLint("Range") String itemPrice = cursor.getString(cursor.getColumnIndex(MyDataBaseHelper.COLUMN_PRICE));

        text1.setText(itemName);
        text2.setText(itemPrice);
    }
}
