package com.example.fooddeliveryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsFragment extends Fragment {

    private CircleImageView profileImageSettings;
    private EditText emailText, usernameText;
    private Button updateButton, logOutButton;

    private String image;
    private Uri imageUri;
    private boolean imageControl = false;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    private FirebaseUser user = auth.getCurrentUser();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        getUserData();

        profileImageSettings = (CircleImageView) view.findViewById(R.id.settingsProfilePic);
        emailText = (EditText) view.findViewById(R.id.settingsEmailText);
        usernameText = (EditText) view.findViewById(R.id.settingsUsernameText);
        updateButton = (Button) view.findViewById(R.id.updateButton);
        logOutButton = (Button) view.findViewById(R.id.logoutButton);


        profileImageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        super.startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImageSettings);
            imageControl = true;
        }
        else{
            imageControl = false;
        }
    }

    public void updateProfile(){
        String email = emailText.getText().toString();
        String username = usernameText.getText().toString();
        reference.child("Users").child(user.getUid()).child("email").setValue(email);
        reference.child("Users").child(user.getUid()).child("username").setValue(username);
        auth.getCurrentUser().updateEmail(email);
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
                                    Toast.makeText(getActivity(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "There is a problem!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            reference.child("Users").child(auth.getUid()).child("image").setValue(image);
        }
    }

    public void getUserData(){
        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = snapshot.child("email").getValue().toString();
                String username = snapshot.child("username").getValue().toString();
                image = snapshot.child("image").getValue().toString();
                emailText.setText(email);
                usernameText.setText(username);
                if(image.equals("null")){
                    profileImageSettings.setImageResource(R.drawable.profile_pic);
                }
                else{
                    Picasso.get().load(image).into(profileImageSettings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}