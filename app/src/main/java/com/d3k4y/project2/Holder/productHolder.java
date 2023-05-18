package com.d3k4y.project2.Holder;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.Models.ProductModel;
import com.d3k4y.project2.R;
import com.d3k4y.project2.View_selected_product;
import com.d3k4y.project2.buy_product;

public class productHolder extends RecyclerView.ViewHolder{

    public TextView v_product_id,v_product_name,v_product_seller_mail,v_product_price;
    public ImageView v_product_pic;
    public Button v_product_view;
    public ProductModel productModel;

    public productHolder(@NonNull View itemView) {
        super(itemView);

        v_product_id=itemView.findViewById(R.id.v_product_id);
        v_product_name=itemView.findViewById(R.id.v_product_name);
        v_product_seller_mail=itemView.findViewById(R.id.v_product_seller_mail);
        v_product_price=itemView.findViewById(R.id.v_product_price);
        v_product_pic=itemView.findViewById(R.id.v_product_pic);
        v_product_view=itemView.findViewById(R.id.v_product_view);

        v_product_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(itemView.getContext(), View_selected_product.class);
                i.putExtra("v_product_id",v_product_id.getText().toString());
                itemView.getContext().startActivity(i);
            }
        });

    }

}
