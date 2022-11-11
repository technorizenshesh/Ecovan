package com.ecoven.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ecoven.R;
import com.ecoven.databinding.ActivitySelectLangBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.Constant;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.models.SuccessResForgetPassword;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.showToast;

public class SelectLangActivity extends AppCompatActivity {

    private ActivitySelectLangBinding binding;

    private String from = "";

    boolean homeAct = false;

    private EcoVanInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_select_lang);
        //setContentView(R.layout.activity_select_lang);

        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);

        boolean val =  SharedPreferenceUtility.getInstance(SelectLangActivity.this).getBoolean(Constant.SELECTED_LANGUAGE);

        from = getIntent().getExtras().getString("from");

        binding.ivBack.setOnClickListener(v -> finish());

        if(from.equalsIgnoreCase("splash"))
        {
            homeAct = false;
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.ivBack.setVisibility(View.GONE);
        }
        else
        {
            homeAct = false;
            binding.btnLogin.setVisibility(View.GONE);
            binding.ivBack.setVisibility(View.VISIBLE);
        }

        if(!val)
        {
            updateResources(SelectLangActivity.this,"en");
            binding.radio1.setChecked(true);
            binding.radio2.setChecked(false);

        }else
        {
            updateResources(SelectLangActivity.this,"es");
            binding.radio2.setChecked(true);
            binding.radio1.setChecked(false);
        }

        binding.radio1.setOnClickListener(v ->
                {
                    updateResources(SelectLangActivity.this,"en");
                    binding.radio2.setChecked(false);
                    SharedPreferenceUtility.getInstance(SelectLangActivity.this).putBoolean(Constant.SELECTED_LANGUAGE, false);
                    updateLanguage("en");
                }
        );
        binding.radio2.setOnClickListener(v ->
                {
                    updateResources(SelectLangActivity.this,"es");
                    binding.radio1.setChecked(false);
                    updateLanguage("es");
                    SharedPreferenceUtility.getInstance(SelectLangActivity.this).putBoolean(Constant.SELECTED_LANGUAGE, true);
                }
        );
        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this,LoginAct.class));
        });

    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    private void updateLanguage(String lang) {
        String userId = SharedPreferenceUtility.getInstance(SelectLangActivity.this).getString(USER_ID);
        Log.e("userId>>>", userId);
        DataManager.getInstance().showProgressMessage(SelectLangActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<SuccessResForgetPassword> call = apiInterface.updateLang(map);

        call.enqueue(new Callback<SuccessResForgetPassword>() {
            @Override
            public void onResponse(Call<SuccessResForgetPassword> call, Response<SuccessResForgetPassword> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResForgetPassword data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(SelectLangActivity.this, data.message);

                    } else if (data.status.equals("0")) {
                        showToast(SelectLangActivity.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResForgetPassword> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}