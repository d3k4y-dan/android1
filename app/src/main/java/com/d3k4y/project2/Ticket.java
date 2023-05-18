package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.d3k4y.project2.Models.Ticketmodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket extends AppCompatActivity {

    EditText tt,td;
    Button submit;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        tt=findViewById(R.id.t_title);
        td=findViewById(R.id.t_description);
        submit=findViewById(R.id.submit_ticket);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String t_title=tt.getText().toString();
                    String t_description=td.getText().toString();
                    String mail=user.getEmail();
                    Date d=new Date();
                    DateFormat df = new SimpleDateFormat("dd/MM/yy");
                    String date=String.valueOf(df.format(d));

                    Ticketmodel t=new Ticketmodel(mail,t_title,t_description,date);

                    db.collection("Tickets").add(t).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),
                                    "Ticket submitted",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(v.getRootView(), "Ticket submission failed", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG)
                            .show();

                }

            }
        });

    }
}