package com.d3k4y.project2.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.Edit_my_product;
import com.d3k4y.project2.Models.PostModel;
import com.d3k4y.project2.R;

public class myProductHolder extends RecyclerView.ViewHolder{

    public PostModel post;
    public TextView p_id,p_name;
    public Button view_product;

    public myProductHolder(@NonNull View itemView) {
        super(itemView);
        p_id=itemView.findViewById(R.id.view_my_pro_id);
        p_name=itemView.findViewById(R.id.view_my_pro_name);
        view_product=itemView.findViewById(R.id.edit_my_product);
        view_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(itemView.getContext(), Edit_my_product.class);
                i.putExtra("product_id",p_id.getText().toString());
                itemView.getContext().startActivity(i);
            }
        });
    }
}
