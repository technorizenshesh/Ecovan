package com.ecoven.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.adapter.NewsAdapter;
import com.ecoven.databinding.FragmentHomeBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResGetActivity;
import com.ecoven.retrofit.models.SuccessResGetAllNews;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.NewsClickListener;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.showToast;

public class HomeFragment extends Fragment
        implements NewsClickListener {

    FragmentHomeBinding binding;

    EcoVanInterface apiInterface;

    private ArrayList<SuccessResGetAllNews.Result> newsList = new ArrayList<>();
    private ArrayList<SuccessResGetActivity.Result> activityList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);

        binding.btnGotoboard.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_boardFragment);
                }
        );

        binding.ivEdits.setOnClickListener(v ->

                {
                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_navigation_profile);
                }
        );

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getNews();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

     public void getNews() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<SuccessResGetAllNews> call = apiInterface.getNews(map);

        call.enqueue(new Callback<SuccessResGetAllNews>() {
            @Override
            public void onResponse(Call<SuccessResGetAllNews> call, Response<SuccessResGetAllNews> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetAllNews data = response.body();
                    Log.e("data", data.status);

                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        newsList.clear();
                        newsList.addAll(data.getResult());

                        binding.rvNews.setHasFixedSize(true);
                        binding.rvNews.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        binding.rvNews.setAdapter(new NewsAdapter(getActivity(), newsList,"home"));

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetAllNews> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void newsClickListener(View view, String newsId) {

    }
}