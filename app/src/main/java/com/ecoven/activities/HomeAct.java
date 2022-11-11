package com.ecoven.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ecoven.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeAct extends AppCompatActivity {

    public static NavController navController;
    private Bundle intent;
    LinearLayout llProfile, llCodes, llProducts, llMas, llGreen;
    private static final int MY_PERMISSION_CONSTANT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        llProfile = findViewById(R.id.llProfile);
        llCodes = findViewById(R.id.llCodes);
        llGreen = findViewById(R.id.llGreen);
        llProducts = findViewById(R.id.llProducts);
        llMas = findViewById(R.id.llMas);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_codes, R.id.navigation_green_footprint, R.id.navigation_products, R.id.navigation_maquinas)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        intent = getIntent().getExtras();
        setAllClick();
    }

    public void setAllClick() {

        llCodes.setOnClickListener(v ->
                {
                    if (checkPermisssionForReadStorage()) {
                        navController.navigate(R.id.navigation_codes);
                    }
                }
          );

        llProfile.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_home);
                }
        );

        llGreen.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_green_footprint);
                }
        );
        llProducts.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_products);
                }
        );

        llMas.setOnClickListener(v ->
                {
                    navController.navigate(R.id.navigation_maquinas);
                }
          );

    }

    public boolean checkPermisssionForReadStorage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }

            return false;

        } else {
            return true;
        }
    }
}