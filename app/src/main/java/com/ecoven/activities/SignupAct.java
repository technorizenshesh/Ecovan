package com.ecoven.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecoven.BuildConfig;
import com.ecoven.R;
import com.ecoven.databinding.ActivitySignupBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResGetCoutries;
import com.ecoven.retrofit.models.SuccessResGetProfile;
import com.ecoven.retrofit.models.SuccessResSignUp;
import com.ecoven.retrofit.models.TermsConditionModal;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.isValidEmail;
import static com.ecoven.retrofit.Constant.showToast;

public class SignupAct extends AppCompatActivity {

    ActivitySignupBinding binding;
    private EcoVanInterface apiInterface;

    public static final int PICK_IMAGE = 1;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;

    private String strLat = "", strLon = "",
            strName = "", strEmail = "", strNumber = "",
            strCountryCode = "",
            strPassword = "",
            strConfirmPassword = "";

    private String strSelectedCountry = "";

    private ArrayList<SuccessResGetCoutries.Result> countriesList = new ArrayList<>();
    private TermsConditionModal.Result TermsDetails;
    private String get_description = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);

        GetTermsCondition();

        binding.checkBoxTerms.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsConditionWeb.class).putExtra("description", get_description));
        });


        binding.ivCamera.setOnClickListener(v -> {
            if (checkPermisssionForReadStorage())
                showImageSelection();
        });

        binding.header.imgHeader.setOnClickListener(v -> finish());
        binding.header.tvHeader.setText(R.string.creaate_acc);

        binding.tvAlreadyHave.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupAct.this, LoginAct.class)
                    );
                }
        );

        binding.btnRegister.setOnClickListener(v ->

                {
                    strName = binding.etName.getText().toString().trim();
                    strEmail = binding.etEmail.getText().toString().trim();
                    strNumber = binding.etPhone.getText().toString().trim();
                    strPassword = binding.etPassword.getText().toString().trim();
                    strConfirmPassword = binding.etConfirmPassword.getText().toString().trim();
                    strCountryCode = binding.ccp.getSelectedCountryCode();
                    strSelectedCountry = getCountryCode(binding.spinnerCountry.getSelectedItem().toString());

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                            signup();

                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();

                    }

                }
        );

        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

            getCountries();

        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private void GetTermsCondition() {

        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);
        Log.e("userId>>>", userId);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<TermsConditionModal> call = apiInterface.get_privacy_policy(map);

        call.enqueue(new Callback<TermsConditionModal>() {
            @Override
            public void onResponse(Call<TermsConditionModal> call, Response<TermsConditionModal> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    TermsConditionModal data = response.body();
                    Log.e("data", data.status);

                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "Terms" + dataResponse);

                        TermsDetails = data.getResult();
                        get_description = TermsDetails.getDescription();

                    } else if (data.status.equals("0")) {
                        showToast(SignupAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TermsConditionModal> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(SignupAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(SignupAct.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SignupAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupAct.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(SignupAct.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SignupAct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {

                ActivityCompat.requestPermissions(SignupAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CONSTANT);

            } else {
                ActivityCompat.requestPermissions(SignupAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSION_CONSTANT: {

                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(SignupAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                // return;
            }

        }
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(SignupAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });

        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Micasa/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Micasa/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(SignupAct.this,
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);

    }

    public void signup() {

        DataManager.getInstance().showProgressMessage(SignupAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), strName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), strPassword);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), strNumber);
        RequestBody selectedCountry = RequestBody.create(MediaType.parse("text/plain"), strSelectedCountry);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), strLat);
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), strLon);
        RequestBody cc = RequestBody.create(MediaType.parse("text/plain"), strCountryCode);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"), "");

        Call<SuccessResSignUp> signupCall = apiInterface.signUp(name, email, password, mobile, cc, selectedCountry, registerID, lat, lng, filePart);
        signupCall.enqueue(new Callback<SuccessResSignUp>() {
            @Override
            public void onResponse(Call<SuccessResSignUp> call, Response<SuccessResSignUp> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignUp data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        startActivity(new Intent(SignupAct.this, LoginAct.class));
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(SignupAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignUp> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(SignupAct.this, data.getData());
                Glide.with(SignupAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfileImage);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(SignupAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfileImage);
            }
        }
    }

    private boolean isValid() {

        if (strName.equalsIgnoreCase("")) {
            binding.etName.setError(getString(R.string.enter_name));
            return false;

        } else if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;

        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;

        } else if (strNumber.equalsIgnoreCase("")) {
            binding.etPhone.setError(getString(R.string.enter_mobile_number));
            return false;

        } else if (strSelectedCountry.equalsIgnoreCase("")) {
            Toast.makeText(SignupAct.this, "" + getString(R.string.select_country), Toast.LENGTH_SHORT).show();
            return false;

        } else if (strPassword.equalsIgnoreCase("")) {
            binding.etPassword.setError(getString(R.string.please_enter_pass));
            return false;

        } else if (strConfirmPassword.equalsIgnoreCase("")) {
            binding.etConfirmPassword.setError(getString(R.string.please_enter_confirm_pass));
            return false;

        } else if (!strPassword.equalsIgnoreCase(strConfirmPassword)) {
            binding.etConfirmPassword.setError(getString(R.string.pass_and_confirm_pass_not_macthed));
            return false;

        } else if (!binding.checkbox.isChecked()) {
            Toast.makeText(this, "Please Select Check Box ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void getCountries() {

        DataManager.getInstance().showProgressMessage(SignupAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetCoutries> call = apiInterface.getCountries(map);
        call.enqueue(new Callback<SuccessResGetCoutries>() {
            @Override
            public void onResponse(Call<SuccessResGetCoutries> call, Response<SuccessResGetCoutries> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCoutries data = response.body();
                    if (data.status.equals("1")) {

                        countriesList.addAll(data.getResult());
                        initializeUI();

                    } else {
                        showToast(SignupAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCoutries> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private ArrayAdapter<String> countryArrayAdapter;


    private void initializeUI() {

        ArrayList<String> spinnerListCountry = new ArrayList<>();
        spinnerListCountry.add(getString(R.string.select_country));

        for (SuccessResGetCoutries.Result country : countriesList) {
            spinnerListCountry.add(country.getName());
        }
        countryArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerListCountry);
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(countryArrayAdapter);

    }

    public String getCountryCode(String strCountryName) {
        for (SuccessResGetCoutries.Result country : countriesList) {
            if (country.getName().equalsIgnoreCase(strCountryName)) {
                return country.getId();
            }
        }
        return "";
    }
}