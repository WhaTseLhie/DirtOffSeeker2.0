package com.example.jayvee.dirtoffseeker;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InboxFragment extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;
    private FloatingActionButton fab;
    private RelativeLayout activity_main;
    private ListView listOfMessages;
    private UserDatabase userDatabase;
    private ArrayList<Worker> workerList = new ArrayList<>();
    private ArrayList<Booking> bookingList = new ArrayList<>();
    private EditText input;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        userDatabase = new UserDatabase(getContext());
        workerList = userDatabase.getAllWorker();
        bookingList = userDatabase.getAllBooking();

        input = (EditText) view.findViewById(R.id.input);
        activity_main = (RelativeLayout) view.findViewById(R.id.activity_main);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listOfMessages = (ListView) view.findViewById(R.id.list_of_message);

        if(!bookingList.isEmpty()) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!input.getText().toString().trim().equals("")) {
                        FirebaseDatabase.getInstance().getReference().child("chatMessages").child(workerList.get(0).getLaundWorker_fbid()).push().setValue(new ChatMessage(input.getText().toString(),
                                workerList.get(0).getLaundWorker_fn() + " " + workerList.get(0).getLaundWorker_mn() + " " + workerList.get(0).getLaundWorker_ln()));
                        FirebaseDatabase.getInstance().getReference().child("chatMessages").child(workerList.get(0).getLaundWorker_fbid()).keepSynced(true);
                        input.setText("");
                        displayChatMessages();
                    }
                }
            });

            displayChatMessages();
        }

        return view;
    }

    private void displayChatMessages() {
        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference().child("chatMessages").child(workerList.get(0).getLaundWorker_fbid())) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("MM/dd/yyyy (HH:mm:ss)" ,model.getMesssageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}