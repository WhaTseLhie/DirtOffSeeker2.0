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

public class WalletPayoutAdapter extends BaseAdapter {

    Context context;
    ArrayList<WalletPayout> list;
    LayoutInflater inflater;

    public WalletPayoutAdapter(Context context, ArrayList<WalletPayout> list) {
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
            view = inflater.inflate(R.layout.activity_wallet_payout_adapter, null);

            handler.txtBookingId = view.findViewById(R.id.textView);
            handler.txtServiceFee = view.findViewById(R.id.textView1);
            handler.txtPaymentDate = view.findViewById(R.id.textView2);

            view.setTag(handler);
        } else {
            handler = (ItemHandler) view.getTag();
        }

        handler.txtBookingId.setText(new StringBuilder().append(list.get(i).getBooking_id()));
        handler.txtServiceFee.setText(new StringBuilder().append(list.get(i).getPayout_fee()));
        handler.txtPaymentDate.setText(new StringBuilder().append(list.get(i).getPayout_date()));

        return view;
    }

    static class ItemHandler {
        TextView txtBookingId, txtServiceFee, txtPaymentDate;
    }
}
