package com.example.jayvee.dirtoffseeker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindWorkerActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    /*/////////////////////////////////
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    *////////////////////////////////
    Toolbar toolbar;
    UserDatabase userdb;
    ArrayList<User> list = new ArrayList<>();
    ListView lv;
    ArrayList<Worker> workerList = new ArrayList<>();
    //ArrayList<User> seekerList = new ArrayList<>();
    FindWorkerAdapter adapter;
    EditText txtLocation;
    Button btnSearch;
    //Spinner cboMode, cboServices;//, cboSort;
    //String mode, service;//, sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_worker);

        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userdb = new UserDatabase(this);
        list = userdb.getAllUser();

        //this.cboSort = (Spinner) this.findViewById(R.id.spinnerSort);
        //this.cboMode = (Spinner) this.findViewById(R.id.spinnerMode);
        //this.cboServices = (Spinner) this.findViewById(R.id.spinnerService);
        this.txtLocation = (EditText) this.findViewById(R.id.editText);
        this.btnSearch = (Button) this.findViewById(R.id.button);
        this.lv = (ListView) this.findViewById(R.id.listView2);
        this.adapter = new FindWorkerAdapter(this, workerList);
        this.lv.setAdapter(adapter);

        /*this.cboMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode = cboMode.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.cboServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                service = cboServices.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.cboSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), cboSort.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();

                sort = cboSort.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers");

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        workerList.clear();
                        //userdb.deleteAllWorker();
                        String address = txtLocation.getText().toString().toLowerCase();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Worker worker = child.getValue(Worker.class);

                            if(worker != null && worker.getLaundWorker_status().equalsIgnoreCase("Verified") && worker.getLaundWorker_address().toLowerCase().contains(address)) {
                                workerList.add(worker);
                                //userdb.addWorker(worker.getLaundWorker_address(), worker.getLaundWorker_bdate(), worker.getLaundWorker_cnum(), worker.getLaundWorker_dateApplied(), worker.getLaundWorker_email(), worker.getLaundWorker_fbid(), worker.getLaundWorker_fn(), worker.getLaundWorker_ln(), worker.getLaundWorker_mn(), worker.getLaundWorker_pic(), worker.getLaundWorker_status());
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(FindWorkerActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list = userdb.getAllUser();
                if(!list.get(0).getLaundSeeker_totalbal().equals("")) {
                    userdb.deleteAllWorker();
                    userdb.addWorker(
                            workerList.get(i).getLaundWorker_address(),
                            workerList.get(i).getLaundWorker_bdate(),
                            workerList.get(i).getLaundWorker_cnum(),
                            workerList.get(i).getLaundWorker_dateApplied(),
                            workerList.get(i).getLaundWorker_email(),
                            workerList.get(i).getLaundWorker_fbid(),
                            workerList.get(i).getLaundWorker_fn(),
                            workerList.get(i).getLaundWorker_ln(),
                            workerList.get(i).getLaundWorker_mn(),
                            workerList.get(i).getLaundWorker_pic(),
                            workerList.get(i).getLaundWorker_status());

                    Intent workerIntent = new Intent(FindWorkerActivity.this, WorkerProfileActivity.class);
                    startActivity(workerIntent);
                } else {
                    Toast.makeText(FindWorkerActivity.this, "You have not enough balance! Please top-up immediately", Toast.LENGTH_SHORT).show();
                }

                /*AlertDialog.Builder builder = new AlertDialog.Builder(FindWorkerActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to book this worker?");
                builder.setPositiveButton("Yes", FindWorkerActivity.this);
                builder.setNegativeButton("No", FindWorkerActivity.this);

                AlertDialog dialog = builder.create();
                dialog.show();*/
            }
        });

        /*adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.notifyDataSetChanged();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundryWorkers");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workerList.clear();

                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Worker worker = child.getValue(Worker.class);

                    if(worker != null && worker.getLaundWorker_status().equalsIgnoreCase("Verified")) {
                        workerList.add(worker);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FindWorkerActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                Toast.makeText(this, "Booking Successful", Toast.LENGTH_SHORT).show();

                break;

            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this, "Booking Canceled", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    /*private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new StepOneFragment(), "Step 1");
        adapter.addFragment(new StepTwoFragment(), "Step 2");
        adapter.addFragment(new StepThreeFragment(), "Step 3");
        adapter.addFragment(new StepFourFragment(), "Step 4");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
}