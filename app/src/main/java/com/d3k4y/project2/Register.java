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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.d3k4y.project2.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Register extends AppCompatActivity {

    ImageView imageView;
    ImageButton select_image;
    Button register;
    EditText nickname,birthday,address,mobile;
    Spinner gender1;

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
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        gender1=findViewById(R.id.spinner1);
        nickname=findViewById(R.id.nickname1);
        birthday=findViewById(R.id.birthday1);
        address=findViewById(R.id.address1);
        mobile=findViewById(R.id.mobile1);
        imageView=findViewById(R.id.imageView2);
        select_image=findViewById(R.id.imageButton2);
        register=findViewById(R.id.register);

        String[] gender = {"Male", "Female"};
        ArrayAdapter<String> gend_adapt = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, gender);
        gend_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender1.setAdapter(gend_adapt);

        Bundle bundle=getIntent().getExtras();
        String name=bundle.getString("auth_name");
        String email=bundle.getString("auth_mail");
        String password=bundle.getString("auth_pw");

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String m=mobile.getText().toString();
                if (m.length() == 10) {
                    register.setClickable(true);
                    mobile.setBackgroundResource(R.drawable.edittext_1);
                    mobile.setPadding(10,10,10,10);
                }else{
                    register.setClickable(false);
                    mobile.setBackgroundResource(R.drawable.edittext_error);
                    mobile.setPadding(10,10,10,10);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String nickname1=nickname.getText().toString();
                    String birthday1=birthday.getText().toString();
                    String mobile1=mobile.getText().toString();
                    String address1=address.getText().toString();
                    String gen=gender1.getSelectedItem().toString();

                    String imgPath="UserProfileImages/User_"+email+".png";

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                if(!user.isEmailVerified()){
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Verification mail sent to "+email,
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }

                                String uid=user.getUid();
                                User u=new User(name,nickname1,email,password,mobile1,address1,birthday1,gen,imgPath,uid,0);
                                storageRef.child(imgPath).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        db.collection("UserData").add(u).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Registration Success",
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(u.getName())
                                                        .setPhotoUri(selectedImage)
                                                        .build();
                                                user.updateProfile(profileUpdates);
                                                firebaseAuth.signOut();
                                                AuthUI.getInstance()
                                                        .signOut(Register.this)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                // ...
                                                                //sends the user back to login page;
                                                                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(i);
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this,
                                                        e.getMessage().toString(),
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Register.this,
                                                e.getMessage().toString(),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            }else{
                                Toast.makeText(Register.this,
                                        task.getException().toString(),
                                        Toast.LENGTH_SHORT)
                                        .show();
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

    @Override
    public void onActivityResult(int requestCode2, int resultCode, Intent data) {
        super.onActivityResult(requestCode2, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {

            switch (requestCode2) {
                case 0:
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        selectedImage = data.getData();
                        Picasso.with(getApplicationContext()).load(selectedImage).fit().into(imageView);
                    }

                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Register.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            b=false;
            // Requesting the permission
            ActivityCompat.requestPermissions(Register.this,
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
                Toast.makeText(Register.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(Register.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                b=true;
                Toast.makeText(Register.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(Register.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}