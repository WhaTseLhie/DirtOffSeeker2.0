package com.example.jayvee.dirtoffseeker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class WalletCashInFragment extends Fragment {

    ListView lv;
    WalletCashInAdapter adapter;
    ArrayList<WalletCashIn> walletList = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    UserDatabase userDatabase;
    DatabaseReference mDatabase;

    public WalletCashInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_cash_in, container, false);

        userDatabase = new UserDatabase(getContext());
        userList = userDatabase.getAllUser();

        lv = view.findViewById(R.id.listView);
        adapter = new WalletCashInAdapter(getActivity(), walletList);
        lv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("walletCashIn").child(userList.get(0).getLaundSeeker_fbid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    walletList.clear();

                    try {
                        WalletCashIn cashIn;

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            cashIn = child.getValue(WalletCashIn.class);
                            walletList.add(cashIn);
                        }

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "WalletCashInFragment Error! Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }
}