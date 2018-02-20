package com.example.jayvee.dirtoffseeker;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StepThreeFragment extends Fragment {

    UserDatabase userdb;
    ArrayList<User> list = new ArrayList<>();
    ListView lv;
    ArrayList<Worker> workerList = new ArrayList<>();
    FindWorkerAdapter adapter;

    public StepThreeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_step_three_fragment, container, false);

        userdb = new UserDatabase(getActivity());
        list = userdb.getAllUser();

        lv = view.findViewById(R.id.listView);
        this.adapter = new FindWorkerAdapter(getActivity(), workerList);
        this.lv.setAdapter(adapter);

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
                Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog dialog;
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to book this worker?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Booking Successful", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Book Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
}