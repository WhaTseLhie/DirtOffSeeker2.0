package com.example.jayvee.dirtoffseeker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Booking> list;
    LayoutInflater inflater;

    public BookingListAdapter(Context context, ArrayList<Booking> list) {
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
        BookingHandler handler;

        if(view == null) {
            handler = new BookingHandler();
            view = inflater.inflate(R.layout.adapter_booking_list_, null);

            handler.imgPic = view.findViewById(R.id.imageView);
            handler.txtName = view.findViewById(R.id.textView);
            handler.txtDate = view.findViewById(R.id.textView1);
            handler.txtTime = view.findViewById(R.id.textView2);
            handler.imgStatus = view.findViewById(R.id.imageView1);
            view.setTag(handler);
        } else {
            handler = (BookingHandler) view.getTag();
        }

        //handler.imgPic.setImageURI();
        handler.txtName.setText(new StringBuilder().append(list.get(i).getLaundWorker_fn()).append(" ").append(list.get(i).getLaundWorker_mn()).append(" ").append(list.get(i).getLaundWorker_ln()));
        handler.txtDate.setText(list.get(i).getBooking_date());
        handler.txtTime.setText(list.get(i).getBooking_time());
        if(list.get(i).getBooking_status().equalsIgnoreCase("Done")) {
            handler.imgStatus.setImageResource(R.drawable.ic_status_done);
        } else if(list.get(i).getBooking_status().equalsIgnoreCase("Progress")) {
            handler.imgStatus.setImageResource(R.drawable.ic_status_progress);
        } else if(list.get(i).getBooking_status().equalsIgnoreCase("Pending")) {
            handler.imgStatus.setImageResource(R.drawable.ic_status_pending);
        } else if(list.get(i).getBooking_status().equalsIgnoreCase("Canceled")) {
            handler.imgStatus.setImageResource(R.drawable.ic_status_canceled);
        }

        return view;
    }

    static class BookingHandler {
        ImageView imgPic, imgStatus;
        TextView txtName, txtDate, txtTime;
    }
}