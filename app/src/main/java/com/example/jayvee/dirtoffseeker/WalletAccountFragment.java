package com.example.jayvee.dirtoffseeker;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.LastOwnerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WalletAccountFragment extends Fragment {

    TextView txtBal;
    Button btnTopup;
    UserDatabase userDatabase;
    ArrayList<User> userList;
    TextView txtAmount;
    DatabaseReference mDatabase;

    public WalletAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_account, container, false);

        userDatabase = new UserDatabase(getActivity());
        userList = userDatabase.getAllUser();
        btnTopup = view.findViewById(R.id.button);
        txtBal = view.findViewById(R.id.textView);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySeekers").child(this.userList.get(0).getLaundSeeker_fbid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String tb = dataSnapshot.child("laundSeeker_totalbal").getValue(String.class);
                    if(tb != null && !tb.equals("")) {
                        txtBal.setText(tb);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "WalletAccountFragment Error! Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        });

        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_top_up);

                txtAmount = dialog.findViewById(R.id.editText);
                txtAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                Button btnSubmit = dialog.findViewById(R.id.button);
                Button btnCancel = dialog.findViewById(R.id.button1);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!txtAmount.getText().toString().trim().equals("")) {
                            int totalBal = Integer.parseInt(txtAmount.getText().toString());

                            if (!txtBal.getText().toString().equals("0.0")) {
                                try {
                                    totalBal += (Integer.parseInt(txtBal.getText().toString()));
                                } catch (NumberFormatException e) {
                                    Toast.makeText(getActivity(), "Message: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                            // push total balance data to firebase
                            DatabaseReference mDb = FirebaseDatabase.getInstance().getReference().child("laundrySeekers").child(userList.get(0).getLaundSeeker_fbid());
                            HashMap<String, String> hm = new HashMap<>();

                            hm.put("laundSeeker_cnum", userList.get(0).getLaundSeeker_cnum());
                            hm.put("laundSeeker_email", userList.get(0).getLaundSeeker_email());
                            hm.put("laundSeeker_fbid", userList.get(0).getLaundSeeker_fbid());
                            hm.put("laundSeeker_fn", userList.get(0).getLaundSeeker_fn());
                            hm.put("laundSeeker_gender", userList.get(0).getLaundSeeker_gender());
                            hm.put("laundSeeker_ln", userList.get(0).getLaundSeeker_ln());
                            hm.put("laundSeeker_link", userList.get(0).getLaundSeeker_link());
                            hm.put("laundSeeker_pic", userList.get(0).getLaundSeeker_pic());
                            hm.put("laundSeeker_status", userList.get(0).getLaundSeeker_status());
                            hm.put("laundSeeker_totalbal", Integer.toString(totalBal));
                            mDb.setValue(hm);

                            // push cash in data to firebase
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("walletCashIn").child(userList.get(0).getLaundSeeker_fbid());
                            HashMap<String, String> hashMap = new HashMap<>();

                            String cashin_date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date());
                            String cashin_time = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date());

                            String key = mDatabase.push().getKey();
                            hashMap.put("cashin_balance", txtAmount.getText().toString());
                            hashMap.put("cashin_date", cashin_date);
                            hashMap.put("cashin_id", key);
                            hashMap.put("cashin_time", cashin_time);
                            mDatabase.child(key).setValue(hashMap);

                            userDatabase.deleteAllUser();
                            userDatabase.addUser(userList.get(0).getLaundSeeker_cnum(), userList.get(0).getLaundSeeker_email(), userList.get(0).getLaundSeeker_fbid(), userList.get(0).getLaundSeeker_fn(), userList.get(0).getLaundSeeker_gender(), userList.get(0).getLaundSeeker_ln(), userList.get(0).getLaundSeeker_link(), userList.get(0).getLaundSeeker_pic(), userList.get(0).getLaundSeeker_status(), Integer.toString(totalBal));
                            userDatabase.addWalletCashIn(txtAmount.getText().toString(), cashin_date, key, cashin_time);
                            Toast.makeText(getActivity(), "Top-up Successful", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Please put an amount", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }
}