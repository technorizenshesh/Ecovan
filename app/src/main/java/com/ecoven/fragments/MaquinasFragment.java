package com.ecoven.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ecoven.R;
import com.ecoven.adapter.MachineAdapter;
import com.ecoven.databinding.FragmentMaquinasBinding;
import com.ecoven.retrofit.ApiClient;
import com.ecoven.retrofit.EcoVanInterface;
import com.ecoven.retrofit.NetworkAvailablity;
import com.ecoven.retrofit.models.MachineListModal;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaquinasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaquinasFragment extends Fragment {

    FragmentMaquinasBinding binding;

    ArrayAdapter ad;

    private ArrayAdapter<String> distanceAdapter;

    private String selectedCountryId="",selectedDistanceId="";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EcoVanInterface apiInterface;
    private ArrayList<SuccessResGetCountryDistance.CountryDetail> countriesList = new ArrayList<>();
    private ArrayList<SuccessResGetCountryDistance.DistanceDetail> distanceList = new ArrayList<>();

    private ArrayAdapter<String> countryArrayAdapter;
    private List<MachineListModal.Result> get_result;
    private GPSTracker gps_tracker;
    private double latitute;
    private double longitute;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private String dfjhdfa;
    private String clickedItem1;
    private String country_list_id;

    public MaquinasFragment() {
    }

    public static MaquinasFragment newInstance(String param1, String param2) {
        MaquinasFragment fragment = new MaquinasFragment();
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maquinas, container, false);
        apiInterface = ApiClient.getClient().create(EcoVanInterface.class);

        gps_tracker = new GPSTracker(getContext());

        latitute = gps_tracker.getLatitude();
        longitute = gps_tracker.getLongitude();

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getCountriesDistance();

        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.tvTime.setOnClickListener(v ->
                {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.time_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                }
        );

        return binding.getRoot();
    }

    public boolean checkPermisssionForReadStorage() {

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_CONSTANT);

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_CONSTANT);
            }

            return false;

        } else {
            return true;
        }
    }

    private void getMachineList() {

        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");
        Map<String, String> map = new HashMap<>();
        map.put("distance", selectedDistanceId);
        map.put("lat", String.valueOf(latitute));
        map.put("lon", String.valueOf(longitute));
        map.put("country_id", selectedCountryId);

        Log.e("params", "params>>>" + map);
        Call<MachineListModal> getProfileModalCall = apiInterface.get_machines(map);

        getProfileModalCall.enqueue(new Callback<MachineListModal>() {
            @Override
            public void onResponse(Call<MachineListModal> call, Response<MachineListModal> response) {
                DataManager.getInstance().hideProgressMessage();

                try {

                    MachineListModal data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "Get Profile RESPONSE" + dataResponse);

                    if (data.status.equals("1")) {

                        get_result = data.getResult();
                        binding.recyclerMachine.setHasFixedSize(true);
                        binding.recyclerMachine.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.recyclerMachine.setAdapter(new MachineAdapter(getActivity(), get_result));

                    } else if (data.status.equals("0")) {
                        Toast.makeText(getContext(), data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MachineListModal> call, Throwable t) {
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

        for (SuccessResGetCountryDistance.CountryDetail country : countriesList) {
            spinnerListCountry.add(country.getName());
        }

        for (SuccessResGetCountryDistance.DistanceDetail country : distanceList) {
            spinnerListDistance.add(country.getKm());
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
                    getMachineList();
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
                    getMachineList();
                } else
                {
                    getMachineList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


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


                        countriesList.addAll(data.getResult().get(0).getCountryDetails());
                        distanceList.addAll(data.getResult().get(0).getDistanceDetails());

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





}