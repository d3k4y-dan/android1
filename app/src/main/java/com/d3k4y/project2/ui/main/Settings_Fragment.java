package com.d3k4y.project2.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.d3k4y.project2.MainActivity;
import com.d3k4y.project2.R;
import com.d3k4y.project2.Ticket;
import com.d3k4y.project2.change_password;
import com.d3k4y.project2.delete_acc;
import com.d3k4y.project2.edit_profile;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Settings_Fragment extends Fragment {

    ImageButton logout;

    FirebaseAuth firebaseAuth;

    ConstraintLayout editprofile,ticket,changpw,deleteacc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.settings_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        logout=v.findViewById(R.id.imageButton3);
        editprofile=v.findViewById(R.id.constraintLayout2);
        ticket=v.findViewById(R.id.constraintLayout6);
        changpw=v.findViewById(R.id.constraintLayout4);
        deleteacc=v.findViewById(R.id.constraintLayout5);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START auth_fui_signout]
                firebaseAuth.signOut();
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                //sends the user back to login page;
                                Intent i=new Intent(getContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                getActivity().finish();
                            }
                        });

                // [END auth_fui_signout]
            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), edit_profile.class);
                startActivity(i);
            }
        });

        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), delete_acc.class);
                startActivity(i);
            }
        });

        changpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), change_password.class);
                startActivity(i);
            }
        });

        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), Ticket.class);
                startActivity(i);
            }
        });

        return v;

    }

}
