package com.example.myanmarcuisinerecipe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    Toolbar mToolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleImageView profileHeaderImage;
    ProgressBar progressBarHorizontal,progressBarCicle;
    TextView username;
    WebView webView;
//    RelativeLayout relativeLayout;
//    Button btnRetryCOnnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolBar= findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Myanmar Cuisine Recipe");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        relativeLayout =findViewById(R.id.Connection_layout);
//        btnRetryCOnnection=findViewById(R.id.btnRetry);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("User");

        checkUserExistance();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View v = navigationView.inflateHeaderView(R.layout.drawer_header);
        profileHeaderImage = v.findViewById(R.id.profile_image_haeder);
        username = v.findViewById(R.id.profile_username_header);


        //prgress
        progressBarCicle=findViewById(R.id.progressBarCicle);
        progressBarHorizontal=findViewById(R.id.progressBarHorizonta);




        webView=findViewById(R.id.webView);
        webView.loadUrl("https://myanmar-cuisine-recipe.blogspot.com/");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarHorizontal.setVisibility(View.VISIBLE);
                progressBarHorizontal.setProgress(newProgress);
                if (newProgress>60)
                {
                    progressBarCicle.setVisibility(View.GONE);
                }
                if (newProgress==100)
                {
                    progressBarHorizontal.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home:

                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        break;
                    case R.id.NonVegetarian:
                        startActivity(new Intent(HomeActivity.this, NonVegetarianActivity.class));
                        break;
                    case R.id.Soup:
                        startActivity(new Intent(HomeActivity.this, NonVegetarianActivity.class));
                        break;

                    case R.id.Vegetarian:
                        startActivity(new Intent(HomeActivity.this, VegetarianActivity.class));
                        break;

                    case R.id.checken:
                        startActivity(new Intent(HomeActivity.this, ChickenActivity.class));
                        break;

                    case R.id.Curry:
                        startActivity(new Intent(HomeActivity.this, CurryActivity.class));
                        break;

                    case R.id.Egg:
                        startActivity(new Intent(HomeActivity.this, EggActivity.class));
                        break;

                    case R.id.Fish:
                        startActivity(new Intent(HomeActivity.this, FishActivity.class));
                        break;
                    case R.id.Rice:
                        startActivity(new Intent(HomeActivity.this, RiceActivity.class));
                        break;
                    case R.id.Noodles:
                        startActivity(new Intent(HomeActivity.this, RiceActivity.class));
                        break;
                    case R.id.Seafood:
                        startActivity(new Intent(HomeActivity.this, SeafoodActivity.class));
                        break;
                    case R.id.Snack:
                        startActivity(new Intent(HomeActivity.this, SnackActivity.class));
                        break;
                    case R.id.Drinks:
                        startActivity(new Intent(HomeActivity.this, DrinksActivity.class));
                        break;
                    case R.id.SeasonalFood:
                        startActivity(new Intent(HomeActivity.this, SeasonalFoodActivity.class));
                        break;

                    case R.id.logout:
                        mAuth.signOut();
                         sendUserToLoginActivity();
                        break;
                }
                return true;
            }
        });



    }

//    private void checkInternetConnection() {
//        ConnectivityManager manager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo net=manager.getNetworkInfo()
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId()==R.id.forward)
        {

            if (webView.canGoForward())
            {
                webView.goForward();
                return true;
            }

        }
        if (item.getItemId()==R.id.backward)
        {

            if (webView.canGoBack())
            {
                webView.goBack();
                return true;
            }

        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.main_menu_app_bar,menu);
       return true;
    }


    private void sendUserToLoginActivity() {
        Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void checkUserExistance() {
        if (mUser==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild("profileImage"))
                    {
                        sendUserToSetupActivity();
                    }
                    else
                    {
                        Picasso.get().load(dataSnapshot.child("profileImage").getValue().toString()).
                                placeholder(R.drawable.loader).into(profileHeaderImage);
                        username.setText(dataSnapshot.child("username").getValue().toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(HomeActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void sendUserToSetupActivity() {
        Intent intent=new Intent(HomeActivity.this,SetupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}