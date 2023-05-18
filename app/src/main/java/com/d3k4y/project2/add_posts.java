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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.d3k4y.project2.Models.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class add_posts extends AppCompatActivity {

    Button pp,ap;
    ImageView p;
    EditText pt,pd;
    String email;

    FirebaseUser user;
    Boolean b=false;
    Uri selectedImage;

    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_posts);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        p=findViewById(R.id.post_pic);
        pt=findViewById(R.id.post_title);
        pd=findViewById(R.id.post_description);

        email=user.getEmail();

        ap=findViewById(R.id.add_post);
        ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String post_title=pt.getText().toString();
                    String post_description=pd.getText().toString();
                    Date d=new Date();
                    DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String date=df.format(d);
                    String image="PostImages/Postby"+email+date+".png";

                    storageRef.child(image).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            PostModel pm=new PostModel(post_title,post_description,image,email,date);
                            db.collection("Posts").add(pm).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(add_posts.this,
                                            "Post upload successful!!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(add_posts.this,
                                            e.getMessage().toString(),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_posts.this,
                                    e.getMessage().toString(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(add_posts.this,
                            e.getMessage().toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        pp=findViewById(R.id.add_post_pic);
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(add_posts.this);
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
                        Picasso.with(getApplicationContext()).load(selectedImage).into(p);
                    }

                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(add_posts.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            b=false;
            // Requesting the permission
            ActivityCompat.requestPermissions(add_posts.this,
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
                Toast.makeText(add_posts.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(add_posts.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                b=true;
                Toast.makeText(add_posts.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(add_posts.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}