package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.d3k4y.project2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class delete_acc extends AppCompatActivity {

    EditText dcp1,dcp2;
    String email,pw1,pw2;
    Button deleteacc;

    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_acc);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        email=user.getEmail();

        dcp1=findViewById(R.id.dcp1);
        dcp2=findViewById(R.id.dcp2);
        deleteacc=findViewById(R.id.delete_acc);

        db.collection("UserData").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> list=queryDocumentSnapshots.toObjects(User.class);
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        u=list.get(i);
                    }
                }
            }
        });

        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    pw1=dcp1.getText().toString();
                    pw2=dcp2.getText().toString();

                    if(pw1.equals(pw2)){
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, pw1);

                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.collection("ProductData").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        for (int i = 0; i < task.getResult().size(); i++) {
                                                            String id=task.getResult().getDocuments().get(i).getId();
                                                            db.collection("ProductData").document(id).delete();
                                                        }
                                                    }
                                                }
                                            });

                                            db.collection("UserData").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(int i=0;i<task.getResult().size();i++){
                                                            String id=task.getResult().getDocuments().get(i).getId();
                                                            db.collection("UserData").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Account deleted!!!",
                                                                            Toast.LENGTH_SHORT)
                                                                            .show();
                                                                    finish();
                                                                    Intent i=new Intent(delete_acc.this,MainActivity.class);
                                                                    startActivity(i);
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }else{
                                    Snackbar.make(v.getRootView(), "Couldn't verify user", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        });
                    }else{
                        Snackbar.make(v.getRootView(), "Passwords doesn't match", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });

    }
}