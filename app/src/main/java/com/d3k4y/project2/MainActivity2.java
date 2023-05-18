package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.d3k4y.project2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    EditText e,n,pw1,pw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        e=findViewById(R.id.email2);
        n=findViewById(R.id.name1);
        pw1=findViewById(R.id.editTextTextPassword);
        pw2=findViewById(R.id.editTextTextPassword2);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button signup=findViewById(R.id.button2);

        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String p = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern=Pattern.compile(p);
                String email=e.getText().toString();
                Matcher matcher=pattern.matcher(email);

                if (matcher.matches()){
                    signup.setClickable(true);
                    e.setBackgroundResource(R.drawable.edittext_1);
                    e.setPadding(10,10,10,10);
                }else{
                    signup.setClickable(false);
                    e.setBackgroundResource(R.drawable.edittext_error);
                    e.setPadding(10,10,10,10);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String email=e.getText().toString();
                    String password=pw1.getText().toString();
                    String verify=pw2.getText().toString();
                    String name=n.getText().toString();

                    if(password.equals(verify)){
                        db.collection("UserData").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<User> list=queryDocumentSnapshots.toObjects(User.class);
                                if(list.size()>0){
                                    Toast.makeText(getApplicationContext(), "Email exists", Toast.LENGTH_LONG).show();
                                }else{
                                    Intent i=new Intent(getApplicationContext(),Register.class);
                                    i.putExtra("auth_name",name);
                                    i.putExtra("auth_mail",email);
                                    i.putExtra("auth_pw",password);
                                    startActivity(i);
                                }
                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "Passwords doesn't match", Toast.LENGTH_LONG).show();
                    }

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