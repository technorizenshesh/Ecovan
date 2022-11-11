package com.ecoven.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.activities.SignupAct;
import com.ecoven.adapter.ProductAdapter;
import com.ecoven.databinding.FragmentProductsBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.Constant;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.GetProductModal;
import com.ecoven.retrofit.models.SuccessResGetCountryDistance;
import com.ecoven.retrofit.models.SuccessResGetCoutries;
import com.ecoven.utility.DataManager;
import com.ecoven.utility.GPSTracker;
import com.ecoven.utility.SharedPreferenceUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ecoven.retrofit.Constant.USER_ID;
import static com.ecoven.retrofit.Constant.showToast;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ProductsFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    FragmentProductsBinding binding;

    ArrayAdapter ad;

    String[] category = {"Weather"};

    // TODO: Rename parameter arguments, choose names that match

    private String myCountry="",myDistance="",myCategory="";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";

    // TODO: Rename and change types of parameters
    private EcoVanInterface apiInterface;
    private List<GetProductModal.Result> get_result;
//    private ArrayList<SuccessResGetCoutries.Result> countriesList = new ArrayList<>();

    private ArrayList<SuccessResGetCountryDistance.CountryDetail> countriesList = new ArrayList<>();
    private ArrayList<SuccessResGetCountryDistance.DistanceDetail> distanceList = new ArrayList<>();
    private ArrayList<SuccessResGetCountryDistance.CategoryDetail> categoryList = new ArrayList<>();

    private ArrayAdapter<String> countryArrayAdapter;
    private ArrayAdapter<String> distanceAdapter;
    private ArrayAdapter<String> categoryAdapter;
    private String[] country;

    private String selectedCountryId="",selectedDistanceId="",selectedCategoryId="";

    public ProductsFragment() {

    }

    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false);
        country = new String[]{" 10 km ", "20 km ", "30 km ", "40 km ", "50 km "};
        gpsTracker = new GPSTracker(getActivity());
        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);
        binding.ivBack.setOnClickListener(v -> getActivity().onBackPressed());
        getLocation();

        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchProducts(binding.etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getCountriesDistance();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }


    private void searchProducts(String text) {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("search_key", text);
        Log.e("MapMap", "LOGIN REQUEST" + map);

        Call<GetProductModal> getProfileModalCall = apiInterface.searchProducts(map);

        getProfileModalCall.enqueue(new Callback<GetProductModal>() {
            @Override
            public void onResponse(Call<GetProductModal> call, Response<GetProductModal> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    GetProductModal data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "Get Profile RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        get_result = data.getResult();
                        binding.rvProducts.setHasFixedSize(true);
                        binding.rvProducts.setLayoutManager(new
                                LinearLayoutManager(getActivity()));
                        binding.rvProducts.setAdapter(new
                                ProductAdapter(getActivity(), get_result));
                    } else if (data.status.equals("0")) {
                        Toast.makeText(getContext(), data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProductModal> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


//    private void getCountries() {
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//
//        Call<SuccessResGetCoutries> call = apiInterface.getCountries(map);
//        call.enqueue(new Callback<SuccessResGetCoutries>() {
//            @Override
//            public void onResponse(Call<SuccessResGetCoutries> call, Response<SuccessResGetCoutries> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//                    SuccessResGetCoutries data = response.body();
//                    if (data.status.equals("1")) {
//                        countriesList.addAll(data.getResult());
//                        initializeUI();
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResGetCoutries> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }

    private void getCountriesDistance() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Log.e("userId>>>", userId);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<SuccessResGetCountryDistance> call = apiInterface.getCountriesDistance(map);
        call.enqueue(new Callback<SuccessResGetCountryDistance>() {
            @Override
            public void onResponse(Call<SuccessResGetCountryDistance> call, Response<SuccessResGetCountryDistance> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetCountryDistance data = response.body();
                    if (data.status.equals("1")) {

                        countriesList.clear();
                        distanceList.clear();
                        categoryList.clear();

                        countriesList.addAll(data.getResult().get(0).getCountryDetails());
                        distanceList.addAll(data.getResult().get(0).getDistanceDetails());
                        categoryList.addAll(data.getResult().get(0).getCategoryDetails());

                        initializeUI();

//                        countriesList.addAll(data.getResult());
//                        initializeUI();

                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetCountryDistance> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void initializeUI() {

        ArrayList<String> spinnerListCountry = new ArrayList<>();
         spinnerListCountry.add("All");

        ArrayList<String> spinnerListDistance = new ArrayList<>();
        spinnerListDistance.add("All");

        ArrayList<String> spinnerListCategory = new ArrayList<>();
        spinnerListCategory.add("All");

        for (SuccessResGetCountryDistance.CountryDetail country : countriesList) {
            spinnerListCountry.add(country.getName());
        }

        for (SuccessResGetCountryDistance.DistanceDetail country : distanceList) {
            spinnerListDistance.add(country.getKm());
        }

        for (SuccessResGetCountryDistance.CategoryDetail country : categoryList) {
            spinnerListCategory.add(country.getCategoryName());
        }

        countryArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerListCountry);
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countrySpinner.setAdapter(countryArrayAdapter);
        binding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0)
                {
                    String idd = countriesList.get(position).id;
                    selectedCountryId = idd;
                    getProductList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        distanceAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerListDistance);
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.distance.setAdapter(distanceAdapter);
        binding.distance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0)
                {

                    String clickedItem = parent.getItemAtPosition(position).toString().trim();

                    selectedDistanceId = clickedItem.replaceFirst("KM", "");

                    selectedDistanceId = selectedDistanceId.trim();

                    getProductList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerListCategory);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categorySpinner.setAdapter(categoryAdapter);
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0)
                {
                    String idd = categoryList.get(position).id;
                    selectedCategoryId = idd;
                    getProductList();
                }
                else
                {
                    getProductList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getProductList() {

        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");
        Map<String, String> map = new HashMap<>();
        map.put("country", selectedCountryId);
        map.put("category", selectedCategoryId);
        map.put("km", selectedDistanceId);
        map.put("lat", "22.7196");
        map.put("lon", "75.8577");
        Log.e("MapMap", "LOGIN REQUEST" + map);

        Call<GetProductModal> getProfileModalCall = apiInterface.get_product_filter(map);

        getProfileModalCall.enqueue(new Callback<GetProductModal>() {
            @Override
            public void onResponse(Call<GetProductModal> call, Response<GetProductModal> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    GetProductModal data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "Get Profile RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        get_result = data.getResult();
                        binding.rvProducts.setHasFixedSize(true);
                        binding.rvProducts.setLayoutManager(new
                                LinearLayoutManager(getActivity()));
                        binding.rvProducts.setAdapter(new
                                ProductAdapter(getActivity(), get_result));
                    } else if (data.status.equals("0")) {
                        Toast.makeText(getContext(), data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProductModal> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;
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
                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
                    strLat = Double.toString(gpsTracker.getLatitude());
                    strLng = Double.toString(gpsTracker.getLongitude());

                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }




}