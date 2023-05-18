package com.d3k4y.project2.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.Models.MessageModel;
import com.d3k4y.project2.R;

public class msgHolder extends RecyclerView.ViewHolder{

    public MessageModel messageModel;
    public TextView my_msg_time,my_msg_body,my_msg_mail;

    public msgHolder(@NonNull View itemView) {
        super(itemView);
        my_msg_mail=itemView.findViewById(R.id.my_msg_mail);
        my_msg_body=itemView.findViewById(R.id.my_msg_body);
        my_msg_time=itemView.findViewById(R.id.my_msg_time);
    }
}
