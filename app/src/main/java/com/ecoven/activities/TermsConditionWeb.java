package com.ecoven.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ecoven.R;
import com.ecoven.databinding.ActivityTermsConditionWebBinding;

public class TermsConditionWeb extends AppCompatActivity {

    ActivityTermsConditionWebBinding binding;
    boolean loadingFinished = true;
    boolean redirect = false;
    private String description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_terms_condition_web);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {

            description= "";
        } else {
            description= extras.getString("description");
        }

        SetupUI();

    }

     private void SetupUI() {

        binding.loaderPage.showProgressBar();
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webview.setWebViewClient(new HelloWebViewClient());
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.getSettings().setAppCacheEnabled(true);
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            //binding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

          binding.webview.loadData(description, "text/html; charset=utf-8", "UTF-8");

          binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                binding.loaderPage.showProgressBar();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT IS NT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    binding.loaderPage.hideProgressBar();
                    //HIDE LOADING IT HAS FINISHED
                } else {
                    redirect = false;
                }

            }
        });

        binding.imgHeader.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}