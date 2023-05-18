package com.d3k4y.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.d3k4y.project2.Models.PracticalModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Practical extends AppCompatActivity {

    FirebaseFirestore db;
    EditText practical_name,practical_name2;
    Button praactical_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical);

        db = FirebaseFirestore.getInstance();

        practical_name=findViewById(R.id.practical_name);
        practical_name2=findViewById(R.id.practical_name2);
        praactical_submit=findViewById(R.id.praactical_submit);

        praactical_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name1=practical_name.getText().toString();
                String name2=practical_name2.getText().toString();

                PracticalModel pm=new PracticalModel(name1,name2);

                db.collection("Practical").add(pm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"submitterd",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}