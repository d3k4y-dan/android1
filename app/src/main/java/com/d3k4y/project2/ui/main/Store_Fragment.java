package com.d3k4y.project2.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.d3k4y.project2.History;
import com.d3k4y.project2.R;
import com.d3k4y.project2.Search_fragment;
import com.d3k4y.project2.View_my_products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Store_Fragment extends Fragment {

    Spinner category;
    Button my_products;
    ImageView search_button,history;
    EditText search;

    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    String category1,search1;
    FragmentTransaction ft;
    Search_fragment sf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v= inflater.inflate(R.layout.store_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        category=v.findViewById(R.id.category_search);
        my_products=v.findViewById(R.id.my_products);
        search_button=v.findViewById(R.id.search_button);
        history=v.findViewById(R.id.history);
        search=v.findViewById(R.id.search);

        String[] categories = {"Merch name","Merch brand","Merch gender","Merch category"};
        ArrayAdapter<String> category_adapt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        category_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(category_adapt);

        ft=getFragmentManager().beginTransaction();
        sf=new Search_fragment();

        Bundle args = new Bundle();
        args.putString("category", "t");
        args.putString("text", "t");
        sf.setArguments(args);

        ft.add(R.id.fragment,sf,"search");
        ft.commit();

        my_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), View_my_products.class);
                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), History.class);
                startActivity(i);
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                category1=category.getSelectedItem().toString();
                search1=search.getText().toString();

                if(!search1.isEmpty()){
                    ft=getFragmentManager().beginTransaction();
                    sf=new Search_fragment();

                    Bundle args = new Bundle();
                    args.putString("category", category1);
                    args.putString("text", search1);
                    sf.setArguments(args);

                    ft.replace(R.id.fragment,sf,"search");
                    ft.commit();
                }else{
                    Toast.makeText(getContext(),
                            "Enter search key words",
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        return v;

    }

}