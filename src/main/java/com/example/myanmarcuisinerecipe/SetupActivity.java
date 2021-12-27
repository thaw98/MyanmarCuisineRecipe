package com.example.myanmarcuisinerecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    CircleImageView profileImage;
    EditText inputUsername;
    Button btAddSetup;


    public static final int IMAGE_REQUEST_CODE_POST=102;
    Uri uri=null;
    ProgressDialog mLoadingBar;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference mStorage;
    Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        profileImage=findViewById(R.id.setup_profile_image);
        inputUsername=findViewById(R.id.inputUsername);
        btAddSetup=findViewById(R.id.btnAddSetup);

        mToolBar = findViewById(R.id.app_bar_main);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Setup Your Profile");



        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mRef= FirebaseDatabase.getInstance().getReference().child("User");
        mStorage= FirebaseStorage.getInstance().getReference().child("User");
        mLoadingBar=new ProgressDialog(this);

        checkUserExistance();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMAGE_REQUEST_CODE_POST);
            }
        });
        btAddSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptSetup();
            }
        });

    }
    private void checkUserExistance() {
        if (mUser!=null )
        {
            mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("profileImage"))
                    {
                        sendUserToMainActivity();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(SetupActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void AtemptSetup() {
        final String username = inputUsername.getText().toString();
        if (username.isEmpty() || username.length()<4) {
            showError(inputUsername, "Username must be greater that 3 latter");
        }else if (uri==null ) {
            Toast.makeText(this, "Please Select Profile Image", Toast.LENGTH_SHORT).show();
        }else {
            mLoadingBar.setTitle("Setup You Profile");
            mLoadingBar.setMessage("Please wait,While Saving your data...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mStorage.child(mUser.getUid()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        mStorage.child(mUser.getUid()).getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap hashMap=new HashMap();
                                        hashMap.put("profileImage",uri.toString());
                                        hashMap.put("username",username);

                                        mRef.child(mUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    sendUserToMainActivity();
                                                    mLoadingBar.dismiss();
                                                }
                                                else {
                                                    mLoadingBar.dismiss();
                                                    Toast.makeText(SetupActivity.this,"Something going Wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SetupActivity.this, "Try Again"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mLoadingBar.dismiss();
                        Toast.makeText(SetupActivity.this,"Something going Wrong Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToMainActivity() {
        Intent intent=new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST_CODE_POST && resultCode==RESULT_OK && data!=null)
        {
            uri=data.getData();
            profileImage.setImageURI(uri);
        }
    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}