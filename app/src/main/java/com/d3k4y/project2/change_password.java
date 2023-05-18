package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.d3k4y.project2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class change_password extends AppCompatActivity {

    EditText cp,np1,np2;
    Button submit;
    String current,new1,new2,email,password,id;

    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        cp=findViewById(R.id.ccp);
        np1=findViewById(R.id.cnp1);
        np2=findViewById(R.id.cnp2);

        email=user.getEmail();

        db.collection("UserData").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> list=queryDocumentSnapshots.toObjects(User.class);
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        u=list.get(i);
                        password=u.getPassword();
                    }
                }
            }
        });

        submit=findViewById(R.id.change_pw);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    current=cp.getText().toString();
                    new1=np1.getText().toString();
                    new2=np2.getText().toString();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, current);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                if(new1.equals(new2)){
                                    user.updatePassword(new1);
                                    db.collection("UserData").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(int i=0;i<task.getResult().size();i++){
                                                    id=task.getResult().getDocuments().get(i).getId();
                                                    u.setPassword(new1);
                                                    db.collection("UserData").document(id).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Password updated",
                                                                    Toast.LENGTH_SHORT)
                                                                    .show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Snackbar.make(v.getRootView(), "Failed to updated password", Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }else{
                                    Snackbar.make(v.getRootView(), "New Passwords doesn't match", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }

                            }else{
                                Snackbar.make(v.getRootView(), "Couldn't verify user", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });

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