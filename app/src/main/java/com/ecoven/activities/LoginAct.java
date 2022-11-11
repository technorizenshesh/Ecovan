package com.ecoven.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.databinding.ActivityLoginBinding;
import com.ecoven.fragments.HomeFragment;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.Constant;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResLogin;
import com.ecoven.retrofit.models.SuccessResSocialLogin;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.isValidEmail;
import static com.ecoven.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {


    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;

    String strName = "", strMobile = "", strLat = "", strLng = "", strSocialId = "", deviceToken = "", strPhoneNumber = "";

    ActivityLoginBinding binding;

    String str_image_path = "";

    String strLanguage = "";

    private EcoVanInterface apiInterface;

    public static String TAG = "LoginActivity";

    private String strEmail = "", strPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);



        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        getToken();

        boolean val = SharedPreferenceUtility.getInstance(LoginAct.this).getBoolean(Constant.SELECTED_LANGUAGE);

        if (!val) {
            strLanguage = "en";
        } else {
            strLanguage = "es";
        }

        binding.tvSignup.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this, SignupAct.class));
                }
        );

        binding.tvForgotPass.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this, ForgotPassActivity.class));
                }
        );

        binding.btnLogin.setOnClickListener(v -> {

            strEmail = binding.etEmail.getText().toString().trim();
            strPassword = binding.etPassword.getText().toString().trim();

            if (isValid()) {

                if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                    login();

                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }

        });


        binding.btnFacebookSignin.setOnClickListener(v ->
                {

                    if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                        FacebookSdk.setApplicationId("270492588321608");
                        FacebookSdk.sdkInitialize(LoginAct.this);
                        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
                        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                Log.e("kjsgdfkjdgsf", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.e("kjsgdfkjdgsf", "error = " + error.getMessage());
                            }

                        });

                    } else {
                        Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                    }

                }
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void socialLogin() {

        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));

        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), strName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), strMobile);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), strLat);
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), strLng);
        RequestBody registerID = RequestBody.create(MediaType.parse("text/plain"), deviceToken);
        RequestBody socialId = RequestBody.create(MediaType.parse("text/plain"), strSocialId);
        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), str_image_path);
        RequestBody language = RequestBody.create(MediaType.parse("text/plain"), str_image_path);

        Call<SuccessResSocialLogin> signupCall = apiInterface.socialLogin(firstName, email, mobile, lat, lng, registerID, socialId, language, image);
        signupCall.enqueue(new Callback<SuccessResSocialLogin>() {
            @Override
            public void onResponse(Call<SuccessResSocialLogin> call, Response<SuccessResSocialLogin> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSocialLogin data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID, data.getResult().getId());
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        Toast.makeText(LoginAct.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginAct.this, HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                        //  boolean val =  SharedPreferenceUtility.getInstance(LoginAct.this).getBoolean(Constant.SELECTED_LANGUAGE);

                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSocialLogin> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            strName = user.getDisplayName();
                            strEmail = user.getEmail();

                            if (strEmail == null) {
                                strEmail = "";
                            }

                            str_image_path = user.getPhotoUrl().toString();
                            strSocialId = user.getUid();
                            strPhoneNumber = user.getPhoneNumber();
                            if (strPhoneNumber == null) {
                                strPhoneNumber = "";
                            }

                            socialLogin();
                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginAct.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }
                    }
                });
    }

    private void login() {
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", strEmail);
        map.put("password", strPassword);
        map.put("register_id", deviceToken);
        map.put("language", strLanguage);

        Call<SuccessResLogin> call = apiInterface.login(map);

        call.enqueue(new Callback<SuccessResLogin>() {
            @Override
            public void onResponse(Call<SuccessResLogin> call, Response<SuccessResLogin> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResLogin data = response.body();
                    Log.e("data", data.status);

                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID, data.getResult().getId());
                        Toast.makeText(LoginAct.this, "" + R.string.logged_in_successfull, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginAct.this, HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResLogin> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (strPassword.equalsIgnoreCase("")) {
            binding.etPassword.setError(getString(R.string.please_enter_pass));
            return false;
        }
        return true;
    }

    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginAct.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}