package com.example.jayvee.dirtoffseeker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FindWorkerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Worker> list;
    LayoutInflater inflater;

    public FindWorkerAdapter(Context context, ArrayList<Worker> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemHandler handler;

        if(view == null) {
            view = inflater.inflate(R.layout.activity_find_worker_adapter, null);
            handler = new ItemHandler();

            handler.iv = view.findViewById(R.id.imageView);
            handler.txtName = view.findViewById(R.id.textView);
            handler.txtAddress = view.findViewById(R.id.textView2);
            handler.txtEmail = view.findViewById(R.id.textView3);
            handler.txtCnum = view.findViewById(R.id.textView4);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        Picasso.with(view.getContext()).load(list.get(i).getLaundWorker_pic()).transform(new CircleTransform()).into(handler.iv);
        handler.txtName.setText(new StringBuilder().append(list.get(i).getLaundWorker_fn()).append(" ").append(list.get(i).getLaundWorker_mn()).append(" ").append(list.get(i).getLaundWorker_ln()));
        handler.txtAddress.setText(list.get(i).getLaundWorker_address());
        handler.txtEmail.setText(list.get(i).getLaundWorker_email());
        handler.txtCnum.setText(list.get(i).getLaundWorker_cnum());

        return view;
    }

    static class ItemHandler {
        ImageView iv;
        TextView txtName, txtAddress, txtEmail, txtCnum;
    }
}