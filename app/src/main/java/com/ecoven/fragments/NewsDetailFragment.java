package com.ecoven.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecoven.R;
import com.ecoven.adapter.NewsAdapter;
import com.ecoven.adapter.NewsTagsAdapter;
import com.ecoven.adapter.RelatedNewsAdapter;
import com.ecoven.databinding.FragmentNewsDetailBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.SuccessResGetAllNews;
import com.ecoven.retrofit.models.SuccessResNewsDetails;
import com.ecoven.retrofit.models.SuccessResNewsDetails;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NewsDetailFragment extends Fragment {

    FragmentNewsDetailBinding binding;

    private String newsId="";

    private EcoVanInterface apiInterface;

    private ArrayList<SuccessResGetAllNews.Result> newsList = new ArrayList<>();

    private SuccessResNewsDetails.Result newDetail;

    private ArrayList<String> tagsList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static NewsDetailFragment newInstance(String param1, String param2) {
        NewsDetailFragment fragment = new NewsDetailFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news_detail, container, false);

        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);

        binding.ivBack.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );

        Bundle bundle = getArguments();

        if(bundle!=null)
        {
            newsId = bundle.getString("id");
        }


        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getNews();
            getRelatedNews();

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
        map.put("news_id", newsId);
        Call<SuccessResNewsDetails> call = apiInterface.getNewsDetails(map);

        call.enqueue(new Callback<SuccessResNewsDetails>() {
            @Override
            public void onResponse(Call<SuccessResNewsDetails> call, Response<SuccessResNewsDetails> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResNewsDetails data = response.body();
                    Log.e("data", data.status);

                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        newDetail = data.getResult().get(0);
                        setData();
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResNewsDetails> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setData()
    {

        String tags = newDetail.getNewsTag();

        List<String> items = Arrays.asList(tags.split("\\s*,\\s*"));

        binding.rvTags.setHasFixedSize(true);
        binding.rvTags.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvTags.setAdapter(new NewsTagsAdapter(getActivity(),items));

        Glide.with(getActivity())
                .load(newDetail.getImage())
                .into(binding.ivNews);

        binding.tvChannel.setText(newDetail.getChanelName());
        binding.tvEditor.setText(newDetail.getEditorName());
        binding.tvTitle.setText(newDetail.getTitle());
        binding.tvTimeAgo.setText(Html.fromHtml(getString(R.string.dot)+"    "+newDetail.getTimeAgo()));
        binding.tvDesciption.setText(Html.fromHtml(newDetail.getDescription()));

    }

    public void getRelatedNews() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("news_id", newsId);
        Call<SuccessResGetAllNews> call = apiInterface.getRelatedNews(map);

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

                        binding.newsRelated.setHasFixedSize(true);
                        binding.newsRelated.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                        binding.newsRelated.setAdapter(new RelatedNewsAdapter(getActivity(),newsList));

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


}