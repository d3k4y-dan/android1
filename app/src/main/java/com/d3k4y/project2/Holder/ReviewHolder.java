package com.d3k4y.project2.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.R;

public class ReviewHolder extends RecyclerView.ViewHolder{
    public TextView lr_mail,lr_body;
    public ReviewHolder(@NonNull View itemView) {
        super(itemView);
        lr_mail=itemView.findViewById(R.id.lr_mail);
        lr_body=itemView.findViewById(R.id.lr_body);
    }
}
