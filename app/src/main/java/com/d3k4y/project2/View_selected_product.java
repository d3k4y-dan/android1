package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.d3k4y.project2.Holder.ReviewHolder;
import com.d3k4y.project2.Holder.postHolder;
import com.d3k4y.project2.Models.PostModel;
import com.d3k4y.project2.Models.ProductModel;
import com.d3k4y.project2.Models.ReviewModel;
import com.d3k4y.project2.Models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class View_selected_product extends AppCompatActivity {

    TextView v2_seller_mail,v2_product_id,v2_product_name,v2_product_brand,v2_product_category,v2_product_gender,v2_product_price,v2_product_quantity,v2_product_description;
    ImageView v2_product_pic;
    Button buy_merch;
    ImageButton send_review;
    EditText b_review;
    RecyclerView load_review_view;

    private FirestoreRecyclerAdapter<ReviewModel, ReviewHolder> fsFirestoreRecyclerAdapter;

    ProductModel pm;

    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_product);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        v2_seller_mail=findViewById(R.id.v2_seller_mail);
        v2_product_id=findViewById(R.id.v2_product_id);
        v2_product_name=findViewById(R.id.v2_product_name);
        v2_product_brand=findViewById(R.id.v2_product_brand);
        v2_product_category=findViewById(R.id.v2_product_category);
        v2_product_gender=findViewById(R.id.v2_product_gender);
        v2_product_price=findViewById(R.id.v2_product_price);
        v2_product_quantity=findViewById(R.id.v2_product_quantity);
        v2_product_description=findViewById(R.id.v2_product_description);
        v2_product_pic=findViewById(R.id.v2_product_pic);
        buy_merch=findViewById(R.id.buy_merch);

        load_review_view=(RecyclerView) findViewById(R.id.load_review_view);
        load_review_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        b_review=findViewById(R.id.b_review);
        send_review=findViewById(R.id.send_review);
        send_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String review=b_review.getText().toString();
                    String email=user.getEmail();
                    String product_id=v2_product_id.getText().toString();

                    ReviewModel rm=new ReviewModel(email,review,product_id);
                    db.collection("ReviewData").add(rm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            b_review.setText("");
                            Toast.makeText(getApplicationContext(),
                                    "Review added",
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
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Bundle b=getIntent().getExtras();
        String productid=b.getString("v_product_id");

        buy_merch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(View_selected_product.this,buy_product.class);
                i.putExtra("v2_seller_mail",v2_seller_mail.getText().toString());
                i.putExtra("v2_product_name",v2_product_name.getText().toString());
                i.putExtra("v2_product_id",v2_product_id.getText().toString());
                i.putExtra("v2_product_price",v2_product_price.getText().toString());
                i.putExtra("v2_product_quantity",v2_product_quantity.getText().toString());
                startActivity(i);
                finish();
            }
        });

        db.collection("ProductData").whereEqualTo(FieldPath.documentId(),productid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ProductModel> list=queryDocumentSnapshots.toObjects(ProductModel.class);
                if(list.size()>0) {
                    for (int i = 0; i < list.size(); i++) {

                        pm=list.get(i);

                        v2_seller_mail.setText(pm.getEmail());
                        v2_product_id.setText(productid);
                        v2_product_name.setText(pm.getName());
                        v2_product_brand.setText(pm.getBrand());
                        v2_product_category.setText(pm.getCategory());
                        v2_product_gender.setText(pm.getGender());
                        v2_product_price.setText(pm.getPrice());
                        v2_product_quantity.setText(pm.getQty());
                        v2_product_description.setText(pm.getDescription());

                        storageRef.child(pm.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(View_selected_product.this).load(uri).into(v2_product_pic);
                            }
                        });

                    }
                }
            }
        });

        Query loadPosts= db.collection("ReviewData").whereEqualTo("product_id",productid);
        FirestoreRecyclerOptions recyclerOptions = new FirestoreRecyclerOptions.Builder<ReviewModel>().setQuery(loadPosts,ReviewModel.class).build();

        fsFirestoreRecyclerAdapter=new FirestoreRecyclerAdapter<ReviewModel, ReviewHolder>(recyclerOptions){

            @NonNull
            @Override
            public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_review,parent,false);
                return new ReviewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReviewHolder holder, int position, @NonNull ReviewModel model) {
                holder.lr_mail.setText("E-mail :- "+model.getEmail());
                holder.lr_body.setText("Review :- "+model.getReview());
            }
        };

        load_review_view.setAdapter(fsFirestoreRecyclerAdapter);

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