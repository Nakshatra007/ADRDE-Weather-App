package com.example.adrdeweatherapp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private String[][] data;

    public GridAdapter(Context context, String[][] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
        }

        String[] rowData = data[position];

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        TextView text3 = convertView.findViewById(R.id.text3);
        TextView text4 = convertView.findViewById(R.id.text4);
        TextView text5 = convertView.findViewById(R.id.text5);
        TextView text6 = convertView.findViewById(R.id.text6);

        text1.setText(""+rowData[0]);
        text2.setText(""+rowData[1]);
        text3.setText(""+rowData[2]);
        text4.setText(""+rowData[3]);
        text5.setText(""+rowData[4]);
        text6.setText(""+rowData[5]);

        // Set the text alignment to center
        text1.setGravity(Gravity.CENTER);
        text2.setGravity(Gravity.CENTER);
        text3.setGravity(Gravity.CENTER);
        text4.setGravity(Gravity.CENTER);
        text5.setGravity(Gravity.CENTER);
        text6.setGravity(Gravity.CENTER);

        // Change the background color of the first row (header)
        if (position == 0) {
            convertView.setBackgroundColor(Color.GRAY);
            text1.setTextColor(Color.WHITE);
            text2.setTextColor(Color.WHITE);
            text3.setTextColor(Color.WHITE);
            text4.setTextColor(Color.WHITE);
            text5.setTextColor(Color.WHITE);
            text6.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            text1.setTextColor(Color.BLACK);
            text2.setTextColor(Color.BLACK);
            text3.setTextColor(Color.BLACK);
            text4.setTextColor(Color.BLACK);
            text5.setTextColor(Color.BLACK);
            text6.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
