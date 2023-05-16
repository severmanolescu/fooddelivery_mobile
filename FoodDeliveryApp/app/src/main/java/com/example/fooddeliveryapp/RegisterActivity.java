package com.example.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private EditText emailText, usernameText, passwordText;
    private Button registerButton;

    private Uri imageUri;
    private boolean imageControl = false;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profilePic = findViewById(R.id.settingsProfilePic);
        emailText = findViewById(R.id.settingsEmailText);
        usernameText = findViewById(R.id.settingsUsernameText);
        passwordText = findViewById(R.id.settingsPasswordText);
        registerButton = findViewById(R.id.updateButton);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                String pass = passwordText.getText().toString();
                String username = usernameText.getText().toString();

                if(!email.equals("") && !pass.equals("") && !username.equals("")){
                    validateEmail(email);
                    register(email, pass, username);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please enter your data first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateEmail(String email){
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else{
            Toast.makeText(RegisterActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profilePic);
            imageControl = true;
        }
        else{
            imageControl = false;
        }
    }

    public void register(String email, String password, String username){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference.child("Users").child(auth.getUid()).child("username").setValue(username);
                    reference.child("Users").child(auth.getUid()).child("email").setValue(email);
                    if(imageControl){
                        UUID randomID = UUID.randomUUID();
                        String imageName = "images/"+randomID+".jpg";
                        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                                myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();
                                        reference.child("Users").child(auth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "There is a problem!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else{
                        reference.child("Users").child(auth.getUid()).child("image").setValue("null");
                    }

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Cannot create the account!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}