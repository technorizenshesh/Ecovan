package com.ecoven.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoven.R;
import com.ecoven.adapter.TicketAdapter;
import com.ecoven.databinding.FragmentMyTicketsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTicketsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTicketsFragment extends Fragment {


    FragmentMyTicketsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyTicketsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTicketsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTicketsFragment newInstance(String param1, String param2) {
        MyTicketsFragment fragment = new MyTicketsFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_tickets, container, false);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );
        binding.header.tvHeader.setText(R.string.my_tickets);


        binding.rvTickets.setHasFixedSize(true);
        binding.rvTickets.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTickets.setAdapter(new TicketAdapter(getActivity()));
        return binding.getRoot();
    }
}