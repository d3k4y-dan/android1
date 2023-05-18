package com.d3k4y.project2.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.Models.PostModel;
import com.d3k4y.project2.R;

public class postHolder extends RecyclerView.ViewHolder {

    public TextView p_mail,p_title,p_description;
    public ImageView p_pic;
    public PostModel post;

    public postHolder(@NonNull View itemView) {
        super(itemView);

        p_mail=itemView.findViewById(R.id.p_mail);
        p_title=itemView.findViewById(R.id.p_title);
        p_description=itemView.findViewById(R.id.p_description);
        p_pic=itemView.findViewById(R.id.p_pic);

    }
}
