package com.ecoven.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.activities.HomeAct;
import com.ecoven.activities.LoginAct;
import com.ecoven.databinding.FragmentCodesBinding;
import com.ecoven.databinding.FragmentContactUsBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.Constant;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResContactUs;
import com.ecoven.retrofit.models.SuccessResContactUs;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.isValidEmail;
import static com.ecoven.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {

    FragmentContactUsBinding binding;

    private EcoVanInterface apiInterface;
    
    private String name="",email="",description="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contact_us, container, false);

        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);
        
        binding.ivBack.setOnClickListener(v -> getActivity().onBackPressed());

        binding.btnSubit.setOnClickListener(v -> {

            name = binding.etName.getText().toString().trim();
            email = binding.etEmail.getText().toString().trim();
            description = binding.etDescription.getText().toString().trim();

            if (isValid()) {

                if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                    submit();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }

        });

        return binding.getRoot();
    }

    private boolean isValid() {
        if (name.equalsIgnoreCase("")) {
            binding.etName.setError(getString(R.string.enter_name));
            return false;
        }else  if (email.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(email)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (description.equalsIgnoreCase("")) {
            binding.etDescription.setError(getString(R.string.please_enter_description));
            return false;
        }
        return true;
    }

    private void submit()
    {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("user_name", name);
        map.put("email", email);
        map.put("message", description);

        Call<SuccessResContactUs> call = apiInterface.contactUs(map);

        call.enqueue(new Callback<SuccessResContactUs>() {
            @Override
            public void onResponse(Call<SuccessResContactUs> call, Response<SuccessResContactUs> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResContactUs data = response.body();
                    Log.e("data", data.status);

                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResContactUs> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    
}