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

public class SupportFragment extends Fragment {

    EditText txtComment;
    Button btnSubmit;

    public SupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        getActivity().setTitle("Support Us");

        txtComment = view.findViewById(R.id.editText);
        btnSubmit = view.findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = txtComment.getText().toString();
                if(!comment.trim().equals("")) {
                    Toast.makeText(getActivity(), comment, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
