package com.ecoven.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.ecoven.R;
import com.ecoven.databinding.FragmentCodesBinding;
import com.google.zxing.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodesFragment extends Fragment {

    FragmentCodesBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CodeScanner mCodeScanner;

    public CodesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CodesFragment newInstance(String param1, String param2) {
        CodesFragment fragment = new CodesFragment();
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_codes, container, false);
        SetupUI();
        return binding.getRoot();
    }

    private void SetupUI() {

        mCodeScanner = new CodeScanner(getContext(), binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();
                        Log.d("TAG", "scanner code +: "+result.getText());
//                        Toast.makeText(getContext(), result.getText(), Toast.LENGTH_SHORT).show();
                        mCodeScanner.startPreview();
                    }
                });
            }
        });

        binding.scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mCodeScanner.startPreview();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mCodeScanner.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        try {
            mCodeScanner.releaseResources();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    public void showDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_qr_code_succes);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        AppCompatButton btnLogin = dialog.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
                );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}