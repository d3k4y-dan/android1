package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.d3k4y.project2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView register,f_pw;
    Button login;
    EditText e,pw;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user!=null) {

            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(intent);

        } else {

            e=findViewById(R.id.email1);
            pw=findViewById(R.id.password1);
            login=findViewById(R.id.button);
            f_pw=findViewById(R.id.f_pw);

            register=findViewById(R.id.textView6);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(i);
                }
            });

            f_pw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(MainActivity.this,Forgot_password.class);
                    startActivity(i);
                    finish();
                }
            });

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

                    if(matcher.matches()){
                        login.setClickable(true);
                        e.setBackgroundResource(R.drawable.edittext_1);
                        e.setPadding(10,10,10,10);
                    }else{
                        login.setClickable(false);
                        e.setBackgroundResource(R.drawable.edittext_error);
                        e.setPadding(10,10,10,10);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        String email=e.getText().toString();
                        String password=pw.getText().toString();

                        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()){
                                        FirebaseFirestore db=FirebaseFirestore.getInstance();
                                        db.collection("UserData").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<User> list=queryDocumentSnapshots.toObjects(User.class);
                                                if(list.size()>0){
                                                    User u=null;
                                                    for(int i=0;i<list.size();i++){
                                                        u=list.get(i);
                                                    }
                                                    Toast.makeText(getApplicationContext(), "Welcome "+u.getName(), Toast.LENGTH_LONG).show();
                                                    Intent intent=new Intent(MainActivity.this,MainActivity3.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                    }else{
                                        Snackbar.make(v.getRootView(), "Verify your email 1st", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        user.sendEmailVerification();
                                    }

                                }else{
                                    Snackbar.make(v.getRootView(), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
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
}