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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.d3k4y.project2.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class edit_profile extends AppCompatActivity {

    ImageView imageview;
    ImageButton imagebutton;

    User u;

    FirebaseUser user;
    FirebaseFirestore db;
    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;

    Boolean b;

    String name,nickname,mobile,address,imagee;
    EditText name1,nickname1,mobile1,address1;
    ImageView imagee1;
    Uri selectedImage;
    Button update;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imageview=findViewById(R.id.imageView);
        imagebutton=findViewById(R.id.imageButton);

        name1=findViewById(R.id.name2);
        nickname1=findViewById(R.id.nickname2);
        mobile1=findViewById(R.id.mobile2);
        address1=findViewById(R.id.address2);
        imagee1=findViewById(R.id.imageView);

        String email=user.getEmail();

        mobile1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String m=mobile1.getText().toString();
                if (m.length() == 10) {
                    update.setClickable(true);
                    mobile1.setBackgroundResource(R.drawable.edittext_1);
                    mobile1.setPadding(10,10,10,10);
                }else{
                    update.setClickable(false);
                    mobile1.setBackgroundResource(R.drawable.edittext_error);
                    mobile1.setPadding(10,10,10,10);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        db.collection("UserData").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<User> list=queryDocumentSnapshots.toObjects(User.class);
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){

                        u=list.get(i);

                        name=u.getName();
                        nickname=u.getNickname();
                        address=u.getAddress();
                        mobile=u.getMobile();
                        imagee=u.getImage();

                        name1.setText(name);
                        nickname1.setText(nickname);
                        address1.setText(address);
                        mobile1.setText(mobile);
                        storageRef.child(imagee).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getApplicationContext()).load(uri).fit().into(imageview);
                            }
                        });

                    }
                }
            }
        });

        update=findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    name=name1.getText().toString();
                    nickname=nickname1.getText().toString();
                    address=address1.getText().toString();
                    mobile=mobile1.getText().toString();

                    u.setName(name);
                    u.setNickname(nickname);
                    u.setAddress(address);
                    u.setMobile(mobile);

                    if(selectedImage!=null) {
                        storageRef.child(imagee).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                db.collection("UserData").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for(int i=0;i<task.getResult().size();i++){
                                            String id=task.getResult().getDocuments().get(i).getId();
                                            db.collection("UserData").document(id).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(u.getName())
                                                            .setPhotoUri(selectedImage)
                                                            .build();
                                                    user.updateProfile(profileUpdates);
                                                    Toast.makeText(getApplicationContext(),
                                                            "Profile updated",
                                                            Toast.LENGTH_SHORT)
                                                            .show();
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Snackbar.make(v.getRootView(), "Couldn't update profile", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        db.collection("UserData").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(int i=0;i<task.getResult().size();i++){
                                    String id=task.getResult().getDocuments().get(i).getId();
                                    db.collection("UserData").document(id).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(u.getName())
                                                    .build();
                                            user.updateProfile(profileUpdates);
                                            Toast.makeText(getApplicationContext(),
                                                    "Profile updated",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(v.getRootView(), "Couldn't update profile", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    });
                                }
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

        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(edit_profile.this);
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
                        Picasso.with(getApplicationContext()).load(selectedImage).fit().into(imagee1);
                    }

                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(edit_profile.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            b=false;
            // Requesting the permission
            ActivityCompat.requestPermissions(edit_profile.this,
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
                Toast.makeText(edit_profile.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(edit_profile.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                b=true;
                Toast.makeText(edit_profile.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                b=false;
                Toast.makeText(edit_profile.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}