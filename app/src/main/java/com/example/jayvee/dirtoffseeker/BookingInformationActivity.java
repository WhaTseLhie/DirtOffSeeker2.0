package com.example.jayvee.dirtoffseeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class BookingInformationActivity extends AppCompatActivity {

    TextView txtName, txtEmail, txtContact, txtAddress, txtBookingId, txtServices, txtDate, txtTime, txtFee;
    Button btnConfirm, btnReport;
    UserDatabase userDatabase;
    ArrayList<User> seekerList = new ArrayList<>();
    ArrayList<Booking> bookingList = new ArrayList<>();
    ArrayList<Worker> workerList = new ArrayList<>();
    String content;//, service;
    Toolbar toolbar;
    double totfee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_information);

        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userDatabase = new UserDatabase(this);
        seekerList = userDatabase.getAllUser();
        bookingList = userDatabase.getAllBooking();

        this.txtName = (TextView) this.findViewById(R.id.textView);
        this.txtEmail = (TextView) this.findViewById(R.id.textView1);
        this.txtContact = (TextView) this.findViewById(R.id.textView2);
        this.txtAddress = (TextView) this.findViewById(R.id.textView3);
        this.txtBookingId = (TextView) this.findViewById(R.id.textView4);
        this.txtServices = (TextView) this.findViewById(R.id.textView5);
        this.txtDate = (TextView) this.findViewById(R.id.textView6);
        this.txtTime = (TextView) this.findViewById(R.id.textView7);
        this.txtFee = (TextView) this.findViewById(R.id.textView8);
        this.btnConfirm = (Button) this.findViewById(R.id.button);
        this.btnReport = (Button) this.findViewById(R.id.button1);

        workerList = userDatabase.getAllWorker();
        DatabaseReference mDb = FirebaseDatabase.getInstance().getReference().child("laundryWorkers").child(workerList.get(0).getLaundWorker_fbid());
        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String name = dataSnapshot.child("laundWorker_fn").getValue(String.class) +" "+ dataSnapshot.child("laundWorker_mn").getValue(String.class) +" "+ dataSnapshot.child("laundWorker_ln").getValue(String.class);
                    txtName.setText("Name: " +name);
                    txtEmail.setText("Email: " +dataSnapshot.child("laundWorker_email").getValue(String.class));
                    txtContact.setText("Contact: " +dataSnapshot.child("laundWorker_cnum").getValue(String.class));
                    txtAddress.setText("Address: " +dataSnapshot.child("laundWorker_address").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserDatabase userDatabase = new UserDatabase(BookingInformationActivity.this);
                    seekerList = userDatabase.getAllUser();

                    try {
                        Booking booking = null;
                        bookingList.clear();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if(booking != null) {
                                if(seekerList.get(0).getLaundSeeker_fbid().equals(booking.getLaundSeeker_fbid())) {
                                    bookingList.add(booking);
                                }
                            }
                        }

                        if(booking != null) {
                            userDatabase.deleteAllBooking();
                            userDatabase.addBooking(booking.getBooking_date(), booking.getBooking_id(), booking.getBooking_status(), booking.getBooking_time(), booking.getLaundWorker_fn(), booking.getLaundWorker_fbid(), booking.getLaundWorker_ln(), booking.getLaundWorker_mn(), booking.getLaundWorker_pic(), booking.getLaundSeeker_fbid(), booking.getBooking_service(), booking.getBooking_fee());

                            txtBookingId.setText("Booking Id: " +booking.getBooking_id());
                            txtDate.setText("Book Date: " +booking.getBooking_date());
                            txtTime.setText("Time: " +booking.getBooking_time());
                            //service = booking.getBooking_service();
                            txtServices.setText("Service Offered: " +booking.getBooking_service());
                            /*int fee = 0;
                            StringTokenizer tokens = new StringTokenizer(booking.getBooking_service(), ",");
                            if(tokens.hasMoreTokens()) {
                                while(tokens.hasMoreTokens()) {
                                    String tokenStr = tokens.nextToken();
                                    if(tokenStr.equals()) {
                                        fee += Integer.parseInt(tokens.nextToken());
                                    }
                                }
                            }

                            if(fee != 0) {
                                fee += (fee*0.2);
                            }
                            txtFee.setText("Total Fees: " +fee);*/
                            if(booking.booking_status.equalsIgnoreCase("Done")) {
                                btnConfirm.setVisibility(View.GONE);
                            }
                        }
                    } catch(Exception e) {
                        Toast.makeText(BookingInformationActivity.this, "Error " +e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mDbase = FirebaseDatabase.getInstance().getReference().child("laundryServices").child(workerList.get(0).getLaundWorker_fbid());
        mDbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String totalfee = dataSnapshot.child("monthlyServicesFee").getValue(String.class);

                    int fee = 0;
                    StringTokenizer tokens = new StringTokenizer(totalfee, ",");
                    if(tokens.hasMoreTokens()) {
                        while(tokens.hasMoreTokens()) {
                            String tokenStr = tokens.nextToken();
                            if(tokenStr.equals(bookingList.get(0).getBooking_service())) {
                                fee = Integer.parseInt(tokens.nextToken());
                                break;
                            }
                        }
                    }

                    if(fee != 0) {
                        totfee = fee + (fee*0.2);
                    }
                    txtFee.setText("Total Fees: " +totfee);
                    bookingList.get(0).setBooking_fee(""+totfee);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDatabase = new UserDatabase(BookingInformationActivity.this);

                userDatabase.addBooking(bookingList.get(0).getBooking_date(), bookingList.get(0).getBooking_id(), bookingList.get(0).getBooking_status(), bookingList.get(0).getBooking_time(), bookingList.get(0).getLaundWorker_fn(), bookingList.get(0).getLaundWorker_fbid(), bookingList.get(0).getLaundWorker_ln(), bookingList.get(0).getLaundWorker_mn(), bookingList.get(0).getLaundWorker_pic(), bookingList.get(0).getLaundSeeker_fbid(), bookingList.get(0).getBooking_service(), bookingList.get(0).getBooking_fee());
                bookingList = userDatabase.getAllBooking();
                if(!bookingList.isEmpty()) {
                    if (bookingList.get(0).getBooking_status().equalsIgnoreCase("Progress")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookingInformationActivity.this);
                        builder.setTitle("Confirmation");
                        builder.setMessage(totfee + " will be deducted from your balance");
                        builder.setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if (!bookingList.isEmpty()) {
                                //if (bookingList.get(0).getBooking_status().equalsIgnoreCase("Progress")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList").child(bookingList.get(0).getBooking_id());
                                HashMap<String, String> hashMap = new HashMap<>();

                                hashMap.put("booking_date", bookingList.get(0).getBooking_date());
                                hashMap.put("booking_id", bookingList.get(0).getBooking_id());
                                hashMap.put("booking_status", "Done");
                                hashMap.put("booking_time", bookingList.get(0).getBooking_time());
                                hashMap.put("laundWorker_fn", bookingList.get(0).getLaundWorker_fn());
                                hashMap.put("laundWorker_fbid", bookingList.get(0).getLaundWorker_fbid());
                                hashMap.put("laundWorker_ln", bookingList.get(0).getLaundWorker_ln());
                                hashMap.put("laundWorker_mn", bookingList.get(0).getLaundWorker_mn());
                                hashMap.put("laundWorker_pic", bookingList.get(0).getLaundWorker_pic());
                                hashMap.put("laundSeeker_fbid", bookingList.get(0).getLaundSeeker_fbid());
                                hashMap.put("booking_service", bookingList.get(0).getBooking_service());
                                hashMap.put("booking_fee", bookingList.get(0).getBooking_fee());
                                mDatabase.setValue(hashMap);

                                bookingList = userDatabase.getAllBooking();
                                userDatabase.deleteAllBooking();
                                userDatabase.addBooking(bookingList.get(0).getBooking_date(), bookingList.get(0).getBooking_id(), "Done", bookingList.get(0).getBooking_time(), bookingList.get(0).getLaundWorker_fn(), bookingList.get(0).getLaundWorker_fbid(), bookingList.get(0).getLaundWorker_ln(), bookingList.get(0).getLaundWorker_mn(), bookingList.get(0).getLaundWorker_pic(), bookingList.get(0).getLaundSeeker_fbid(), bookingList.get(0).getBooking_service(), bookingList.get(0).getBooking_fee());

                                DatabaseReference mDb = FirebaseDatabase.getInstance().getReference().child("bookingListHistory").child(bookingList.get(0).getBooking_id());
                                HashMap<String, String> hash = new HashMap<>();

                                hash.put("booking_date", bookingList.get(0).getBooking_date());
                                hash.put("booking_id", bookingList.get(0).getBooking_id());
                                hash.put("booking_status", "Done");
                                hash.put("booking_time", bookingList.get(0).getBooking_time());
                                hash.put("laundWorker_fn", bookingList.get(0).getLaundWorker_fn());
                                hash.put("laundWorker_fbid", bookingList.get(0).getLaundWorker_fbid());
                                hash.put("laundWorker_ln", bookingList.get(0).getLaundWorker_ln());
                                hash.put("laundWorker_mn", bookingList.get(0).getLaundWorker_mn());
                                hash.put("laundWorker_pic", bookingList.get(0).getLaundWorker_pic());
                                hash.put("laundSeeker_fbid", bookingList.get(0).getLaundSeeker_fbid());
                                hash.put("booking_service", bookingList.get(0).getBooking_service());
                                hash.put("booking_fee", bookingList.get(0).getBooking_fee());
                                mDb.setValue(hash);

                                userDatabase.addHistoryBooking(bookingList.get(0).getBooking_date(), bookingList.get(0).getBooking_id(), "Done", bookingList.get(0).getBooking_time(), bookingList.get(0).getLaundWorker_fn(), bookingList.get(0).getLaundWorker_fbid(), bookingList.get(0).getLaundWorker_ln(), bookingList.get(0).getLaundWorker_mn(), bookingList.get(0).getLaundWorker_pic(), bookingList.get(0).getLaundSeeker_fbid(), bookingList.get(0).getBooking_service(), bookingList.get(0).getBooking_fee());
                                Toast.makeText(BookingInformationActivity.this, "Confirm Success", Toast.LENGTH_LONG).show();

                                //booking_id, payout_date, payout_fee, payout_id;
                                DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("laundrySeeker").child(seekerList.get(0).getLaundSeeker_fbid());
                                seekerList = userDatabase.getAllUser();

                                double tfee = 120.0;

                                try {
                                    tfee = Integer.parseInt(seekerList.get(0).getLaundSeeker_totalbal()) - Integer.parseInt(bookingList.get(0).getBooking_fee());
                                } catch(Exception e) {
                                    Log.d("NumberFormatException", e.getMessage());
                                }
                                String str1 = "" + tfee;
                                HashMap<String, String> hashMap2 = new HashMap<>();
                                hashMap2.put("laundSeeker_cnum", seekerList.get(0).getLaundSeeker_cnum());
                                hashMap2.put("laundSeeker_email", seekerList.get(0).getLaundSeeker_email());
                                hashMap2.put("laundSeeker_fbid", seekerList.get(0).getLaundSeeker_fbid());
                                hashMap2.put("laundSeeker_fn", seekerList.get(0).getLaundSeeker_fn());
                                hashMap2.put("laundSeeker_gender", seekerList.get(0).getLaundSeeker_gender());
                                hashMap2.put("laundSeeker_ln", seekerList.get(0).getLaundSeeker_ln());
                                hashMap2.put("laundSeeker_link", seekerList.get(0).getLaundSeeker_link());
                                hashMap2.put("laundSeeker_pic", seekerList.get(0).getLaundSeeker_pic());
                                hashMap2.put("laundSeeker_status", seekerList.get(0).getLaundSeeker_status());
                                hashMap2.put("laundSeeker_totalbal", str1);
                                mydb.setValue(hashMap2);

                                DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("walletPayout").child(seekerList.get(0).getLaundSeeker_fbid());
                                HashMap<String, String> hashmap = new HashMap<>();
                                String key = mdb.push().getKey();
                                hashmap.put("booking_id", bookingList.get(0).getBooking_id());
                                hashmap.put("payout_date", new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date()));
                                hashmap.put("payout_fee", "" + totfee);
                                hashmap.put("payout_id", key);
                                mdb.child(key).setValue(hashmap);
                                //}
                                //} else {
                                //Toast.makeText(BookingInformationActivity.this, "Empty", Toast.LENGTH_LONG).show();
                                //}
                                rateWorker();

                                btnConfirm.setVisibility(View.GONE);
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Toast.makeText(BookingInformationActivity.this, "Please wait for the workers confirmation", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(BookingInformationActivity.this, "Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        this.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(BookingInformationActivity.this);
                dialog.setContentView(R.layout.dialog_report_content);

                final EditText txtContent = dialog.findViewById(R.id.editText);
                Button btnSubmit = dialog.findViewById(R.id.button);
                Button btnCancel = dialog.findViewById(R.id.button1);
                //content = txtContent.getText().toString();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("abusive_message");
                        HashMap<String, String> hashMap = new HashMap<>();

                        String key = mDatabase.push().getKey();
                        hashMap.put("booking_id", bookingList.get(0).getBooking_id());
                        //hashMap.put("content", content);
                        hashMap.put("content", txtContent.getText().toString());
                        hashMap.put("status", "Unread");
                        hashMap.put("dateSent", new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date()));
                        hashMap.put("laundryWorker_id", bookingList.get(0).getLaundWorker_fbid());
                        hashMap.put("report_id", key);
                        hashMap.put("seeker_id", bookingList.get(0).getLaundSeeker_fbid());
                        mDatabase.child(key).setValue(hashMap);

                        Toast.makeText(BookingInformationActivity.this, "Worker Reported", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
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
    }

    private void rateWorker() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rate_worker);

        final EditText txtComment = dialog.findViewById(R.id.editText);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button btnSub = dialog.findViewById(R.id.button);

        final String com = txtComment.getText().toString();
        final String rate = ""+ratingBar.getRating();

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference().child("workerRates");
                String k = dbreference.push().getKey();
                HashMap<String, String> hm = new HashMap<>();
                hm.put("rate_id", k);
                hm.put("rate_comment", com);
                hm.put("rate_bar", rate);
                dbreference.child(k).setValue(hm);

                Toast.makeText(BookingInformationActivity.this, "Rate Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}