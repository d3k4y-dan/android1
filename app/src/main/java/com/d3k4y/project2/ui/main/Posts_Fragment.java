package com.d3k4y.project2.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.Holder.postHolder;
import com.d3k4y.project2.Models.PostModel;
import com.d3k4y.project2.R;
import com.d3k4y.project2.add_posts;
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

public class Posts_Fragment extends Fragment {

    ImageButton add;
    RecyclerView postview;
    TextView nameee;

    private FirestoreRecyclerAdapter<PostModel, postHolder> fsFirestoreRecyclerAdapter;
    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.posts_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        nameee=v.findViewById(R.id.textView18);
        nameee.setText(user.getDisplayName()+"'s Feed");

        postview=(RecyclerView) v.findViewById(R.id.post_view);
        postview.setLayoutManager(new LinearLayoutManager(getContext()));

        Query loadPosts= db.collection("Posts").orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<PostModel>().setQuery(loadPosts,PostModel.class).build();

        fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<PostModel, postHolder>(recyclerOptions){

            @Override
            protected void onBindViewHolder(@NonNull postHolder holder, int position, @NonNull PostModel model) {
                holder.p_mail.setText("Posted by "+model.getEmail());
                holder.p_title.setText(model.getPost_title());
                holder.p_description.setText(model.getPost_description());
                storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getContext()).load(uri).into(holder.p_pic);
                    }
                });
            }

            @NonNull
            @Override
            public postHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_load,parent,false);
                return new postHolder(view);
            }
        };

        postview.setAdapter(fsFirestoreRecyclerAdapter);

        add=v.findViewById(R.id.imageButton4);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), add_posts.class);
                startActivity(i);
            }
        });

        return v;
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
