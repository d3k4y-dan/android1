package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.d3k4y.project2.Holder.myProductHolder;
import com.d3k4y.project2.Holder.productHolder;
import com.d3k4y.project2.Models.ProductModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class View_my_products extends AppCompatActivity {

    RecyclerView productview;

    private FirestoreRecyclerAdapter<ProductModel, myProductHolder> fsFirestoreRecyclerAdapter;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_products);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        productview=findViewById(R.id.my_product_view);
        productview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query loadProducts= db.collection("ProductData").whereEqualTo("email",user.getEmail());
        FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(loadProducts,ProductModel.class).build();

        fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ProductModel, myProductHolder>(recyclerOptions) {

            @NonNull
            @Override
            public myProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_my_products,parent,false);
                return new myProductHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull myProductHolder holder, int position, @NonNull ProductModel model) {
                String id = fsFirestoreRecyclerAdapter.getSnapshots().getSnapshot(position).getId();
                holder.p_id.setText(id);
                holder.p_name.setText(model.getName());
            }
        };

        productview.setAdapter(fsFirestoreRecyclerAdapter);

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