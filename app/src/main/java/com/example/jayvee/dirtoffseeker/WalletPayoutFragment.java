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

public class WalletPayoutFragment extends Fragment {

    ListView lv;
    WalletPayoutAdapter adapter;
    ArrayList<WalletPayout> payoutList = new ArrayList<>();
    ArrayList<User> seekerList = new ArrayList<>();
    UserDatabase userDatabase;
    DatabaseReference mDatabase;

    public WalletPayoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_payout, container, false);

        userDatabase = new UserDatabase(getContext());
        payoutList = userDatabase.getAllWalletPayout();
        seekerList = userDatabase.getAllUser();

        lv = view.findViewById(R.id.listView);
        adapter = new WalletPayoutAdapter(getContext(), payoutList);
        lv.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("walletPayout").child(seekerList.get(0).getLaundSeeker_fbid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    try {
                        WalletPayout payout;
                        payoutList.clear();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            payout = child.getValue(WalletPayout.class);

                            if(payout != null) {
                                payoutList.add(payout);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        Toast.makeText(getActivity(), "Error " +e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}