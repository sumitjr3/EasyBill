package com.example.easybill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import java.util.ArrayList;

public class customAdapter extends BaseAdapter {



    private final ArrayList<ProductList> dataList;
    private final LayoutInflater inflater;

    public customAdapter(Context context, ArrayList<ProductList> dataList) {
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.column1TextView = view.findViewById(R.id.text1);
            holder.column2TextView = view.findViewById(R.id.text2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.column1TextView.setText(dataList.get(position).getInput1());
        holder.column2TextView.setText(dataList.get(position).getInput2());

        return view;
    }

    private static class ViewHolder {
        TextView column1TextView;
        TextView column2TextView;
    }


}
