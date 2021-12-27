package com.example.myanmarcuisinerecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profileImageView;
    TextView username,email;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mToolBar= findViewById(R.id.include2);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        profileImageView=findViewById(R.id.ProfileImageView);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("User");

        mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("username"))
                {
                    username.setText(dataSnapshot.child("username").getValue().toString());
                    Picasso.get().load(dataSnapshot.child("profileImage").getValue().toString()).placeholder(R.drawable.loader).into(profileImageView);
                    email.setText(mUser.getEmail());
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Data no found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}