package com.example.jayvee.dirtoffseeker;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Keep;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WorkerProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    RatingBar ratingBar;
    TextView txtRating;
    UserDatabase db;
    ArrayList<Worker> list = new ArrayList<>();
    ImageView iv;
    ViewPagerAdapter adapter;
    String average = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.db = new UserDatabase(this);
        this.list = this.db.getAllWorker();
        this.iv = (ImageView) this.findViewById(R.id.imageView);
        Picasso.with(this).load(list.get(0).getLaundWorker_pic()).transform(new CircleTransform()).into(iv);
        this.txtRating = (TextView) this.findViewById(R.id.textView);
        this.ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);

        txtRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ratingIntent = new Intent(WorkerProfileActivity.this, MyAccountRatingActivity.class);
                ratingIntent.putExtra("average_rate", average);
                startActivity(ratingIntent);
            }
        });

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("workerRates").child(list.get(0).getLaundWorker_fbid());
        mDatabase1.addValueEventListener(new ValueEventListener() {
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

                    ratingBar.setRating(starf/dataSnapshot.getChildrenCount());
                    average = ""+starf/dataSnapshot.getChildrenCount();
                    txtRating.setText(new StringBuilder().append(dataSnapshot.getChildrenCount()).append(" Ratings"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new WorkerProfileFragment(), "Worker Information");
        adapter.addFragment(new WorkerScheduleFragment(), "Available Schedules");
        viewPager.setAdapter(adapter);
    }

    @Keep
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
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

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}