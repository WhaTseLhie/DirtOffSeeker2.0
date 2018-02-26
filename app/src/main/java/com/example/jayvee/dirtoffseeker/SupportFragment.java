package com.example.jayvee.dirtoffseeker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SupportFragment extends Fragment {

    EditText txtComment;
    TextView txtEmail;
    Button btnSubmit;
    UserDatabase userDatabase;
    ArrayList<User> seekerList = new ArrayList<>();

    public SupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        getActivity().setTitle("Support Us");

        userDatabase = new UserDatabase(getActivity());
        seekerList = userDatabase.getAllUser();

        txtEmail = view.findViewById(R.id.textView);
        txtEmail.setText(seekerList.get(0).getLaundSeeker_email());

        txtComment = view.findViewById(R.id.editText);
        btnSubmit = view.findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = txtComment.getText().toString();

                if(!comment.trim().equals("")) {
                    String email = (seekerList.get(0).getLaundSeeker_email().equals(""))?seekerList.get(0).getLaundSeeker_fn() +" "+ seekerList.get(0).getLaundSeeker_fn():seekerList.get(0).getLaundSeeker_email();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("supportUs");
                    String key = mDatabase.push().getKey();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("comment", comment);
                    hashMap.put("date", new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(new Date()));
                    hashMap.put("email", email);
                    hashMap.put("id", key);
                    mDatabase.child(key).setValue(hashMap);

                    txtComment.setText("");
                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Please leave a comment", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
