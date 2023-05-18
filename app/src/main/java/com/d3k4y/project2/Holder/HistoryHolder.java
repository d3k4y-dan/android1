package com.d3k4y.project2.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.R;

public class HistoryHolder extends RecyclerView.ViewHolder{

    public TextView h_buy_mail,h_buy_qty,h_buy_price,h_buy_product_id,h_seller_mail;

    public HistoryHolder(@NonNull View itemView) {
        super(itemView);
        h_buy_mail=itemView.findViewById(R.id.h_buy_mail);
        h_buy_qty=itemView.findViewById(R.id.h_buy_qty);
        h_buy_price=itemView.findViewById(R.id.h_buy_price);
        h_buy_product_id=itemView.findViewById(R.id.h_buy_product_id);
        h_seller_mail=itemView.findViewById(R.id.textView55);
    }
}
