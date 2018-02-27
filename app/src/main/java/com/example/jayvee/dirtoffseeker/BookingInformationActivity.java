package com.example.jayvee.dirtoffseeker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    double totfee = 0;
    EditText txtContent;
    RatingBar rbReview;
    String com = "", rate = "";
    RatingBar ratingBar;
    TextView txtComment;
    ImageView iv;
    String seekerFee;
    String workerBal = "";
    int index = 0;
    Booking book;

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
        workerList = userDatabase.getAllWorker();

        this.iv = (ImageView) this.findViewById(R.id.imageView);
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
        this.rbReview = (RatingBar) this.findViewById(R.id.ratingBar);

        index = getIntent().getIntExtra("index", 0);
        book = (Booking) getIntent().getSerializableExtra("booking");

        workerList = userDatabase.getAllWorker();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers").child(workerList.get(0).getLaundWorker_fbid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child("laundWorker_pic").getValue(String.class)).transform(new CircleTransform()).into(iv);
                    String name = dataSnapshot.child("laundWorker_fn").getValue(String.class) +" "+ dataSnapshot.child("laundWorker_mn").getValue(String.class) +" "+ dataSnapshot.child("laundWorker_ln").getValue(String.class);
                    txtName.setText(new StringBuilder().append("Name: ").append(name));
                    txtEmail.setText(new StringBuilder().append("Email: ").append(dataSnapshot.child("laundWorker_email").getValue(String.class)));
                    txtContact.setText(new StringBuilder().append("Contact: ").append(dataSnapshot.child("laundWorker_cnum").getValue(String.class)));
                    txtAddress.setText(new StringBuilder().append("Address: ").append(dataSnapshot.child("laundWorker_address").getValue(String.class)));
                    workerBal = dataSnapshot.child("laundWorker_bal").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*DatabaseReference mDatabaseR = FirebaseDatabase.getInstance().getReference().child("bookingList").child(workerList.get(0).getLaundWorker_fbid()).child("weeklyBook");
        mDatabaseR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserDatabase userDatabase = new UserDatabase(BookingInformationActivity.this);

                    try {
                        Booking booking = null;
                        userDatabase.deleteAllBooking();
                        bookingList.clear();

                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            booking = child.getValue(Booking.class);

                            if(booking != null) {
                                bookingList.add(booking);
                                userDatabase.addBooking(booking.getBooking_date(), booking.getBooking_id(), booking.getBooking_status(), booking.getBooking_time(), booking.getLaundWorker_fn(), booking.getLaundWorker_fbid(), booking.getLaundWorker_ln(), booking.getLaundWorker_mn(), booking.getLaundWorker_pic(), booking.getLaundSeeker_fbid(), booking.getBooking_service(), booking.getBooking_fee());
                            }
                        }
                        txtBookingId.setText("Booking Id: " +booking.getBooking_id());
                        txtDate.setText("Book Date: " +booking.getBooking_date());
                        txtTime.setText("Time: " +booking.getBooking_time());
                        txtServices.setText("Service Offered: " +booking.getBooking_service());

                        if(booking.booking_status.equalsIgnoreCase("Done")) {
                            btnConfirm.setVisibility(View.GONE);
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
        });*/

        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("laundryServices").child(workerList.get(0).getLaundWorker_fbid());
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String totalfee = dataSnapshot.child("weeklyServicesFee").getValue(String.class);

                    int fee = 0;
                    StringTokenizer tokens = new StringTokenizer(totalfee, ",");
                    if(tokens.hasMoreTokens()) {
                        while(tokens.hasMoreTokens()) {
                            String tokenStr = tokens.nextToken();

                            if(bookingList.get(index).getBooking_service().toLowerCase().contains(tokenStr.toLowerCase())) {
                                fee += Integer.parseInt(tokens.nextToken());
                            }
                        }
                    }

                    txtBookingId.setText("Booking Id: " +book.getBooking_id());
                    txtDate.setText("Book Date: " +book.getBooking_date());
                    txtTime.setText("Time: " +book.getBooking_time());
                    txtServices.setText("Service Offered: " +book.getBooking_service());

                    if(book.getBooking_status().equalsIgnoreCase("Done")) {
                        btnConfirm.setVisibility(View.GONE);
                    }
                    totfee = fee + (fee * 0.2);
                    bookingList.get(index).setBooking_fee(""+totfee);
                    txtFee.setText("Total Fees: " +totfee);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //userDatabase = new UserDatabase(BookingInformationActivity.this);

                //userDatabase.addBooking(bookingList.get(index).getBooking_date(), bookingList.get(0).getBooking_id(), bookingList.get(0).getBooking_status(), bookingList.get(0).getBooking_time(), bookingList.get(0).getLaundWorker_fn(), bookingList.get(0).getLaundWorker_fbid(), bookingList.get(0).getLaundWorker_ln(), bookingList.get(0).getLaundWorker_mn(), bookingList.get(0).getLaundWorker_pic(), bookingList.get(0).getLaundSeeker_fbid(), bookingList.get(0).getBooking_service(), bookingList.get(0).getBooking_fee());
                bookingList = userDatabase.getAllBooking();
                if(!bookingList.isEmpty()) {
                    if (bookingList.get(index).getBooking_status().equalsIgnoreCase("Progress")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookingInformationActivity.this);
                        builder.setTitle("Confirmation");
                        builder.setMessage(totfee + " will be deducted from your balance");
                        builder.setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference mDatabase14 = FirebaseDatabase.getInstance().getReference().child("bookingList").child(bookingList.get(index).getLaundWorker_fbid()).child("weeklyBook");
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("booking_date", bookingList.get(index).getBooking_date());
                                hashMap1.put("booking_fee", bookingList.get(index).getBooking_fee());
                                hashMap1.put("booking_id", bookingList.get(index).getBooking_id());
                                hashMap1.put("booking_service", bookingList.get(index).getBooking_service());
                                hashMap1.put("booking_status", "Done");
                                hashMap1.put("booking_time", bookingList.get(index).getBooking_time());
                                hashMap1.put("laundSeeker_fbid", bookingList.get(index).getLaundSeeker_fbid());
                                hashMap1.put("laundWorker_fbid", bookingList.get(index).getLaundWorker_fbid());
                                hashMap1.put("laundWorker_fn", bookingList.get(index).getLaundWorker_fn());
                                hashMap1.put("laundWorker_ln", bookingList.get(index).getLaundWorker_ln());
                                hashMap1.put("laundWorker_mn", bookingList.get(index).getLaundWorker_mn());
                                hashMap1.put("laundWorker_pic", bookingList.get(index).getLaundWorker_pic());
                                mDatabase14.child(book.getBooking_id()).setValue(hashMap1);

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(BookingInformationActivity.this);
                                builder1.setTitle("Rebook");
                                builder1.setMessage("Do you still want to continue?");
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference().child("bookingList").child(workerList.get(0).getLaundWorker_fbid()).child("weeklyBook");
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("booking_date", bookingList.get(index).getBooking_date());
                                        hashMap.put("booking_id", bookingList.get(index).getBooking_id());
                                        hashMap.put("booking_status", "PENDING");
                                        hashMap.put("booking_time", bookingList.get(index).getBooking_time());
                                        hashMap.put("laundWorker_fn", bookingList.get(index).getLaundWorker_fn());
                                        hashMap.put("laundWorker_fbid", bookingList.get(index).getLaundWorker_fbid());
                                        hashMap.put("laundWorker_ln", bookingList.get(index).getLaundWorker_ln());
                                        hashMap.put("laundWorker_mn", bookingList.get(index).getLaundWorker_mn());
                                        hashMap.put("laundWorker_pic", bookingList.get(index).getLaundWorker_pic());
                                        hashMap.put("laundSeeker_fbid", bookingList.get(index).getLaundSeeker_fbid());
                                        hashMap.put("booking_service", bookingList.get(index).getBooking_service());
                                        hashMap.put("booking_fee", bookingList.get(index).getBooking_fee());

                                        DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference().child("bookingListHistory").child(bookingList.get(index).getLaundWorker_fbid());
                                        HashMap<String, String> hashMap11 = new HashMap<>();
                                        String ki = historyReference.push().getKey();
                                        hashMap11.put("booking_date", bookingList.get(index).getBooking_date());
                                        hashMap11.put("booking_id", ki);
                                        hashMap11.put("booking_status", "DONE");
                                        hashMap11.put("booking_time", bookingList.get(index).getBooking_time());
                                        hashMap11.put("laundWorker_fn", bookingList.get(index).getLaundWorker_fn());
                                        hashMap11.put("laundWorker_fbid", bookingList.get(index).getLaundWorker_fbid());
                                        hashMap11.put("laundWorker_ln", bookingList.get(index).getLaundWorker_ln());
                                        hashMap11.put("laundWorker_mn", bookingList.get(index).getLaundWorker_mn());
                                        hashMap11.put("laundWorker_pic", bookingList.get(index).getLaundWorker_pic());
                                        hashMap11.put("laundSeeker_fbid", bookingList.get(index).getLaundSeeker_fbid());
                                        hashMap11.put("booking_service", bookingList.get(index).getBooking_service());
                                        hashMap11.put("booking_fee", bookingList.get(index).getBooking_fee());
                                        historyReference.child(ki).setValue(hashMap11);

                                        mDatabase3.child(book.getBooking_id()).setValue(hashMap);
                                        userDatabase.deleteBooking(bookingList.get(index).getBooking_id());
                                        userDatabase.addBooking(
                                                bookingList.get(index).getBooking_date(),
                                                bookingList.get(index).getBooking_id(),
                                                "PENDING",
                                                bookingList.get(index).getBooking_time(),
                                                bookingList.get(index).getLaundWorker_fn(),
                                                bookingList.get(index).getLaundWorker_fbid(),
                                                bookingList.get(index).getLaundWorker_ln(),
                                                bookingList.get(index).getLaundWorker_mn(),
                                                bookingList.get(index).getLaundWorker_pic(),
                                                bookingList.get(index).getLaundSeeker_fbid(),
                                                bookingList.get(index).getBooking_service(),
                                                bookingList.get(index).getBooking_fee()
                                        );

                                        Toast.makeText(BookingInformationActivity.this, "Rebook Success", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("laundrySchedule").child(bookingList.get(index).getLaundWorker_fbid()).child("modeWeekly");
                                        mDatabaseRef.child(bookingList.get(index).getBooking_id()).removeValue();

                                        Toast.makeText(BookingInformationActivity.this, "Booking Canceled", Toast.LENGTH_LONG).show();
                                    }
                                });

                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();

                                DatabaseReference mDatabase4 = FirebaseDatabase.getInstance().getReference().child("laundrySeekers").child(seekerList.get(0).getLaundSeeker_fbid());
                                mDatabase4.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        seekerFee = dataSnapshot.child("laundSeeker_fee").getValue(String.class);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                double tfee = 0;
                                seekerList = userDatabase.getAllUser();

                                try {
                                    //tfee = Integer.parseInt(seekerList.get(0).getLaundSeeker_totalbal()) - Integer.parseInt(bookingList.get(0).getBooking_fee());
                                    tfee = Double.parseDouble(seekerFee) - totfee;
                                } catch(Exception e) {
                                    Log.d("NumberFormatException", e.getMessage());
                                }
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
                                hashMap2.put("laundSeeker_totalbal", ""+tfee);
                                mDatabase4.setValue(hashMap2);

                                DatabaseReference mDatabase5 = FirebaseDatabase.getInstance().getReference().child("walletPayout").child(seekerList.get(0).getLaundSeeker_fbid());
                                HashMap<String, String> hashmap = new HashMap<>();
                                String key = mDatabase5.push().getKey();
                                hashmap.put("booking_id", bookingList.get(index).getBooking_id());
                                hashmap.put("payout_date", new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date()));
                                hashmap.put("payout_fee", "" +totfee);
                                hashmap.put("payout_id", key);
                                mDatabase5.child(key).setValue(hashmap);

                                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("walletCashInWorker").child(bookingList.get(index).getLaundWorker_fbid());
                                HashMap<String, String> hm2 = new HashMap<>();
                                String keyId = dbRef.push().getKey();
                                hm2.put("cashin_id", keyId);
                                hm2.put("cashin_date", new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date()));
                                hm2.put("cashin_fee", ""+totfee);
                                dbRef.child(keyId).setValue(hm2);

                                DatabaseReference mDatabaseW = FirebaseDatabase.getInstance().getReference().child("laundryWorkers").child(workerList.get(0).getLaundWorker_fbid());
                                HashMap<String, String> hm3 = new HashMap<>();
                                hm3.put("laundWorker_address", workerList.get(0).getLaundWorker_address());
                                hm3.put("laundWorker_bdate", workerList.get(0).getLaundWorker_bdate());
                                hm3.put("laundWorker_cnum", workerList.get(0).getLaundWorker_cnum());
                                hm3.put("laundWorker_dateApplied", workerList.get(0).getLaundWorker_dateApplied());
                                hm3.put("laundWorker_email", workerList.get(0).getLaundWorker_email());
                                hm3.put("laundWorker_fbid", workerList.get(0).getLaundWorker_fbid());
                                hm3.put("laundWorker_fn", workerList.get(0).getLaundWorker_fn());
                                hm3.put("laundWorker_ln", workerList.get(0).getLaundWorker_ln());
                                hm3.put("laundWorker_mn", workerList.get(0).getLaundWorker_mn());
                                hm3.put("laundWorker_pic", workerList.get(0).getLaundWorker_pic());
                                hm3.put("laundWorker_status", workerList.get(0).getLaundWorker_status());
                                double d = Double.parseDouble(workerBal) + totfee;
                                hm3.put("laundWorker_bal", "" + d);
                                mDatabaseW.setValue(hm3);

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

                txtContent = dialog.findViewById(R.id.editText);
                Button btnSubmit = dialog.findViewById(R.id.button);
                Button btnCancel = dialog.findViewById(R.id.button1);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("abusive_message");
                        HashMap<String, String> hashMap = new HashMap<>();

                        String key = mDatabase.push().getKey();
                        hashMap.put("booking_id", bookingList.get(index).getBooking_id());
                        hashMap.put("content", txtContent.getText().toString());
                        hashMap.put("status", "Unread");
                        hashMap.put("dateSent", new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date()));
                        hashMap.put("laundryWorker_id", bookingList.get(index).getLaundWorker_fbid());
                        hashMap.put("report_id", key);
                        hashMap.put("seeker_id", bookingList.get(index).getLaundSeeker_fbid());
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

        DatabaseReference mDatabase7 = FirebaseDatabase.getInstance().getReference().child("workerRates").child(bookingList.get(index).getLaundWorker_fbid());
        mDatabase7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Rating rating;
                    Float starf = 0.0f;

                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        rating = child.getValue(Rating.class);
                        if(rating != null) {
                            starf += Float.parseFloat(rating.getRate_bar());
                        }
                    }

                    rbReview.setRating(starf/dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void rateWorker() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rate_worker);

        txtComment = dialog.findViewById(R.id.editText);
        ratingBar = dialog.findViewById(R.id.ratingBar);
        Button btnSub = dialog.findViewById(R.id.button);

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com = txtComment.getText().toString();
                rate = ""+ratingBar.getRating();

                if(!rate.equals("") && !com.trim().equals("")) {
                    DatabaseReference mDatabase6 = FirebaseDatabase.getInstance().getReference().child("workerRates").child(bookingList.get(index).getLaundWorker_fbid());
                    String sName = seekerList.get(0).getLaundSeeker_fn() +" "+ seekerList.get(0).getLaundSeeker_ln();
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("rate_bar", rate);
                    hm.put("rate_comment", com);
                    hm.put("rate_name", sName);
                    mDatabase6.child(seekerList.get(0).getLaundSeeker_fbid()).setValue(hm);

                    Toast.makeText(BookingInformationActivity.this, "Rate Success", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(BookingInformationActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    //service = booking.getBooking_service();
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

    //if (!bookingList.isEmpty()) {
    //if (bookingList.get(0).getBooking_status().equalsIgnoreCase("Progress")) {
                                /*DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookingList").child(bookingList.get(0).getLaundSeeker_fbid()).child("weeklyBook");
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
                                userDatabase.addBooking(bookingList.get(0).getBooking_date(), bookingList.get(0).getBooking_id(), "Done", bookingList.get(0).getBooking_time(), bookingList.get(0).getLaundWorker_fn(), bookingList.get(0).getLaundWorker_fbid(), bookingList.get(0).getLaundWorker_ln(), bookingList.get(0).getLaundWorker_mn(), bookingList.get(0).getLaundWorker_pic(), bookingList.get(0).getLaundSeeker_fbid(), bookingList.get(0).getBooking_service(), bookingList.get(0).getBooking_fee());*/

                /*Float stars = 0.0f;
                String starf = "";

                if(dataSnapshot12.exists()) {
                    starf = dataSnapshot12.child("rate_bar").getValue(String.class);
                    Toast.makeText(BookingInformationActivity.this, starf+ "starf", Toast.LENGTH_LONG).show();

                    try {
                        stars = Float.parseFloat(starf);
                    } catch(Exception e) {
                        Toast.makeText(BookingInformationActivity.this, stars+ "stars", Toast.LENGTH_LONG).show();
                    }
                    rbReview.setRating(stars);
                }*/
    //Toast.makeText(BookingInformationActivity.this, rating.getRate_bar()+ " rate bar", Toast.LENGTH_LONG).show();
    //Toast.makeText(BookingInformationActivity.this, dataSnapshot.getChildrenCount()+ " children count", Toast.LENGTH_LONG).show();
}