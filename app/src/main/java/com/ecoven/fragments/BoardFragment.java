package com.ecoven.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoven.R;
import com.ecoven.activities.SelectLangActivity;
import com.ecoven.activities.SplashAct;
import com.ecoven.databinding.FragmentBoardBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment {

    FragmentBoardBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    public BoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoardFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
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
/*            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);

        binding.llCard.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_myTicketsFragment);
                }
        );

        binding.llProducts.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_navigation_products);
                }
        );

        binding.llNews.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_newsFragment);
                }
        );

        binding.llContact.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_boardFragment_to_contactUsFragment);
                }
        );

        binding.btnChangeLang.setOnClickListener(v ->
        {
            startActivity(new Intent(getActivity(), SelectLangActivity.class).putExtra("from","Home"));
        });

        return binding.getRoot();

    }
}