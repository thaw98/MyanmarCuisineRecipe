package com.example.myanmarcuisinerecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    Toolbar mToolBar;
    Button btnConformPassword;
    EditText inputEmail;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mToolBar= findViewById(R.id.appBarForgotPassword);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnConformPassword=findViewById(R.id.btnVerification);
        inputEmail=findViewById(R.id.inputEmailConfirmPassword);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();


        btnConformPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptForgotPassword();
            }
        });


    }

    private void AtemptForgotPassword() {

        final String email = inputEmail.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputEmail, "Email is not Valid");
        }else {
          progressBar.setVisibility(View.VISIBLE);
           mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                   {
                       progressBar.setVisibility(View.GONE);
                       startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                       Toast.makeText(ForgotPasswordActivity.this, "Check Your Email & Update password!", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       progressBar.setVisibility(View.GONE);
                       Toast.makeText(ForgotPasswordActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                   }
               }
           });

        }
    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}