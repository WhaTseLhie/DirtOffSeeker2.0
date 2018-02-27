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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookingListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Booking> list;
    LayoutInflater inflater;
    BookingHandler handler;

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

        Picasso.with(view.getContext()).load(list.get(i).getLaundWorker_pic()).transform(new CircleTransform()).into(handler.imgPic);
        handler.txtName.setText(new StringBuilder().append(list.get(i).getLaundWorker_fn()).append(" ").append(list.get(i).getLaundWorker_mn()).append(" ").append(list.get(i).getLaundWorker_ln()));
        handler.txtDate.setText(list.get(i).getBooking_date());
        handler.txtTime.setText(list.get(i).getBooking_time());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList").child(list.get(i).getLaundWorker_fbid()).child("weeklyBook");
        mDatabase.child(list.get(i).getBooking_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("booking_status").getValue(String.class);
                Toast.makeText(context, status+ " Not Tidert", Toast.LENGTH_LONG).show();

                switch(status) {
                    case "DONE":
                        handler.imgStatus.setImageResource(R.drawable.ic_status_done);
                        break;
                    case "PROGRESS":
                        handler.imgStatus.setImageResource(R.drawable.ic_status_progress);
                        break;
                    case "PENDING":
                        Toast.makeText(context, "PEND", Toast.LENGTH_LONG).show();
                        handler.imgStatus.setImageResource(R.drawable.ic_status_pending);
                        break;
                    case "CANCELED":
                        handler.imgStatus.setImageResource(R.drawable.ic_status_canceled);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    static class BookingHandler {
        ImageView imgPic, imgStatus;
        TextView txtName, txtDate, txtTime;
    }
}