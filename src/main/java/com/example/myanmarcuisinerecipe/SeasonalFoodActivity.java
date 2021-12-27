package com.example.myanmarcuisinerecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class SeasonalFoodActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBarCicle;
    Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasonal_food);
        mToolBar= findViewById(R.id.appBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Seasonal Food ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBarCicle=findViewById(R.id.progressBarCicle);

        webView=findViewById(R.id.webView);
        webView.loadUrl("https://myanmar-cuisine-recipe.blogspot.com/search/label/Seasonal/");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress>80)
                {
                    progressBarCicle.setVisibility(View.GONE);
                }

                super.onProgressChanged(view, newProgress);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

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