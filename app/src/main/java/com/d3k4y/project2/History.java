package com.d3k4y.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class History extends AppCompatActivity {

    ConstraintLayout buy_history,sell_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        buy_history=findViewById(R.id.buy_history);
        sell_history=findViewById(R.id.sell_history);

        buy_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(History.this, Buy_history.class);
                startActivity(i);
            }
        });

        sell_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(History.this, Sell_history.class);
                startActivity(i);
            }
        });

    }
}