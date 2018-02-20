package com.example.jayvee.dirtoffseeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAvailabilityMonthlyAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyAvailabilityMonthly> list;
    LayoutInflater inflater;

    public MyAvailabilityMonthlyAdapter(Context context, ArrayList<MyAvailabilityMonthly> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemHandler handler;

        if(view == null) {
            view = inflater.inflate(R.layout.activity_myavailability_monthly_adapter, null);
            handler = new ItemHandler();

            handler.txtStartMonth = view.findViewById(R.id.txtMonthStart);
            handler.txtDate = view.findViewById(R.id.textView);
            handler.txtStartTime = view.findViewById(R.id.textView1);
            handler.txtEndTime = view.findViewById(R.id.textView2);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        handler.txtStartMonth.setText(new StringBuilder().append("Month: ").append(list.get(i).getAvailability_startMonth()));
        handler.txtDate.setText(new StringBuilder().append("Date: ").append(list.get(i).getAvailability_date()));
        handler.txtStartTime.setText(new StringBuilder().append("Start Time: ").append(list.get(i).getAvailability_startTime()));
        handler.txtEndTime.setText(new StringBuilder().append("End Time: ").append(list.get(i).getAvailability_endTime()));

        return view;
    }

    static class ItemHandler {
        TextView txtStartMonth, txtDate, txtStartTime, txtEndTime;
    }
}