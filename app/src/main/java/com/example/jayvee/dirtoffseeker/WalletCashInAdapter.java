package com.example.jayvee.dirtoffseeker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WalletCashInAdapter extends BaseAdapter {

    Context context;
    ArrayList<WalletCashIn> list;
    LayoutInflater inflater;

    public WalletCashInAdapter(Context context, ArrayList<WalletCashIn> list) {
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
            handler = new ItemHandler();
            view = inflater.inflate(R.layout.adapter_wallet_cash_in, null);

            handler.txtMessage = view.findViewById(R.id.textView);
            handler.txtDate = view.findViewById(R.id.textView1);
            handler.txtTime = view.findViewById(R.id.textView2);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        handler.txtMessage.setText(new StringBuilder().append("You have received ").append(list.get(i).getCashin_balance()).append(" load to your wallet."));
        handler.txtDate.setText(new StringBuilder().append(list.get(i).getCashin_date()));
        handler.txtTime.setText(new StringBuilder().append(list.get(i).getCashin_time()));

        return view;
    }

    static class ItemHandler {
        TextView txtMessage, txtDate, txtTime;
    }
}
