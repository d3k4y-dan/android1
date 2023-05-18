package com.d3k4y.project2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Search_fragment extends Fragment {

    RecyclerView productview;
    ImageButton add_product;

    private FirestoreRecyclerAdapter<ProductModel, productHolder> fsFirestoreRecyclerAdapter;
    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        productview=(RecyclerView) v.findViewById(R.id.product_search_view);
        productview.setLayoutManager(new GridLayoutManager(getContext(),2));

        Bundle b=getArguments();
        String category=b.getString("category");
        String text=b.getString("text");

        try{
            if(category.equals("t")){
                Query loadProducts= db.collection("ProductData").orderBy("name", Query.Direction.ASCENDING);
                FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(loadProducts,ProductModel.class).build();
                fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ProductModel, productHolder>(recyclerOptions){

                    @Override
                    protected void onBindViewHolder(@NonNull productHolder holder, int position, @NonNull ProductModel model) {
                        String id = fsFirestoreRecyclerAdapter.getSnapshots().getSnapshot(position).getId();
                        holder.v_product_id.setText(id);
                        holder.v_product_seller_mail.setText(model.getEmail());
                        holder.v_product_name.setText(model.getName());
                        holder.v_product_price.setText(model.getPrice());
                        storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).fit().into(holder.v_product_pic);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_load,parent,false);
                        return new productHolder(view);
                    }

                };

                productview.setAdapter(fsFirestoreRecyclerAdapter);
            }
            if(category.equals("Merch name")){
                Query loadProducts= db.collection("ProductData").orderBy("name", Query.Direction.ASCENDING).startAt(text).endAt(text+"\uf8ff");
                FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(loadProducts,ProductModel.class).build();
                fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ProductModel, productHolder>(recyclerOptions){

                    @Override
                    protected void onBindViewHolder(@NonNull productHolder holder, int position, @NonNull ProductModel model) {
                        String id = fsFirestoreRecyclerAdapter.getSnapshots().getSnapshot(position).getId();
                        holder.v_product_id.setText(id);
                        holder.v_product_seller_mail.setText(model.getEmail());
                        holder.v_product_name.setText(model.getName());
                        holder.v_product_price.setText(model.getPrice());
                        storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).fit().into(holder.v_product_pic);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_load,parent,false);
                        return new productHolder(view);
                    }

                };

                productview.setAdapter(fsFirestoreRecyclerAdapter);

            }else if(category.equals("Merch brand")){
                Query loadProducts= db.collection("ProductData").orderBy("brand", Query.Direction.ASCENDING).startAt(text).endAt(text+"\uf8ff");
                FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(loadProducts,ProductModel.class).build();
                fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ProductModel, productHolder>(recyclerOptions){

                    @Override
                    protected void onBindViewHolder(@NonNull productHolder holder, int position, @NonNull ProductModel model) {
                        String id = fsFirestoreRecyclerAdapter.getSnapshots().getSnapshot(position).getId();
                        holder.v_product_id.setText(id);
                        holder.v_product_seller_mail.setText(model.getEmail());
                        holder.v_product_name.setText(model.getName());
                        holder.v_product_price.setText(model.getPrice());
                        storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).fit().into(holder.v_product_pic);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_load,parent,false);
                        return new productHolder(view);
                    }

                };

                productview.setAdapter(fsFirestoreRecyclerAdapter);
            }else if(category.equals("Merch gender")){
                Query loadProducts= db.collection("ProductData").orderBy("gender", Query.Direction.ASCENDING).startAt(text).endAt(text+"\uf8ff");
                FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(loadProducts,ProductModel.class).build();
                fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ProductModel, productHolder>(recyclerOptions){

                    @Override
                    protected void onBindViewHolder(@NonNull productHolder holder, int position, @NonNull ProductModel model) {
                        String id = fsFirestoreRecyclerAdapter.getSnapshots().getSnapshot(position).getId();
                        holder.v_product_id.setText(id);
                        holder.v_product_seller_mail.setText(model.getEmail());
                        holder.v_product_name.setText(model.getName());
                        holder.v_product_price.setText(model.getPrice());
                        storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).fit().into(holder.v_product_pic);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_load,parent,false);
                        return new productHolder(view);
                    }

                };
                productview.setAdapter(fsFirestoreRecyclerAdapter);
            }else if(category.equals("Merch category")){
                Query loadProducts= db.collection("ProductData").orderBy("category", Query.Direction.ASCENDING).startAt(text).endAt(text+"\uf8ff");
                FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(loadProducts,ProductModel.class).build();
                fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ProductModel, productHolder>(recyclerOptions){

                    @Override
                    protected void onBindViewHolder(@NonNull productHolder holder, int position, @NonNull ProductModel model) {
                        String id = fsFirestoreRecyclerAdapter.getSnapshots().getSnapshot(position).getId();
                        holder.v_product_id.setText(id);
                        holder.v_product_seller_mail.setText(model.getEmail());
                        holder.v_product_name.setText(model.getName());
                        holder.v_product_price.setText(model.getPrice());
                        storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getContext()).load(uri).fit().into(holder.v_product_pic);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_load,parent,false);
                        return new productHolder(view);
                    }

                };
                productview.setAdapter(fsFirestoreRecyclerAdapter);
            }
        }catch (Exception e){
            Toast.makeText(getContext(),
                    e.getMessage().toString(),
                    Toast.LENGTH_SHORT)
                    .show();
        }

        add_product=v.findViewById(R.id.add_product_page2);
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), add_product.class);
                startActivity(i);
            }
        });

        return  v;

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