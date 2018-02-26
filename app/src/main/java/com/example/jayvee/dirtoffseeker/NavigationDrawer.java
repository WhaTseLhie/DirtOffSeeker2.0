package com.example.jayvee.dirtoffseeker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView navFullname;
    ArrayList<User> list = new ArrayList<>();
    UserDatabase userdb;
    ImageView iv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        /////////
        this.userdb = new UserDatabase(this);
        this.list = this.userdb.getAllUser();

        if(!list.isEmpty()) {
            String fname = this.list.get(0).getLaundSeeker_fn();
            String lname = this.list.get(0).getLaundSeeker_ln();
            String url = this.list.get(0).getLaundSeeker_pic();
            String str = fname+ " " +lname;

            iv = headerView.findViewById(R.id.imageView);
            Picasso.with(this).load(url).transform(new CircleTransform()).into(iv);
            navFullname = headerView.findViewById(R.id.textView);
            navFullname.setText(str);

            /*if(image != null) {
                if (!image.startsWith("file")) {
                    new NavigationDrawer.DownloadImage(iv).execute(image);
                } else {
                    try {
                        iv.setImageURI(Uri.parse(image));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error image" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "image null", Toast.LENGTH_LONG).show();
            }*/
        } else {
            Toast.makeText(this, "Something went wrong! Please restart the application", Toast.LENGTH_LONG).show();
            LoginManager.getInstance().logOut();
            userdb.deleteAllUser();
            this.finish();
        }

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    /*public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Uri uri;
        UserDatabase db;
        ArrayList<User> list = new ArrayList<>();

        private DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
            db = new UserDatabase(NavigationDrawer.this);
            list = db.getAllUser();
            db.deleteAllUser();
        }

        protected Bitmap doInBackground(String...urls) {
            String urldisplay = urls[0];
            Bitmap micon = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                micon = BitmapFactory.decodeStream(in);

                uri = getImageUri(micon);
                list.get(0).setLaundSeeker_pic(uri.toString());
                db.addUser(list.get(0).getLaundSeeker_cnum(), list.get(0).getLaundSeeker_email(), list.get(0).getLaundSeeker_fbid(), list.get(0).getLaundSeeker_fn(), list.get(0).getLaundSeeker_gender(), list.get(0).getLaundSeeker_ln(), list.get(0).getLaundSeeker_link(), uri.toString(), list.get(0).getLaundSeeker_status(), list.get(0).getLaundSeeker_totalbal());

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySeekers");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("laundSeeker_cnum", list.get(0).getLaundSeeker_cnum());
                hashMap.put("laundSeeker_email", list.get(0).getLaundSeeker_email());
                hashMap.put("laundSeeker_fbid", list.get(0).getLaundSeeker_fbid());
                hashMap.put("laundSeeker_fn", list.get(0).getLaundSeeker_fn());
                hashMap.put("laundSeeker_gender", list.get(0).getLaundSeeker_gender());
                hashMap.put("laundSeeker_ln", list.get(0).getLaundSeeker_ln());
                hashMap.put("laundSeeker_link", list.get(0).getLaundSeeker_link());
                hashMap.put("laundSeeker_pic", uri.toString());
                hashMap.put("laundSeeker_status", list.get(0).getLaundSeeker_status());
                hashMap.put("laundSeeker_totalbal", list.get(0).getLaundSeeker_totalbal());
                mDatabase.child(list.get(0).getLaundSeeker_fbid()).setValue(hashMap);
            } catch(Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return micon;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                bmImage.setImageURI(Uri.parse(list.get(0).getLaundSeeker_pic()));
            } catch(Exception e) {
                //Log.d("TAG", e.getMessage());
            }
        }

        private Uri getImageUri(Bitmap icon) {
            Uri uri = null;

            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                // calculate insamplesize
                options.inSampleSize = calculateInSampleSize(options, 100, 100);

                // decode bitmap with insamplesize set
                options.inJustDecodeBounds = false;
                Bitmap newBitmap = Bitmap.createScaledBitmap(icon, 100,100, true);
                File file = new File(NavigationDrawer.this.getFilesDir(), "Image" +new Random().nextInt()+ ".jpeg");
                FileOutputStream out = NavigationDrawer.this.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                // get absolute path
                String realPath = file.getAbsolutePath();
                File f = new File(realPath);
                uri = Uri.fromFile(f);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                e.printStackTrace();
            }

            return uri;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height/ (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio; } final float totalPixels = width * height; final float totalReqPixelsCap = reqWidth * reqHeight * 2; while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_worker) {
            if (list.get(0).getLaundSeeker_status().equalsIgnoreCase("verified")) {
//                Intent workerIntent = new Intent(NavigationDrawer.this, FindWorkerActivity.class);
//                startActivity(workerIntent);
                FindWorkerFragment fragment = new FindWorkerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.linearLayout, fragment, fragment.getTag()).commit();
                getSupportActionBar().setTitle("Find Laundry Worker");
            } else {
                Toast.makeText(this, "Unverified or Blocked users are not allowed to book", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_account) {
            Intent accountIntent = new Intent(NavigationDrawer.this, MyAccount.class);
            startActivity(accountIntent);
        } else if (id == R.id.nav_inbox) {
            InboxFragment inboxFragment = new InboxFragment();
            FragmentManager inboxManager = getSupportFragmentManager();
            inboxManager.beginTransaction().replace(
                    R.id.linearLayout,
                    inboxFragment,
                    inboxFragment.getTag()
            ).commit();
            getSupportActionBar().setTitle("Messages");
        } else if (id == R.id.nav_booking_list) {
            BookingFragment bookingFragment = new BookingFragment();
            FragmentManager bookingManager = getSupportFragmentManager();
            bookingManager.beginTransaction().replace(
                    R.id.linearLayout,
                    bookingFragment,
                    bookingFragment.getTag()
            ).commit();
            getSupportActionBar().setTitle("Booking List");
        } else if (id == R.id.nav_wallet) {
            WalletFragment walletFragment = new WalletFragment();
            FragmentManager walletManager = getSupportFragmentManager();
            walletManager.beginTransaction().replace(
                    R.id.linearLayout,
                    walletFragment,
                    walletFragment.getTag()
            ).commit();
            getSupportActionBar().setTitle("E-Wallet");
        } else if (id == R.id.nav_support) {
            SupportFragment supportFragment = new SupportFragment();
            FragmentManager supportManager = getSupportFragmentManager();
            supportManager.beginTransaction().replace(
                    R.id.linearLayout,
                    supportFragment,
                    supportFragment.getTag()
            ).commit();
            getSupportActionBar().setTitle("Support");
        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            this.userdb.deleteAllUser();
            this.userdb.deleteAllWorker();
            this.userdb.deleteAllAvailabilityMonthly();
            this.userdb.deleteAllAvailabilityWeekly();
            this.userdb.deleteAllCashIn();
            this.userdb.deleteAllPayout();
            this.userdb.deleteAllFeedback();
            this.userdb.deleteAllBooking();
            this.userdb.deleteAllHistoryBooking();

            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}