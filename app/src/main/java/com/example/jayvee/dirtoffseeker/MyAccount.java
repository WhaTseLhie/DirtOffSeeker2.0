package com.example.jayvee.dirtoffseeker;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAccount extends AppCompatActivity {

    Toolbar toolbar;
    ImageView iv;
    EditText txtFullName, txtGender, txtEmail, txtNum, txtLink, txtStatus;
    UserDatabase userdb;
    ArrayList<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userdb = new UserDatabase(this);
        list = userdb.getAllUser();

        iv = (ImageView) this.findViewById(R.id.imageView);
        txtFullName = (EditText) this.findViewById(R.id.editText1);
        txtEmail = (EditText) this.findViewById(R.id.editText2);
        txtGender = (EditText) this.findViewById(R.id.editText3);
        txtNum = (EditText) this.findViewById(R.id.editText4);
        txtLink = (EditText) this.findViewById(R.id.editText5);
        txtStatus = (EditText) this.findViewById(R.id.editText6);

        if(!list.isEmpty()) {
            //iv.setImageURI(Uri.parse(list.get(0).getLaundSeeker_pic()));
            Picasso.with(this).load(list.get(0).getLaundSeeker_pic()).transform(new CircleTransform()).into(iv);
            txtFullName.setText(new StringBuilder().append(list.get(0).getLaundSeeker_fn()).append(" ").append(list.get(0).getLaundSeeker_ln()));
            txtEmail.setText(list.get(0).getLaundSeeker_email());
            txtGender.setText(list.get(0).getLaundSeeker_gender());
            txtNum.setText(list.get(0).getLaundSeeker_cnum());
            txtLink.setText(list.get(0).getLaundSeeker_link());
            txtStatus.setText(list.get(0).getLaundSeeker_status());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.myaccount_edit_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit:
                if(item.getTitle().equals("Edit")) {
                    item.setTitle("Save");
                    txtGender.setClickable(true);
                    txtGender.setFocusable(true);
                    txtGender.setFocusableInTouchMode(true);
                    txtGender.setBackgroundColor(Color.WHITE);
                    txtGender.setTextColor(Color.BLACK);
                    ///
                    txtEmail.setClickable(true);
                    txtEmail.setFocusable(true);
                    txtEmail.setFocusableInTouchMode(true);
                    txtEmail.setBackgroundColor(Color.WHITE);
                    txtEmail.setTextColor(Color.BLACK);
                    ///
                    txtNum.setClickable(true);
                    txtNum.setFocusable(true);
                    txtNum.setFocusableInTouchMode(true);
                    txtNum.requestFocus();
                    txtNum.setBackgroundColor(Color.WHITE);
                    txtNum.setTextColor(Color.BLACK);
                    ///
                    txtLink.setClickable(true);
                    txtLink.setFocusable(true);
                    txtLink.setFocusableInTouchMode(true);
                    txtLink.setBackgroundColor(Color.WHITE);
                    txtLink.setTextColor(Color.BLACK);
                } else {
                    if(!txtEmail.getText().toString().trim().equals("") && (!txtEmail.getText().toString().trim().equals("") && (txtGender.getText().toString().trim().equalsIgnoreCase("male") || txtGender.getText().toString().trim().equalsIgnoreCase("female"))) && !txtNum.getText().toString().trim().equals("") && !txtLink.getText().toString().trim().equals("")) {
                        item.setTitle("Edit");
                        ///
                        txtFullName.setClickable(false);
                        txtFullName.setFocusable(false);
                        txtFullName.setFocusableInTouchMode(false);
                        txtFullName.setBackgroundColor(Color.BLACK);
                        txtFullName.setTextColor(Color.WHITE);
                        ///
                        txtGender.setClickable(false);
                        txtGender.setFocusable(false);
                        txtGender.setFocusableInTouchMode(false);
                        txtGender.setBackgroundColor(Color.BLACK);
                        txtGender.setTextColor(Color.WHITE);
                        ///
                        txtEmail.setClickable(false);
                        txtEmail.setFocusable(false);
                        txtEmail.setFocusableInTouchMode(false);
                        txtEmail.setBackgroundColor(Color.BLACK);
                        txtEmail.setTextColor(Color.WHITE);
                        ///
                        txtNum.setClickable(false);
                        txtNum.setFocusable(false);
                        txtNum.setFocusableInTouchMode(false);
                        txtNum.setBackgroundColor(Color.BLACK);
                        txtNum.setTextColor(Color.WHITE);
                        ///
                        txtLink.setClickable(false);
                        txtLink.setFocusable(false);
                        txtLink.setFocusableInTouchMode(false);
                        txtLink.setBackgroundColor(Color.BLACK);
                        txtLink.setTextColor(Color.WHITE);

                        list.get(0).setLaundSeeker_email(txtEmail.getText().toString());
                        list.get(0).setLaundSeeker_gender(txtGender.getText().toString());
                        list.get(0).setLaundSeeker_cnum(txtNum.getText().toString());
                        list.get(0).setLaundSeeker_link(txtLink.getText().toString());

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySeekers").child(list.get(0).getLaundSeeker_fbid());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("laundSeeker_cnum", list.get(0).getLaundSeeker_cnum());
                        hashMap.put("laundSeeker_email", list.get(0).getLaundSeeker_email());
                        hashMap.put("laundSeeker_fbid", list.get(0).getLaundSeeker_fbid());
                        hashMap.put("laundSeeker_fn", list.get(0).getLaundSeeker_fn());
                        hashMap.put("laundSeeker_gender", list.get(0).getLaundSeeker_gender());
                        hashMap.put("laundSeeker_ln", list.get(0).getLaundSeeker_ln());
                        hashMap.put("laundSeeker_link", list.get(0).getLaundSeeker_link());
                        hashMap.put("laundSeeker_pic", list.get(0).getLaundSeeker_pic());
                        hashMap.put("laundSeeker_status", list.get(0).getLaundSeeker_status());
                        hashMap.put("laundSeeker_totalbal", list.get(0).getLaundSeeker_totalbal());
                        mDatabase.setValue(hashMap);

                        userdb.deleteAllUser();
                        userdb.addUser(list.get(0).getLaundSeeker_cnum(), list.get(0).getLaundSeeker_email(), list.get(0).getLaundSeeker_fbid(), list.get(0).getLaundSeeker_fn(), list.get(0).getLaundSeeker_gender(), list.get(0).getLaundSeeker_ln(), list.get(0).getLaundSeeker_link(), list.get(0).getLaundSeeker_pic(), list.get(0).getLaundSeeker_status(), list.get(0).getLaundSeeker_totalbal());
                        Toast.makeText(this, "Edit Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Please fill all fields! Male or Female is allowed in gender", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
