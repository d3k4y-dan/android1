package com.d3k4y.project2.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d3k4y.project2.Holder.msgHolder;
import com.d3k4y.project2.Models.MessageModel;
import com.d3k4y.project2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Chats_Fragment extends Fragment {

    EditText chat_body;
    ImageButton send_chat;
    RecyclerView chat_body_view;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseRecyclerAdapter<MessageModel, msgHolder> fsFirestoreRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.chat_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chat");

        chat_body=v.findViewById(R.id.chat_body);
        send_chat=v.findViewById(R.id.send_chat);
        chat_body_view=v.findViewById(R.id.chat_body_view);
        chat_body_view.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query1 = FirebaseDatabase.getInstance().getReference().child("chat").orderByKey();
        FirebaseRecyclerOptions<MessageModel> recyclerOptions =
                new FirebaseRecyclerOptions.Builder<MessageModel>()
                        .setQuery(query1, MessageModel.class)
                        .build();
        fsFirestoreRecyclerAdapter=new FirebaseRecyclerAdapter<MessageModel, msgHolder>(recyclerOptions){

            @NonNull
            @Override
            public msgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymsg_layout,parent,false);
                return new msgHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull msgHolder holder, int position, @NonNull MessageModel model) {
                holder.my_msg_mail.setText(model.getEmail());
                holder.my_msg_body.setText(model.getChat());
                holder.my_msg_time.setText(model.getDate());
            }
        };

        chat_body_view.setAdapter(fsFirestoreRecyclerAdapter);

        send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=chat_body.getText().toString();
                if(!msg.isEmpty()){
                    String email=user.getEmail();

                    Date d=new Date();
                    DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String date=df.format(d);

                    MessageModel mm=new MessageModel(email,msg,date);

                    String key = myRef.push().getKey();
                    myRef.child(key).setValue(mm).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            chat_body.setText("");
                        }
                    });

                }
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