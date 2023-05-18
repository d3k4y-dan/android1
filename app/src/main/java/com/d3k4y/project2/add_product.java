package com.d3k4y.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.d3k4y.project2.Models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class add_product extends AppCompatActivity {

    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Boolean b=false;
    Uri selectedImage;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    Spinner gender1;
    EditText product_name,product_brand,product_description,product_category,product_price,product_qty;
    Button add_product_pic,add_product;
    ImageView product_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        gender1=findViewById(R.id.product_gender);
        product_pic=findViewById(R.id.product_pic);
        product_name=findViewById(R.id.product_name);
        product_brand=findViewById(R.id.product_brand);
        product_description=findViewById(R.id.product_description);
        product_category=findViewById(R.id.product_category);
        product_price=findViewById(R.id.product_price);
        product_qty=findViewById(R.id.product_qty);
        add_product=findViewById(R.id.add_product);
        add_product_pic=findViewById(R.id.add_product_pic);

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    String name=product_name.getText().toString();
                    String brand=product_brand.getText().toString();
                    String description=product_description.getText().toString();
                    String gender=gender1.getSelectedItem().toString();
                    String category=product_category.getText().toString();
                    String price=product_price.getText().toString();
                    String qty=product_qty.getText().toString();
                    String email=user.getEmail();
                    String image="ProductImages/Product_"+email+"_"+name+"_"+brand+".png";

                    ProductModel pm=new ProductModel(name,brand,description,gender,category,price,qty,image,email);

                    storageRef.child(image).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            db.collection("ProductData").add(pm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(add_product.this,
                                            "Product Added",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(add_product.this,
                                            e.getMessage().toString(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_product.this,
                                    e.getMessage().toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(add_product.this,
                            e.getMessage().toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        add_product_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(add_product.this);
                builder.setTitle("Choose your profile picture");
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {

                            checkPermission(Manifest.permission.CAMERA,
                                    CAMERA_PERMISSION_CODE);

                            if(b){
                                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                            }

                        } else if (options[item].equals("Choose from Gallery")) {

                            checkPermission(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    STORAGE_PERMISSION_CODE);

                            if(b){
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                            }

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        String[] gender = {"Male", "Female","Unisex"};
        ArrayAdapter<String> gend_adapt = new ArrayAdapter<String>(add_product.this, android.R.layout.simple_spinner_dropdown_item, gender);
        gend_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender1.setAdapter(gend_adapt);

    }

    @Override
    public void onActivityResult(int requestCode2, int resultCode, Intent data) {
        super.onActivityResult(requestCode2, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {

            switch (requestCode2) {
                case 0:
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        selectedImage = data.getData();
                        Picasso.with(getApplicationContext()).load(selectedImage).into(product_pic);
                    }

                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(add_product.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            b=false;
            // Requesting the permission
            ActivityCompat.requestPermissions(add_product.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            b=true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                b=true;
                Toast.makeText(add_product.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(add_product.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                b=true;
                Toast.makeText(add_product.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(add_product.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}