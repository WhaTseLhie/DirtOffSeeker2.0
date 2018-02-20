package com.example.jayvee.dirtoffseeker;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    CallbackManager callbackManager;
    LoginButton loginButton;
    UserDatabase userdb;
    ArrayList<User> list = new ArrayList<>();
    DatabaseReference mDatabase;
    Uri profilePictureUri;
    String profileId = "", firstName = "", lastName = "", email = "", link = "", gender = "", cnum = "", status = "Verified", totalbal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("laundrySeekers");
        userdb = new UserDatabase(MainActivity.this);
        list = userdb.getAllUser();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        if(AccessToken.getCurrentAccessToken() == null) {
            callbackManager = CallbackManager.Factory.create();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Profile profile = Profile.getCurrentProfile();

                            if(profile != null) {
                                try {
                                    profileId = object.getString("id");
                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    email = object.getString("email");
                                    link = object.getString("link");
                                    gender = object.getString("gender");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                profilePictureUri = profile.getProfilePictureUri(100, 100);

                                Query id = mDatabase.orderByKey().equalTo(profileId);
                                id.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // if old user => get user information from firebase
                                        if (dataSnapshot.exists()) {
                                            try {
                                                User user = null;

                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    user = child.getValue(User.class);
                                                }

                                                if(user != null) {
                                                    userdb.deleteAllUser();
                                                    userdb.addUser(user.getLaundSeeker_cnum(), user.getLaundSeeker_email(), user.getLaundSeeker_fbid(), user.getLaundSeeker_fn(), user.getLaundSeeker_gender(), user.getLaundSeeker_ln(), user.getLaundSeeker_link(), user.getLaundSeeker_pic(), user.getLaundSeeker_status(), user.getLaundSeeker_totalbal());
                                                    Toast.makeText(getApplicationContext(), "Welcome Back! " + user.getLaundSeeker_fn() + " " + user.getLaundSeeker_ln(), Toast.LENGTH_LONG).show();
                                                }

                                                Intent navIntent2 = new Intent(getApplicationContext(), NavigationDrawer.class);
                                                startActivity(navIntent2);
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            // if new user => push user information to firebase
                                        } else {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("laundSeeker_cnum", cnum);
                                            hashMap.put("laundSeeker_email", email);
                                            hashMap.put("laundSeeker_fbid", profileId);
                                            hashMap.put("laundSeeker_fn", firstName);
                                            hashMap.put("laundSeeker_gender", gender);
                                            hashMap.put("laundSeeker_ln", lastName);
                                            hashMap.put("laundSeeker_link", link);
                                            hashMap.put("laundSeeker_pic", profilePictureUri.toString());
                                            hashMap.put("laundSeeker_status", status);
                                            hashMap.put("laundSeeker_totalbal", totalbal);

                                            mDatabase.child(profileId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Error in storing data to the cloud! Please check your internet connection", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                            userdb.deleteAllUser();
                                            list.add(new User(cnum, email, profileId, firstName, gender, lastName, link, profilePictureUri.toString(), status, totalbal));
                                            userdb.addUser(cnum, email, profileId, firstName, gender, lastName, link, profilePictureUri.toString(), status, totalbal);
                                            Toast.makeText(getApplicationContext(), "Hello " + firstName + " " + lastName, Toast.LENGTH_LONG).show();

                                            Intent navIntent = new Intent(getApplicationContext(), NavigationDrawer.class);
                                            startActivity(navIntent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(), "Error! Please check your internet connection", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "first_name, last_name, email, id, link, gender");
                    graphRequest.setParameters(parameters);
                    graphRequest.executeAsync();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Login Canceled", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "Login Error! Please check your internet connection", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Welcome Back! " + list.get(0).getLaundSeeker_fn() + " " + list.get(0).getLaundSeeker_ln(), Toast.LENGTH_LONG).show();
            Intent navIntent = new Intent(getApplicationContext(), NavigationDrawer.class);
            startActivity(navIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            Query id = mDatabase.orderByKey().equalTo(list.get(0).getLaundSeeker_fbid());
            id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        User user = null;

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            user = child.getValue(User.class);
                        }

                        if(user != null) {
                            userdb.deleteAllUser();
                            userdb.addUser(user.getLaundSeeker_cnum(), user.getLaundSeeker_email(), user.getLaundSeeker_fbid(), user.getLaundSeeker_fn(), user.getLaundSeeker_gender(), user.getLaundSeeker_ln(), user.getLaundSeeker_link(), user.getLaundSeeker_pic(), user.getLaundSeeker_status(), user.getLaundSeeker_totalbal());
                            Toast.makeText(getApplicationContext(), "Welcome Back! " + user.getLaundSeeker_fn() + " " + user.getLaundSeeker_ln(), Toast.LENGTH_LONG).show();
                        }

                        Intent navIntent2 = new Intent(getApplicationContext(), NavigationDrawer.class);
                        startActivity(navIntent2);
                    } else {
                        Toast.makeText(getApplicationContext(), "Not Exist! Something went wrong! Please restart the application", Toast.LENGTH_LONG).show();
                        userdb.deleteAllUser();
                        MainActivity.this.finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", this);
        builder.setNegativeButton("No", this);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case DialogInterface.BUTTON_POSITIVE:
                this.finish();
                System.exit(0);

                break;
        }
    }
}