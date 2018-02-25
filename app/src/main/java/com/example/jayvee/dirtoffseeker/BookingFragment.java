package com.example.jayvee.dirtoffseeker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingFragment extends Fragment {

    ListView lv;
    //BookingListHistoryAdapter adapter;
    BookingListAdapter adapter;
    ArrayList<Booking> bookingList = new ArrayList<>();
    //ArrayList<HistoryBooking> historyList = new ArrayList<>();
    ArrayList<User> seekerList = new ArrayList<>();
    UserDatabase userDatabase;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        getActivity().setTitle("Booking List");
        userDatabase = new UserDatabase(getActivity());
        bookingList = userDatabase.getAllBooking();
        seekerList = userDatabase.getAllUser();
        //historyList = userDatabase.getAllHistoryBooking();

        lv = view.findViewById(R.id.listView);
        //adapter = new BookingListHistoryAdapter(getContext(), historyList);
        adapter = new BookingListAdapter(getContext(), bookingList);
        lv.setAdapter(adapter);

        onStart();
        //adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent bookingIntent = new Intent(getActivity(), BookingInformationActivity.class);
                startActivity(bookingIntent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList").child(seekerList.get(0).getLaundSeeker_fbid()).child("weeklyBook");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    seekerList = userDatabase.getAllUser();
                    userDatabase.deleteAllBooking();

                    try {
                        Booking booking = null;
                        bookingList.clear();
                        //HistoryBooking historyBooking;
                        //historyList.clear();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);
                            //historyBooking = child.getValue(HistoryBooking.class);

                            if(booking != null) {
                            //if(historyBooking != null) {
                                //if(seekerList.get(0).getLaundSeeker_fbid().equals(booking.getLaundSeeker_fbid())) {
                                //if(seekerList.get(0).getLaundSeeker_fbid().equals(historyBooking.getLaundSeeker_fbid())) {
                                if(seekerList.get(0).getLaundSeeker_fbid().equals(booking.getLaundSeeker_fbid())) {
                                    bookingList.add(booking);
                                    //historyList.add(historyBooking);
                                }
                            }
                        }

                        if(booking != null) {
                            if(seekerList.get(0).getLaundSeeker_fbid().equals(booking.getLaundSeeker_fbid())) {
                                userDatabase.deleteAllBooking();
                                userDatabase.addBooking(booking.getBooking_date(), booking.getBooking_id(), booking.getBooking_status(), booking.getBooking_time(), booking.getLaundWorker_fn(), booking.getLaundWorker_fbid(), booking.getLaundWorker_ln(), booking.getLaundWorker_mn(), booking.getLaundWorker_pic(), booking.getLaundSeeker_fbid(), booking.getBooking_service(), booking.getBooking_fee());
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        //Toast.makeText(getActivity(), "Error " +e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Error ", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}