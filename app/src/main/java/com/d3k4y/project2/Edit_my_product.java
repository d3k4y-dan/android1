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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.d3k4y.project2.Models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Edit_my_product extends AppCompatActivity {

    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    Uri selectedImage;
    Boolean b;
    ProductModel pm;

    TextView edit_product_id,edit_product_name,edit_product_brand,edit_product_category,edit_product_gender;
    EditText edit_product_price,edit_product_qty,edit_product_description;
    Button edit_product,delete_product;
    ImageButton edit_product_pic;
    ImageView edit_product_pic_view;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_product);

        edit_product_id=findViewById(R.id.edit_product_id);
        edit_product_name=findViewById(R.id.edit_product_name);
        edit_product_brand=findViewById(R.id.edit_product_brand);
        edit_product_category=findViewById(R.id.edit_product_category);
        edit_product_gender=findViewById(R.id.edit_product_gender);

        edit_product_price=findViewById(R.id.edit_product_price);
        edit_product_qty=findViewById(R.id.edit_product_qty);
        edit_product_description=findViewById(R.id.edit_product_description);

        edit_product=findViewById(R.id.edit_product);
        delete_product=findViewById(R.id.delete_product);

        edit_product_pic=findViewById(R.id.edit_product_pic);
        edit_product_pic_view=findViewById(R.id.edit_product_pic_view);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Bundle bundle=getIntent().getExtras();
        String product_id=bundle.getString("product_id");

        db.collection("ProductData").whereEqualTo(FieldPath.documentId(),product_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ProductModel> list=queryDocumentSnapshots.toObjects(ProductModel.class);
                if(list.size()>0) {
                    for (int i = 0; i < list.size(); i++) {

                        pm = list.get(i);

                        edit_product_id.setText(product_id);
                        edit_product_name.setText(pm.getName());
                        edit_product_brand.setText(pm.getBrand());
                        edit_product_category.setText(pm.getCategory());
                        edit_product_gender.setText(pm.getGender());

                        edit_product_price.setText(pm.getPrice());
                        edit_product_qty.setText(pm.getQty());
                        edit_product_description.setText(pm.getDescription());

                        storageRef.child(pm.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getApplicationContext()).load(uri).fit().into(edit_product_pic_view);
                            }
                        });

                    }
                }
            }
        });

        delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    storageRef.child(pm.getImage()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("ProductData").document(product_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),
                                            "Product deleted",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    e.getMessage().toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
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

        edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    pm.setPrice(edit_product_price.getText().toString());
                    pm.setQty(edit_product_qty.getText().toString());
                    pm.setDescription(edit_product_description.getText().toString());

                    if(selectedImage!=null){
                        storageRef.child(pm.getImage()).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                db.collection("ProductData").document(product_id).set(pm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),
                                                "Product edited",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        finish();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        e.getMessage().toString(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }else{
                        db.collection("ProductData").document(product_id).set(pm).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),
                                        "Product edited",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }
                        });
                    }

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });

        edit_product_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_my_product.this);
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
                        Picasso.with(getApplicationContext()).load(selectedImage).fit().into(edit_product_pic_view);
                    }

                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Edit_my_product.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            b=false;
            // Requesting the permission
            ActivityCompat.requestPermissions(Edit_my_product.this,
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
                Toast.makeText(Edit_my_product.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(Edit_my_product.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                b=true;
                Toast.makeText(Edit_my_product.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(Edit_my_product.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}