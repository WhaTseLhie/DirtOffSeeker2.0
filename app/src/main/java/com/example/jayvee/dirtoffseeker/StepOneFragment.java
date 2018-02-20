package com.example.jayvee.dirtoffseeker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class StepOneFragment extends Fragment {

    final static int ADDRESS_PICK_REQUEST_CODE = 10;
    String address = "";
    TextView txtPlace;
    Button btnChange;

    public StepOneFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_step_one_fragment, container, false);

        txtPlace = view.findViewById(R.id.textView);
        btnChange = view.findViewById(R.id.button);

        /*if(address.equals("")) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent placeIntent;

            try {
                placeIntent = builder.build(getActivity());
                startActivityForResult(placeIntent, ADDRESS_PICK_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }*/

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent placeIntent;

                try {
                    placeIntent = builder.build(getActivity());
                    startActivityForResult(placeIntent, ADDRESS_PICK_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDRESS_PICK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                address = place.getAddress().toString();
                txtPlace.setText(address);
            }
        }
    }
}