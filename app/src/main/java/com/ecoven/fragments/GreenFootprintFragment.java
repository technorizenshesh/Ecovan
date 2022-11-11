package com.ecoven.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ecoven.R;
import com.ecoven.databinding.FragmentGreenFootprintBinding;
import com.ecoven.util.BottomSheet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GreenFootprintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GreenFootprintFragment extends Fragment {

     FragmentGreenFootprintBinding binding;
    ArrayAdapter ad;

    String[] category = {"Everthing"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GreenFootprintFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GreenFootprintFragment newInstance(String param1, String param2) {
        GreenFootprintFragment fragment = new GreenFootprintFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_green_footprint,
                container, false);

        binding.rlDropDownInstitute.setOnClickListener(v ->
                {
                    BottomSheet bottomSheetFragment= new BottomSheet();
                    bottomSheetFragment.show(getActivity().getSupportFragmentManager(),"ModalBottomSheet");
                   }
                );

     // setSpinners();
        return binding.getRoot();
    }




    private void setSpinners() {

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                category);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

   /*     binding.spinner1.setAdapter(ad);

        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                category);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinner2.setAdapter(ad);

        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/

    }
}