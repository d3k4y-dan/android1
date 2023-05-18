package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_password extends AppCompatActivity {

    EditText f_email;
    Button f_reset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        f_email=findViewById(R.id.f_email);
        f_reset=findViewById(R.id.f_reset);

        auth=FirebaseAuth.getInstance();

        f_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = f_email.getText().toString();

                auth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),
                                "Reset mail sent to " + emailAddress,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage().toString(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });

        f_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String p = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern=Pattern.compile(p);
                String email=f_email.getText().toString();
                Matcher matcher=pattern.matcher(email);

                if(matcher.matches()){
                    f_reset.setClickable(true);
                    f_email.setBackgroundResource(R.drawable.edittext_1);
                    f_email.setPadding(10,10,10,10);
                }else{
                    f_reset.setClickable(false);
                    f_email.setBackgroundResource(R.drawable.edittext_error);
                    f_email.setPadding(10,10,10,10);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}