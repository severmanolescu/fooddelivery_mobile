package com.example.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailText;
    private Button resetPassButton;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailText = findViewById(R.id.emailResetText);
        resetPassButton = findViewById(R.id.resetPassButton);

        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                if(!email.equals("")){
                    validateEmail(email);
                    resetPassword(email);
                }
                else{
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your email first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateEmail(String email){
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else{
            Toast.makeText(ResetPasswordActivity.this, "Invalid Email!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void resetPassword(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Please check your email!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ResetPasswordActivity.this, "No account with this email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}