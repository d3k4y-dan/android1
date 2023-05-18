package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d3k4y.project2.Holder.HistoryHolder;
import com.d3k4y.project2.Models.BuyModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Sell_history extends AppCompatActivity {

    RecyclerView sell_view;

    private FirestoreRecyclerAdapter<BuyModel, HistoryHolder> fsFirestoreRecyclerAdapter;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        sell_view =findViewById(R.id.sell_history_view);
        sell_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query loadProducts= db.collection("ProductBuyDetails").whereEqualTo("seller_mail",user.getEmail());
        FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<BuyModel>().setQuery(loadProducts, BuyModel.class).build();
        fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<BuyModel, HistoryHolder>(recyclerOptions){

            @NonNull
            @Override
            public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout,parent,false);
                return new HistoryHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull BuyModel model) {
                holder.h_buy_mail.setText(model.getBuyer_mail());
                holder.h_buy_qty.setText(model.getBuy_qty());
                holder.h_buy_price.setText(model.getTotal_price());
                holder.h_buy_product_id.setText(model.getProduct_id());
            }
        };

        sell_view.setAdapter(fsFirestoreRecyclerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        fsFirestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fsFirestoreRecyclerAdapter.stopListening();
    }

}