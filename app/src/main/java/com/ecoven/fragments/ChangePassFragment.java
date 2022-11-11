package com.ecoven.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.databinding.FragmentChangePassBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResForgetPassword;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassFragment extends Fragment {

    FragmentChangePassBinding binding;
    EcoVanInterface apiInterface;

    String oldPass = "", newConfirmPass = "", newPass = "", pass = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePassFragment newInstance(String param1, String param2) {
        ChangePassFragment fragment = new ChangePassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_pass, container, false);

        binding.layHeader.imgHeader.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });


        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);

        binding.btnSave.setOnClickListener(v ->
                {

                    oldPass = binding.etPass.getText().toString().trim();
                    newPass = binding.etNewPass.getText().toString().trim();
                    newConfirmPass = binding.etNewConPass.getText().toString().trim();

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                            changePassword();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
                    }

                }
        );

        return binding.getRoot();
    }

    private boolean isValid() {

        if (oldPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_old_pass));
            return false;
        } else if (newPass.equalsIgnoreCase("")) {
            binding.etNewPass.setError(getString(R.string.enter_new_password));
            return false;
        } else if (newConfirmPass.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.please_enter_confirm_password));
            return false;

        } else if (!newConfirmPass.equalsIgnoreCase(newPass)) {
            binding.etNewConPass.setError(getString(R.string.password_mismatched));
            return false;
        }

        return true;
    }

    public void changePassword() {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("current_password", oldPass);
        map.put("password", newPass);

        Log.e("MapMap", "LOGIN REQUEST" + map);

        Call<SuccessResForgetPassword> SignupCall = apiInterface.changePass(map);

        SignupCall.enqueue(new Callback<SuccessResForgetPassword>() {
            @Override
            public void onResponse(Call<SuccessResForgetPassword> call, Response<SuccessResForgetPassword> response) {
                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResForgetPassword data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "LOGIN RESPONSE" + dataResponse);

                    if (data.status.equals("1")) {

                        Toast.makeText(getContext(), "SuccessFully Change Your Password !!", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Sudzfv", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Error>>>>", "" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResForgetPassword> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Please Enter Correct Password !!!", Toast.LENGTH_SHORT).show();
                DataManager.getInstance().hideProgressMessage();
            }
        });


    }
}