package com.ecoven.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecoven.BuildConfig;
import com.ecoven.R;
import com.ecoven.databinding.FragmentProfileBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResGetProfile;
import com.ecoven.retrofit.models.SuccessResUpdateProfile;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.isValidEmail;
import static com.ecoven.retrofit.Constant.showToast;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    private EcoVanInterface apiInterface;

    String strName = "", strEmail = "", strmobile = "", strCountryCode = "", strLat = "", strLng = "", strGreenFootprint = "";

    private SuccessResGetProfile.Result userDetails;

    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String cardNumber = "";

    public ProfileFragment() {

    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,
                container, false);

        apiInterface = ApiClient.getClient()
                .create(EcoVanInterface.class);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getProfile();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.header.tvHeader.setText(R.string.edit_profile);

        binding.header.imgHeader.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });


        binding.ivEditProfile.setOnClickListener(v -> {
            if (checkPermisssionForReadStorage())
                showImageSelection();
        });

        binding.btnSave.setOnClickListener(v ->
                {
                    strName = binding.nameEditText.getText().toString();
                    strEmail = binding.emailEditText.getText().toString();
                    strmobile = binding.etPhone.getText().toString();
                    strCountryCode = binding.ccp.getSelectedCountryCode();
                    cardNumber = binding.cardNumber.getText().toString();

                    if (binding.radioGreenFootprint.isChecked()) {
                        strGreenFootprint = "1";

                    } else {
                        strGreenFootprint = "0";
                    }

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
                            updateProfile();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "ON error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.tvChangePassword.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_changePassFragment);

                }
        );

        return binding.getRoot();
    }

    public void updateProfile() {

        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        MultipartBody.Part filePart;

        if (!str_image_path.equalsIgnoreCase("")) {

            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if (file != null) {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            } else {
                filePart = null;
            }

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), strName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), strmobile);
        RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"), strCountryCode);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), strLat);
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), strLng);
        RequestBody greenFootprint = RequestBody.create(MediaType.parse("text/plain"), strGreenFootprint);
        RequestBody card_number = RequestBody.create(MediaType.parse("text/plain"), cardNumber);

        Call<SuccessResUpdateProfile> call = apiInterface.updateProfile(userId, name, email, mobile,
                countryCode, lat, lng, greenFootprint, card_number, filePart);

        call.enqueue(new Callback<SuccessResUpdateProfile>() {
            @Override
            public void onResponse(Call<SuccessResUpdateProfile> call,
                                   Response<SuccessResUpdateProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResUpdateProfile data = response.body();
                    getProfile();

                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        getProfile();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
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

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Ecovan/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Ecovan/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider", tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfileImage);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfileImage);

            }
        }
    }

    public boolean checkPermisssionForReadStorage() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case MY_PERMISSION_CONSTANT: {


                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void getProfile() {
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Log.e("userId>>>", userId);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);

        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetProfile data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        userDetails = data.getResult();
                        setProfileDetails();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setProfileDetails() {

        Glide.with(getActivity())
                .load(userDetails.getImage())
                .centerCrop()
                .into(binding.ivProfileImage);

        binding.nameEditText.setText(userDetails.getName());
        binding.emailEditText.setText(userDetails.getEmail());
        binding.etPhone.setText(userDetails.getMobile());
        binding.cardNumber.setText(userDetails.getCard_number());
        binding.ccp.setCountryForPhoneCode(Integer.parseInt(userDetails.getCountryCode()));

        if (userDetails.getGreenFootprint().equalsIgnoreCase("1")) {
            binding.radioGreenFootprint.setChecked(true);
        } else {
            binding.radioGreenFootprint.setChecked(false);
        }
    }


    private boolean isValid() {
        if (strName.equalsIgnoreCase("")) {
            binding.nameEditText.setError(getString(R.string.enter_name));
            return false;
        } else if (strEmail.equalsIgnoreCase("")) {
            binding.emailEditText.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.emailEditText.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (strmobile.equalsIgnoreCase("")) {
            binding.etPhone.setError(getString(R.string.enter_mobile_number));
            return false;

        } else if (cardNumber.equalsIgnoreCase("")) {
            binding.cardNumber.setError(getString(R.string.enter_card_number));
            return false;
        }
        return true;
    }
}