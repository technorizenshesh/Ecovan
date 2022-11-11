package com.ecoven.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.retrofit.Constant;
import com.ecoven.utility.SharedPreferenceUtility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashAct extends AppCompatActivity {

    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        getLocation();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ecoven",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashAct.this).getBoolean(Constant.IS_USER_LOGGED_IN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {
                    startActivity(new Intent(SplashAct.this, HomeAct.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAct.this, SelectLangActivity.class).putExtra("from","splash"));
                    finish();
                }
            }
        },3000);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SplashAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  
                } else {
                    Toast.makeText(SplashAct.this, SplashAct.this.getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
    
}